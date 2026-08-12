package com.currentdetection.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.currentdetection.ui.theme.PowerOff

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .pressClickEffect(onClick),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .pressClickEffect(onClick),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun DestructiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .pressClickEffect(onClick),
        colors = ButtonDefaults.buttonColors(containerColor = PowerOff),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}
