package `in`.insideandroid.bangthefinger_ytb

import android.app.Dialog
import android.media.MediaActionSound
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    private var score = 50
    private val threshold = 5

    private lateinit var blueView: View
    private lateinit var redView: View
    private lateinit var guideline: Guideline
    private lateinit var dialog: Dialog

    private lateinit var mediaPlayer : MediaPlayer
    private val sound = MediaActionSound()

    private lateinit var dialogImage: ImageView
    private lateinit var dialogText: TextView
    private lateinit var dialogButton: AppCompatButton

    //this is sample change 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.getWindowInsetsController(window.decorView)?.let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContentView(R.layout.activity_main)

        initView()
        initListener()
    }

    private fun initListener() {
        redView.setOnClickListener {
            sound.play(MediaActionSound.STOP_VIDEO_RECORDING)
            score -= threshold
            checkWin()
        }

        blueView.setOnClickListener {
            sound.play(MediaActionSound.STOP_VIDEO_RECORDING)
            score += threshold
            checkWin()
        }
    }

    private fun checkWin() {
        guideline.setGuidelinePercent(score / 100f)
        if(score == 0){
            //red win
            showWinDialog(0)
        } else if(score == 100){
            //blue win
            showWinDialog(1)
        }
    }

    private fun showWinDialog(player: Int){
        mediaPlayer.start()
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialogImage = dialog.findViewById(R.id.ivDialog)
        dialogText = dialog.findViewById(R.id.tvDescription)
        dialogButton = dialog.findViewById(R.id.btDialog)

        if(player == 0){
            dialogImage.setBackgroundColor(getColor(R.color.red))
            dialogButton.setBackgroundColor(getColor(R.color.red))
            dialogText.text = "Red wins!"
        } else {
            dialogImage.setBackgroundColor(getColor(R.color.blue))
            dialogButton.setBackgroundColor(getColor(R.color.blue))
            dialogText.text = "Blue wins!"
        }
        dialog.show()

        dialogButton.setOnClickListener {
            dialog.dismiss()
            resetScore()
        }
    }

    override fun onPause() {
        super.onPause()
        if(::dialog.isInitialized && dialog.isShowing){
            dialog.dismiss()
            resetScore()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        if(::dialog.isInitialized && dialog.isShowing){
            dialog.dismiss()
        }
    }

    private fun resetScore() {
        score = 50
        guideline.setGuidelinePercent(0.5f)
    }

    private fun initView() {
        val resource = resources.getIdentifier("win","raw",packageName)
        mediaPlayer = MediaPlayer.create(this, resource)
        blueView = findViewById(R.id.vBlue)
        redView = findViewById(R.id.vRed)
        guideline = findViewById(R.id.guideline2)
    }
}