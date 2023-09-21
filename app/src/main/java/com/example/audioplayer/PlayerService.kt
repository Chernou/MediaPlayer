package com.example.audioplayer

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PlayerService() : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var actionProvider: ActionProvider? = null
    private var notification: Notification? = null
    private val assetsIds = arrayOf(
        "dozhd.mp3",
        "cho_takoe_osen.mp3",
        "eto_vse.mp3",
        "ne_strelyay.mp3"
    )
    private var currentlyPlaying = 0
    private var isPlaying = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer().also {
            it.setOnCompletionListener { nextTrack() }
        }
        preparePlayer()
    }

    private fun preparePlayer() {
        val afd = assets.openFd(assetsIds[currentlyPlaying])
        mediaPlayer.setDataSource(afd)
        afd.close()
        mediaPlayer.prepare()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.PLAY_PAUSE.toString() -> {
                if (isPlaying) pause() else play()
            }
            Action.PREVIOUS.toString() -> previousTrack()
            Action.NEXT.toString() -> nextTrack()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun pause() {
        mediaPlayer.pause()
        isPlaying = false
    }

    private fun play() {
        if (notification == null) buildNotification()
        mediaPlayer.start()
        isPlaying = true
    }

    private fun buildNotification() {
        actionProvider = ActionProvider(this)
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(resources.getString(R.string.audio_player))
            .addAction(actionProvider!!.getPreviousAction())
            .addAction(actionProvider!!.getPlayAction())
            .addAction(actionProvider!!.getNextAction())
            .build()
        startForeground(NOTIFICATION_ID, notification)
        actionProvider = null
    }

    private fun previousTrack() {
        resetPlayer()
        if (currentlyPlaying - 1 < 0) currentlyPlaying = assetsIds.size - 1
        else currentlyPlaying--
        preparePlayer()
        play()
    }

    private fun nextTrack() {
        resetPlayer()
        if (currentlyPlaying == assetsIds.size - 1) currentlyPlaying = 0
        else currentlyPlaying++
        preparePlayer()
        play()
    }

    private fun resetPlayer() {
        with(mediaPlayer) {
            stop()
            reset()
        }
    }

    enum class Action {
        PLAY_PAUSE, PREVIOUS, NEXT
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "player_channel"
    }
}