package com.example.kstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kstream.ui.theme.KStreamTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KStreamTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                    ScreenMain(modifier = modifier)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScreenCameraRequest(modifier: Modifier = Modifier) {

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        ScreenLaunchServer()
    } else {
        Column (
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown
                "The camera is required for KStream"
            } else {
                // If it's the first time the user lands on this feature
                "Camera permission is required to host"
            }
            Text(textToShow)
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}


@Composable
fun ScreenMain(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            Text(
                text = "KStream",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "Android camera streaming over the network, in Kotlin",
                modifier = Modifier
                    .padding(horizontal = LocalConfiguration.current.screenWidthDp.dp * 0.15f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
        var selected by remember { mutableIntStateOf(1) }
        Box(
            modifier = Modifier
                .weight(1f)
        )
        {
            if (selected == 0) {
                ScreenCameraRequest()
            }
            else
            {
                ScreenLaunchClient()
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        )
        {


            SegmentedToggle(
                options = listOf("Server", "Client"),
                selectedIndex = selected,
                onOptionSelected = { selected = it },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ScreenLaunchServer() {

}

@Composable
fun ScreenLaunchClient() {
    var btnText: String by remember { mutableStateOf("Connect") }
    var ipAddress: String by remember { mutableStateOf("192.168.245.43") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        TextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
        )
        Button(
            onClick = {

            },
        ) {
            Text(
                text = btnText,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}


@Composable
fun SegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(50))
            .border(1.dp, Color.Gray, RoundedCornerShape(50))
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onOptionSelected(index) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    KStreamTheme {
//        Greeting("Android")
//    }
//}