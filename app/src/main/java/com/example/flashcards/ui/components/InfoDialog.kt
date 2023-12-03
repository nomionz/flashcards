package com.example.flashcards.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.example.flashcards.R

// InfoDialog is a composable that displays a dialog with a message.
@Composable
fun InfoDialog(
    onDismiss: () -> Unit,
    message: String
) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.3f) // dim background

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = message,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.action_confirm))
                }
            }
        }
    }
}