package kg.android.onemorestepapp.service.broadcastrecievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kg.android.onemorestepapp.service.foregroundservice.StepCounterService

class ShutdownReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        context.startService(Intent(context, StepCounterService::class.java))

        // if the user used a root script for shutdown, the DEVICE_SHUTDOWN
        // broadcast might not be send. Therefore, the app will check this
        // setting on the next boot and displays an error message if it's not
        // set to true
       /* context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
            .putBoolean("correctShutdown", true).commit()
        val db: Database = Database.getInstance(context)
        // if it's already a new day, add the temp. steps to the last one
        if (db.getSteps(Util.getToday()) === Int.MIN_VALUE) {
            val steps: Int = db.getCurrentSteps()
            db.insertNewDay(Util.getToday(), steps)
        } else {
            db.addToLastEntry(db.getCurrentSteps())
        }*/
        // current steps will be reset on boot @see BootReceiver
        //db.close()
    }
}