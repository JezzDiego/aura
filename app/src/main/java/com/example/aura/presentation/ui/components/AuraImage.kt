package com.example.aura.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * Imagem carregada via Coil. Aceita `android.resource://` (fotos do seed offline),
 * `file://` (fotos dos relatos) ou URLs remotas no futuro.
 */
@Composable
fun AuraImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context).data(model).crossfade(true).build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        error = { ImagePlaceholder() },
        loading = { ImagePlaceholder(showIcon = false) },
    )
}

@Composable
private fun ImagePlaceholder(showIcon: Boolean = true) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (showIcon) {
            Icon(
                Icons.Outlined.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
