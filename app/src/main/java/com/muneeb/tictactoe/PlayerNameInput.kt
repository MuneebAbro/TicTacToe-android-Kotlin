package com.muneeb.tictactoe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PlayerNameInput : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_name_input)


        window.statusBarColor = ContextCompat.getColor(this, R.color.bg)
        // Access views using findViewById
        val buttonOk = findViewById<Button>(R.id.okBtn)

        // Set onClickListener for the button
        buttonOk.setOnClickListener {
            vibrateDevice()

            if (startGame()) {
                finish()
            }
        }


    }

    private fun startGame(): Boolean {
        val player1EditText = findViewById<EditText>(R.id.playerOne)
        val player2EditText = findViewById<EditText>(R.id.playerTwo)
        val player1Name = player1EditText.text.toString().trim()
        val player2Name = player2EditText.text.toString().trim()

        return if(player1Name.isEmpty() || player2Name.isEmpty()) {
            showAlertDialog("Enter Names")
            false
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PLAYER_1_NAME", player1Name)
            intent.putExtra("PLAYER_2_NAME", player2Name)
            startActivity(intent)
            true
        }
    }

    private fun showAlertDialog(message: String) {
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        val messageTextView = dialogView.findViewById<TextView>(R.id.alert_message)
        messageTextView.text = message

        val errorIcon = dialogView.findViewById<ImageView>(R.id.alertImage)
        errorIcon.setImageResource(R.drawable.error)

        val okButton = dialogView.findViewById<Button>(R.id.alert_button_ok)
        okButton.setOnClickListener {
            vibrateDevice()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun vibrateDevice() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    companion object {
        // Vibration duration in milliseconds
        private const val VIBRATION_DURATION = 50L
    }

}
