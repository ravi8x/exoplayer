package info.androidhive.exoplayer

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
    private var isDash = false

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
        isDash = intent.extras?.getBoolean("is_dash") ?: false

        if (mediaUrl.isNullOrBlank()) {
            Toast.makeText(this, "Media url is null!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            exoPlayer.trackSelectionParameters =
                exoPlayer.trackSelectionParameters.buildUpon().setMaxVideoSizeSd().build()

            val mediaBuilder = MediaItem.Builder().setUri(mediaUrl)

            // set mime type to APPLICATION_MPD in case of DASH
            if (isDash) {
                mediaBuilder.setMimeType(MimeTypes.APPLICATION_MPD)
            }

            val mediaItem = mediaBuilder.build()

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