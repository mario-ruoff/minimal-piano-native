package com.escape99.minimalpiano

import android.content.res.Resources
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.children

class MainActivity : AppCompatActivity() {

    var keySpan = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullscreenMode()
        setContentView(R.layout.activity_main)
        setKeyWidths()
        setButtonActions()
        initializeAudio()
    }

    private fun setFullscreenMode() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun setButtonActions() {
        val decreaseSpan: Button = findViewById(R.id.decrease_span)
        val increaseSpan: Button = findViewById(R.id.increase_span)
        val keys: ConstraintLayout = findViewById(R.id.keys)

        decreaseSpan.setOnClickListener {
            if (keySpan > 0) keySpan -= 1
            println("New key span: $keySpan")
            setKeyWidths()
        }
        increaseSpan.setOnClickListener {
            if (keySpan < getKeyCount()) keySpan += 1
            println("New key span: $keySpan")
            setKeyWidths()
        }
    }

    private fun setKeyWidths() {
        val keys: ConstraintLayout = findViewById(R.id.keys)
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        for (key in keys.children) {
            val params = key.layoutParams
            if (key.tag == "whiteKey") {
                params.width = displayWidth / keySpan - 2   // minus key border
            }
            if (key.tag == "blackKey") {
                params.width = (displayWidth / keySpan * 0.65).toInt()
            }
            key.layoutParams = params
        }
    }

    private fun initializeAudio() {
        // Set up audio builder
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        val soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sounds from keys defined in xml
        val keys: ConstraintLayout = findViewById(R.id.keys)
        for (key in keys.children) {
            if (key.tag == "whiteKey" || key.tag == "blackKey") {
                val keyName = resources.getResourceEntryName(key.id)
                val soundId = resources.getIdentifier("acoustic_grand_piano_$keyName", "raw", applicationContext.packageName)
                val sound = soundPool.load(this, soundId, 1)
                key.setOnTouchListener { view, event ->
                    when(event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            soundPool.play(sound, 0.2f, 0.2f, 0, 0, 1f)
                        }
                        MotionEvent.ACTION_MOVE -> { }
                        MotionEvent.ACTION_UP -> {
                            view.performClick()
                        }
                    }
                    false
                }
            }
        }
    }

    private fun getKeyCount(): Int {
        val keys: ConstraintLayout = findViewById(R.id.keys)
        return keys.children.filter { it.tag == "whiteKey" }.count()
    }

}