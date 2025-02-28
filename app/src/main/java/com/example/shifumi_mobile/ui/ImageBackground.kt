package com.example.shifumi_mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import com.example.shifumi_mobile.R

@Composable
fun ImageBackground(alphaValue: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alphaValue)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
