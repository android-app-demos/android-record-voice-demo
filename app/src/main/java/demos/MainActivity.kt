package demos

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.Button
import java.io.File

class MainActivity : AppCompatActivity() {

    private val MY_RECORD_AUDIO_REQUEST_CODE = 111

    private val button by lazy<Button> { findViewById(R.id.button) }

    private val audioFile by lazy { File(filesDir, "voice.mp4") }

    private val recorder by lazy { MediaRecorder() }

    private val player by lazy {
        MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupButton(button)
    }

    private fun setupButton(button: Button) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (hasRecordPermission()) {
                        button.text = "Recording ..............."
                        setupRecorder(recorder)
                        recorder.setOutputFile(audioFile.absolutePath)
                        recorder.prepare()
                        recorder.start()
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), MY_RECORD_AUDIO_REQUEST_CODE)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    button.text = "Record Voice"
                    if (hasRecordPermission()) {
                        if (stopRecording()) {
                            play()
                        }
                    }
                }
            }
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_RECORD_AUDIO_REQUEST_CODE -> {
                println("Permission request for recording voice: $requestCode")
                println("permissions: $permissions")
                println("results: $grantResults")
            }
            else -> println("Not my requests: $requestCode")
        }
    }

    private fun stopRecording(): Boolean {
        return try {
            recorder.stop()
            true
        } catch (e: Exception) {
            println(e.toString())
            false
        }
    }

    private fun hasRecordPermission() = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    private fun setupRecorder(recorder: MediaRecorder) {
        recorder.reset()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder.setAudioEncodingBitRate(128000)
    }

    private fun play() {
        player.reset()
        player.setDataSource(audioFile.absolutePath)
        player.prepare()
        player.start()
    }

}
