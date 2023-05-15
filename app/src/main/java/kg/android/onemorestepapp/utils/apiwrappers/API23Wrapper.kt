package kg.android.onemorestepapp.utils.apiwrappers

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build

@TargetApi(Build.VERSION_CODES.M)
class API23Wrapper {
    companion object{
        fun requestPermission(a: Activity, permissions: Array<String?>?) {
            a.requestPermissions(permissions!!, 42)
        }

        fun setAlarmWhileIdle(
            am: AlarmManager, type: Int, time: Long,
            intent: PendingIntent?
        ) {
            am.setAndAllowWhileIdle(type, time, intent)
        }
    }
}