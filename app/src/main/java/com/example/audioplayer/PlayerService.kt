package com.example.audioplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PlayerService() : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val resourcesIds = arrayOf(
        "dozhd.mp3",
        "cho_takoe_osen.mp3",
        "eto_vse.mp3",
        "ne_strelyay.mp3"
    )
    private var currentlyPlaying = 0

    override fun onCreate() {
        super.onCreate()
        buildNotification()
        mediaPlayer = MediaPlayer().also {
            it.setOnCompletionListener { nextTrack() }
        }
    }

    private fun buildNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pause)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Action.PLAY.toString() -> play()
            Action.PAUSE.toString() -> pause()
            Action.PREVIOUS.toString() -> previousTrack()
            Action.NEXT.toString() -> nextTrack()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun play() {
        preparePlayer()
        mediaPlayer.start()
    }

    private fun preparePlayer() {
        val afd = assets.openFd(resourcesIds[currentlyPlaying])
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        afd.close()
        mediaPlayer.prepare()
    }

    private fun pause() {
        mediaPlayer.pause()
    }

    private fun nextTrack() {
        resetPlayer()
        if (currentlyPlaying == resourcesIds.size - 1) currentlyPlaying = 0
        else currentlyPlaying++
        play()
    }

    private fun previousTrack() {
        resetPlayer()
        if (currentlyPlaying - 1 < 0) currentlyPlaying = resourcesIds.size - 1
        else currentlyPlaying--
        play()
    }

    private fun resetPlayer() {
        with(mediaPlayer) {
            stop()
            reset()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    enum class Action {
        PLAY, PAUSE, PREVIOUS, NEXT
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "player_channel"
    }
}