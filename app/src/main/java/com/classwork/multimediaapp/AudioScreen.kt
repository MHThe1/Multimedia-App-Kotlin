package com.classwork.multimediaapp

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@UnstableApi
@Composable
fun AudioScreen() {
    val context = LocalContext.current

    // Create and manage ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Access the local audio from res/raw
            val audioUri = Uri.parse("android.resource://${context.packageName}/raw/theme_song")
            val mediaItem = androidx.media3.common.MediaItem.fromUri(audioUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false // Let the user start playback
        }
    }

    // Dispose of the ExoPlayer instance when no longer needed
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Display the PlayerView using AndroidView
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true // Show playback controls
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
