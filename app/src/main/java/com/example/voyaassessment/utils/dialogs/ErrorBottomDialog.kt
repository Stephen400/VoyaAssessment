package com.example.voyaassessment.utils.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voyaassessment.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorDialog(
    isVisible: Boolean,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { newValue ->
            true
        }
    )

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismissRequest()
            },
            sheetState = sheetState,
            modifier = Modifier.navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.warning),
                    contentDescription = "Warning",
                    tint = Color.Unspecified, // This disables tinting so the PNG displays in its original colors
                    modifier = Modifier
                        .height(80.dp)
                        .width(70.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = title,
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            // When Cancel is tapped, explicitly hide the sheet and then call onDismissRequest
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismissRequest()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text("Cancel", color = Color.Black)
                    }

                    // Button(
                    //     onClick = onConfirmDelete,
                    //     modifier = Modifier.weight(1f),
                    //     colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    //     contentPadding = PaddingValues(vertical = 12.dp)
                    // ) {
                    //     Text("Delete", color = Color.White)
                    // }
                }
            }
        }

        // Show the sheet when visible
        LaunchedEffect(key1 = isVisible) {
            try {
                sheetState.show()
            } catch (e: CancellationException) {
                // Ignore cancellation
            }
        }
    }
}

