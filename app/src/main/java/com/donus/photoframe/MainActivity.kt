package com.donus.photoframe

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.donus.photoframe.databinding.ActivityMainBinding
import java.io.File
import kotlin.concurrent.fixedRateTimer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val iv = findViewById<ImageView>(R.id.imageView)
        val directory = File(
            "/storage/sdcard1/ptf"
        )

        val rawList = arrayListOf<File>()

        if (directory.exists()) {
            directory.walk().forEach { file ->
                if (file.isFile && file.path.endsWith(".jpg") && !file.nameWithoutExtension.startsWith('.')) {
                    rawList.add(file)
                }
            }
        }

        rawList.shuffle()

        fixedRateTimer("timer", false, 0L,  20 * 1000) {
            this@MainActivity.runOnUiThread {
                Glide.with(this@MainActivity)
                    .load(rawList[i++])
                    .into(iv);

                if (i >= rawList.size) {
                    i = 0
                }
            }
        }
    }

}