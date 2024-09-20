package info.androidhive.exoplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.androidhive.exoplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Sample media urls
    private val mp4Url =
        "https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fsample2.mp4?alt=media&token=2f09078b-6c87-4159-9054-73c9b88d665b"
    private val mp3Url = "https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fnightfall-future-bass-music-228100.mp3?alt=media&token=32821471-654b-4a9e-9790-1e9c7d1cc584"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.btnMp4.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("url", mp4Url)
            })
        }

        binding.content.btnMp3.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("url", mp3Url)
            })
        }

        binding.content.btnPlaylist.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }
    }
}