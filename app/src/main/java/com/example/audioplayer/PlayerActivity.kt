package com.example.audioplayer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import com.example.audioplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        binding.playPause.setOnClickListener {
            if (isPlaying) {
                sendIntent(PlayerService.Action.PAUSE)
                isPlaying = false
            } else {
                sendIntent(PlayerService.Action.PLAY)
                isPlaying = true
            }
            renderPlayPauseButton()
        }

        binding.next.setOnClickListener {
            sendIntent(PlayerService.Action.NEXT)
            if (!isPlaying) {
                isPlaying = true
                setPauseButton()
            }
        }

        binding.previous.setOnClickListener {
            sendIntent(PlayerService.Action.PREVIOUS)
            if (!isPlaying) {
                isPlaying = true
                setPauseButton()
            }
        }
    }

    private fun sendIntent(action: PlayerService.Action) {
        Intent(this, PlayerService::class.java).also {
            it.action = action.toString()
            this.startService(it)
        }
    }

    private fun renderPlayPauseButton() {
        binding.playPause.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )
        )
    }

    private fun setPauseButton() {
        binding.playPause.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_pause
            )
        )
    }
}