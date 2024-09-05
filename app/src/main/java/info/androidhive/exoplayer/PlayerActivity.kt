package info.androidhive.exoplayer

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import info.androidhive.exoplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private var player: Player? = null
    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L
    private var mediaUrl: String? = null

    private val playbackStateListener: Player.Listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {}
                ExoPlayer.STATE_BUFFERING -> {}
                ExoPlayer.STATE_READY -> {}
                ExoPlayer.STATE_ENDED -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        mediaUrl = intent.extras?.getString("url")

        if (mediaUrl.isNullOrBlank()) {
            Toast.makeText(this, "Media url is null!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializePlayer() {
        // ExoPlayer implements the Player interface
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            // Update the track selection parameters to only pick standard definition tracks
            exoPlayer.trackSelectionParameters =
                exoPlayer.trackSelectionParameters.buildUpon().setMaxVideoSizeSd().build()

            //val mediaItem = MediaItem.fromUri(Uri.parse(mediaUrl))
            //val mediaItem = MediaItem.Builder().setUri("https://www.youtube.com/api/manifest/dash/id/bf5bb2419360daf1/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=51AF5F39AB0CEC3E5497CD9C900EBFEAECCCB5C7.8506521BFC350652163895D4C26DEE124209AA9E&key=ik0")
              //  .setMimeType(MimeTypes.APPLICATION_MPD).build()
            val mediaItem = MediaItem.fromUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fnightfall-future-bass-music-228100.mp3?alt=media&token=32821471-654b-4a9e-9790-1e9c7d1cc584"))
            exoPlayer.setMediaItems(listOf(mediaItem), mediaItemIndex, playbackPosition)
            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.addListener(playbackStateListener)
            exoPlayer.prepare()
        }
    }

    private fun releasePlayer() {
        player?.let { player ->
            playbackPosition = player.currentPosition
            mediaItemIndex = player.currentMediaItemIndex
            playWhenReady = player.playWhenReady
            player.removeListener(playbackStateListener)
            player.release()
        }
        player = null
    }

    public override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        if (player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    public override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}