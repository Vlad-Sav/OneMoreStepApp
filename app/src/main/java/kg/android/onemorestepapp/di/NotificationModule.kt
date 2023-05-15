package kg.android.onemorestepapp.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kg.android.onemorestepapp.R
import kg.android.onemorestepapp.ui.MainActivity
import kg.android.onemorestepapp.utils.Constants.ACTION_NAVIGATE_TO_MAPS_FRAGMENT
import kg.android.onemorestepapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import kg.android.onemorestepapp.utils.Constants.PENDING_INTENT_REQUEST_CODE

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {
    @ServiceScoped
    @Provides
    fun providePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                PENDING_INTENT_REQUEST_CODE,
                Intent(
                    context,
                    MainActivity::class.java
                ),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                context,
                PENDING_INTENT_REQUEST_CODE,
                Intent(
                    context,
                    MainActivity::class.java
                ).apply {
                    this.action = ACTION_NAVIGATE_TO_MAPS_FRAGMENT
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentIntent(pendingIntent)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}