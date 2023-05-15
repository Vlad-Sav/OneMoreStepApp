package kg.android.onemorestepapp.service.broadcastrecievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import kg.android.onemorestepapp.service.foregroundservice.StepCounterService
import kg.android.onemorestepapp.utils.apiwrappers.API26Wrapper

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= 26) {
            API26Wrapper.startService(
                context,
                Intent(context, StepCounterService::class.java)
            )
        } else {
            context.startService(Intent(context, StepCounterService::class.java))
        }
    }
}