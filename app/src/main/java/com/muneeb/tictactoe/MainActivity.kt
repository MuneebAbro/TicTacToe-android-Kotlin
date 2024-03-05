package com.muneeb.tictactoe

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<ImageButton>
    private var currentPlayer: Int = 1
    private var boardState: Array<Int> = Array(9) { 0 }
    private var winner1 = ""
    private lateinit var player1Name: String
    private lateinit var player2Name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, R.color.bg)

        player1Name = intent.getStringExtra("PLAYER_1_NAME") ?: "Player 1"
        player2Name = intent.getStringExtra("PLAYER_2_NAME") ?: "Player 2"

        updatePlayerTurnText()

        buttons = arrayOf(
            findViewById(R.id.btn1), findViewById(R.id.btn2), findViewById(R.id.btn3),
            findViewById(R.id.btn4), findViewById(R.id.btn5), findViewById(R.id.btn6),
            findViewById(R.id.btn7), findViewById(R.id.btn8), findViewById(R.id.btn9)
        )


        for (i in buttons.indices) {
            buttons[i].setOnClickListener { onButtonClick(i) }
        }
    }

    private fun onButtonClick(index: Int) {
        if (boardState[index] == 0) {

            // Handle the game logic
            boardState[index] = currentPlayer
            buttons[index].setImageResource(if (currentPlayer == 1) R.drawable.ic_baseline_close_24 else R.drawable.ic_baseline_panorama_fish_eye_24)
            if (checkForWin()) {
                val winner = if (currentPlayer == 1) player1Name else player2Name
                val capitalizedPlayerName = winner.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
                showToast("$capitalizedPlayerName Wins!")
                winner1 = winner
                resetGame()
                updatePlayerTurnText()
            } else if (boardState.all { it != 0 }) {
                showToast("It's a draw!")
                resetGame()
                updatePlayerTurnText()
            } else {
                currentPlayer = if (currentPlayer == 1) 2 else 1 // Switch player
                updatePlayerTurnText()
            }

            // Vibrate the device
            vibrateDevice()
        }
    }


    private fun checkForWin(): Boolean {
        val winPatterns = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Rows
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Columns
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6) // Diagonals
        )

        for (pattern in winPatterns) {
            if (boardState[pattern[0]] != 0 &&
                boardState[pattern[0]] == boardState[pattern[1]] &&
                boardState[pattern[0]] == boardState[pattern[2]]
            ) {
                return true
            }
        }
        return false
    }

    private fun showToast(message: String) {
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        val alertImage = dialogView.findViewById<ImageView>(R.id.alertImage)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val alertDialog = builder.create()

        if (currentPlayer == 1) {
            alertImage.setBackgroundResource(R.drawable.ic_baseline_close_24)
            val rotateanimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_light)
            alertImage.startAnimation(rotateanimation)
        } else {
            alertImage.setBackgroundResource(R.drawable.ic_baseline_panorama_fish_eye_24)
            val rotateanimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
            alertImage.startAnimation(rotateanimation)
        }

        val messageTextView = dialogView.findViewById<TextView>(R.id.alert_message)
        messageTextView.text = message

        val okButton = dialogView.findViewById<Button>(R.id.alert_button_ok)
        okButton.text = getString(R.string.play_again)
        okButton.setOnClickListener {
            alertDialog.dismiss()
            if (winner1.isNotEmpty()) {
                vibrateDevice()
                resetGame()
            }
        }

        alertDialog.show()
    }

    private fun resetGame() {
        boardState = Array(9) { 0 }
        for (button in buttons) {
            button.setImageResource(android.R.color.transparent)
        }
        currentPlayer = 1
    }

    private fun updatePlayerTurnText() {
        val player = findViewById<TextView>(R.id.player_turn)
        val currentPlayerName = if (currentPlayer == 1) player1Name else player2Name
        val textIcon = findViewById<ImageView>(R.id.textIcon)

        if (currentPlayerName == player1Name) {
            textIcon.setBackgroundResource(R.drawable.ic_baseline_close_24)
            val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation_light)
            textIcon.visibility = View.VISIBLE
            textIcon.startAnimation(rotationAnimation)
        } else {
            textIcon.setBackgroundResource(R.drawable.ic_baseline_panorama_fish_eye_24)
            textIcon.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
            textIcon.startAnimation(rotateAnimation)
        }

        val capitalizedPlayerName = currentPlayerName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        "$capitalizedPlayerName's Turn".also { player.text = it }

        val textColor = if (currentPlayer == 1) R.color.light_blue else R.color.yellow
        player.setTextColor(ContextCompat.getColor(this, textColor))
    }


    private fun vibrateDevice() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }


    companion object {
        private const val VIBRATION_DURATION = 60L

    }
}
