package com.escape99.minimalpiano

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var headingButton: Button
    private lateinit var soundPool: SoundPool
    private var sound1: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
        sound1 = soundPool.load(this, R.raw.acoustic_grand_piano_a3, 1)

        headingButton = findViewById(R.id.headingButton)

        headingButton.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                soundPool.play(sound1, 1F, 1F, 0, 0, 1F)
            }
            return@setOnTouchListener view.onTouchEvent(motionEvent)
        }

    }

}
