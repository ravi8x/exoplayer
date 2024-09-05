package info.androidhive.exoplayer

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import info.androidhive.exoplayer.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlaylistBinding.inflate(layoutInflater)
    }

    private var player: Player? = null
    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L
    private val mediaItems: MutableList<MediaItem> = mutableListOf()

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initializePlayer() {
        // ExoPlayer implements the Player interface
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            // Update the track selection parameters to only pick standard definition tracks
            exoPlayer.trackSelectionParameters =
                exoPlayer.trackSelectionParameters.buildUpon().setMaxVideoSizeSd().build()

            mediaItems.clear()
            mediaItems.addAll(preparePlayList())

            exoPlayer.setMediaItems(mediaItems, mediaItemIndex, playbackPosition)
            exoPlayer.playWhenReady = playWhenReady
            exoPlayer.addListener(playbackStateListener)
            exoPlayer.prepare()
        }
    }

    private fun preparePlayList(): List<MediaItem> {
        return listOf(
            MediaItem.fromUri("https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fnightfall-future-bass-music-228100.mp3?alt=media&token=32821471-654b-4a9e-9790-1e9c7d1cc584"),
            MediaItem.fromUri("https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fnight-detective-226857.mp3?alt=media&token=4f6ade23-0aaf-4afc-acb9-645540f2fe87"),
            MediaItem.fromUri("https://firebasestorage.googleapis.com/v0/b/project-8525323942962534560.appspot.com/o/samples%2Fin-slow-motion-inspiring-ambient-lounge-219592.mp3?alt=media&token=8c4e73cb-97c6-4163-9cfe-0728dbecf076"),
        )
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