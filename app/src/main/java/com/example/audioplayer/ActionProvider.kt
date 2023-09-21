package com.example.audioplayer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class ActionProvider(private val context: Context) {

    fun getPreviousAction(): NotificationCompat.Action {
        val previousIntent = Intent(context, PlayerService::class.java).also {
            it.action = PlayerService.Action.PREVIOUS.toString()
            context.startService(it)
        }
        val previousPendingIntent: PendingIntent =
            PendingIntent.getService(context, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Action.Builder(
            R.drawable.ic_previous,
            context.getString(R.string.previous), previousPendingIntent
        )
            .build()
    }

    fun getPlayAction(): NotificationCompat.Action {
        val playIntent = Intent(context, PlayerService::class.java).also {
            it.action = PlayerService.Action.PLAY_PAUSE.toString()
            context.startService(it)
        }
        val previousPendingIntent: PendingIntent =
            PendingIntent.getService(context, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Action.Builder(
            R.drawable.ic_play,
            context.getString(R.string.play_pause), previousPendingIntent
        )
            .build()
    }

    fun getNextAction(): NotificationCompat.Action {
        val pauseIntent = Intent(context, PlayerService::class.java).also {
            it.action = PlayerService.Action.NEXT.toString()
            context.startService(it)
        }
        val pausePendingIntent: PendingIntent =
            PendingIntent.getService(context, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Action.Builder(
            R.drawable.ic_next,
            context.getString(R.string.next), pausePendingIntent
        )
            .build()
    }
}