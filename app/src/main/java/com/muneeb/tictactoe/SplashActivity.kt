package com.muneeb.tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets



        }

        val imageView = findViewById<ImageView>(R.id.imageView)


        val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)

        imageView.startAnimation(rotationAnimation)

        val imageView2 = findViewById<ImageView>(R.id.imageView2)
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        imageView2.startAnimation(animation)



        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, PlayerNameInput::class.java))
            finish()
        }, 1500)
    }
}