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
    private val dashUrl =
        "https://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0"
    private val mp4Url =
        "https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fsample2.mp4?alt=media&token=2f09078b-6c87-4159-9054-73c9b88d665b"
    private val mp3Url = "https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fnightfall-future-bass-music-228100.mp3?alt=media&token=32821471-654b-4a9e-9790-1e9c7d1cc584"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.content.btnAdaptiveStream.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("url", dashUrl)
                putExtra("is_dash", true)
            })
        }

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