package kg.android.onemorestepapp.utils.apiwrappers

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build

@TargetApi(Build.VERSION_CODES.O)
class API26Wrapper {
    companion object{
        fun startService(context: Context, intent: Intent?) {
            context.startService(intent)
        }
    }

}