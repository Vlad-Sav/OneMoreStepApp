package kg.android.onemorestepapp.service.foregroundservice

import android.app.*
import android.content.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.service.broadcastrecievers.ShutdownReceiver
import kg.android.onemorestepapp.service.foregroundservice.Util.Companion.getToday
import kg.android.onemorestepapp.ui.MainActivity
import kg.android.onemorestepapp.utils.apiwrappers.API23Wrapper
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class StepCounterService : Service(), SensorEventListener {
    private val TODAY_STEPS_COUNT = "today_steps_count"
    private val TOTAL_STEPS_COUNT = "total_steps_count"
    private val TODAYS_DATE = "todays_date"
    private lateinit var sharedPreferences: SharedPreferences

    private val MICROSECONDS_IN_ONE_MINUTE: Long = 60000000
    private val SAVE_OFFSET_TIME = 5000//AlarmManager.INTERVAL_HOUR
    private val SAVE_OFFSET_STEPS = 3

    private var today = 0L
    private var totalSteps = 0
    private var todayStepsCount = 0
    private var curTotalSteps = 0
    private var lastSaveTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        Log.d("steps", "Counter Created")
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

        totalSteps = loadTotalStepsCount()
        todayStepsCount = loadTodayStepsCount()
        today = loadTodaysDate()
        if(todayStepsCount < 0){
            saveTodayStepsCount(0)
        }
        Log.d("steps", "OnCreate: total steps $totalSteps, todaySteps = $todayStepsCount")
    }
    private fun saveTotalStepsCount(totalStepsCount: Int) {
        sharedPreferences.edit().putInt(TOTAL_STEPS_COUNT, totalStepsCount).apply()
    }

    private fun loadTotalStepsCount(): Int {
        return sharedPreferences.getInt(TOTAL_STEPS_COUNT, 0)
    }

    private fun saveTodayStepsCount(todayStepsCount: Int) {
        sharedPreferences.edit().putInt(TODAY_STEPS_COUNT, todayStepsCount).apply()
    }

    private fun loadTodayStepsCount(): Int {
        return sharedPreferences.getInt(TODAY_STEPS_COUNT, 0)
    }

    private fun saveTodaysDate() {
        sharedPreferences.edit().putLong(TODAYS_DATE, getToday()).apply()
    }

    private fun loadTodaysDate(): Long {
        return sharedPreferences.getLong(TODAYS_DATE, 0)
    }

    private val shutdownReceiver: BroadcastReceiver = ShutdownReceiver()

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        Log.d("steps", "OnSensorChanged: ${event.values[0]}")
        if (event.values[0] > Int.MAX_VALUE) {

            return
        } else {

            curTotalSteps = event.values[0].toInt()
            if(totalSteps == 0) {
                totalSteps = curTotalSteps
                saveTotalStepsCount(totalSteps)
            }

            updateIfNecessary()
        }
    }

    /**
     * @return true, if notification was updated
     */
    private fun updateIfNecessary(): Boolean {
        if (curTotalSteps > totalSteps + SAVE_OFFSET_STEPS ||
            curTotalSteps > 0 && System.currentTimeMillis() > lastSaveTime + SAVE_OFFSET_TIME
        ) {
            // проверка дня, если последний день в префах не равен текущему дню, обновляем префы и количество шагов
            // сохраняем steps
            if(today != getToday()){
                saveTodaysDate()
                saveTodayStepsCount(0)
                today = getToday()
                todayStepsCount = 0
            }
            val temp = totalSteps
            totalSteps = curTotalSteps
            Log.d("steps", "Saved Steps: $curTotalSteps - $temp = ${curTotalSteps - temp}")
            saveTodayStepsCount(todayStepsCount + (curTotalSteps - temp))
            todayStepsCount += (curTotalSteps - temp)
            saveTotalStepsCount(curTotalSteps)
            lastSaveTime = System.currentTimeMillis()
            return true
        } else {
            return false
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        reRegisterSensor()
        registerBroadcastReceiver()

        // restart service every hour to save the current step count
        val nextUpdate = Math.min(
            Util.getTomorrow(),
            System.currentTimeMillis() + AlarmManager.INTERVAL_HOUR
        )

        val am = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent
            .getService(
                applicationContext,
                2,
                Intent(this, StepCounterService::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        if (Build.VERSION.SDK_INT >= 23) {
            API23Wrapper.setAlarmWhileIdle(am, AlarmManager.RTC, nextUpdate, pi)
        } else {
            am[AlarmManager.RTC, nextUpdate] = pi
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        // Restart service in 500 ms
        (getSystemService(ALARM_SERVICE) as AlarmManager)[AlarmManager.RTC, System.currentTimeMillis() + 500] =
            PendingIntent
                .getService(this, 3, Intent(this, StepCounterService::class.java), 0)
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            val sm = getSystemService(SENSOR_SERVICE) as SensorManager
            sm.unregisterListener(this)
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SHUTDOWN)
    }

    private fun reRegisterSensor() {

        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        try {
            sm.unregisterListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // enable batching with delay of max 5 min
        sm.registerListener(
            this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
            SensorManager.SENSOR_DELAY_FASTEST, (5 * MICROSECONDS_IN_ONE_MINUTE).toInt()
        )
    }
   /*
   private val TODAY_STEPS_COUNT = "today_steps_count"
    private val TOTAL_STEPS_COUNT = "total_steps_count"
    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var todaySteps = 0
    private var totalSteps = 0
    private lateinit var sharedPreferences: SharedPreferences
    private var stepCountBuffer = mutableListOf<Int>()
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        todaySteps = loadTodayStepsCount()
        totalSteps = loadTotalStepsCount()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                var stepCountData = event.values[0].roundToInt()
                if (totalSteps == 0) {
                    // При первом запуске сервиса получаем количество шагов
                    totalSteps = event.values[0].roundToInt()

                }
                if(totalSteps != stepCountData) {
                    stepCountBuffer.add(stepCountData)
                    if (stepCountBuffer.size >= 10) {
                        Log.d("debug", "Previous total $totalSteps")
                        todaySteps = todaySteps + event.values[0].roundToInt() - totalSteps
                        Log.d("debug", "Previous today $todaySteps")
                        saveTodayStepsCount(todaySteps)
                        saveTotalStepsCount(event.values[0].roundToInt())
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this, stepSensor)
    }

    private fun saveTotalStepsCount(totalStepsCount: Int) {
        sharedPreferences.edit().putInt(TOTAL_STEPS_COUNT, totalStepsCount).apply()
    }

    private fun loadTotalStepsCount(): Int {
        return sharedPreferences.getInt(TOTAL_STEPS_COUNT, 0)
    }

    private fun saveTodayStepsCount(todayStepsCount: Int) {
        sharedPreferences.edit().putInt(TODAY_STEPS_COUNT, todayStepsCount).apply()
    }

    private fun loadTodayStepsCount(): Int {
        return sharedPreferences.getInt(TODAY_STEPS_COUNT, 0)
    }

    companion object {
        const val FOREGROUND_ID = 123
    }*/
}