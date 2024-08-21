package com.develop.ui.dialogs.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.develop.ui.R

@Composable
fun ErrorDialog(
    msg:String,
    title:String = stringResource(id = R.string.error_dialog_title),
    showDialog:Boolean = false,
    onDismiss:()->Unit = {},
){

    if (showDialog){
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties()
            ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = msg)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ErrorDialogPreview(){
    ErrorDialog(msg = "Some error happens", showDialog = true)
}