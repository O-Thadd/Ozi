package com.othadd.ozi.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.othadd.ozi.R
import com.othadd.ozi.ui.theme.OziComposeTheme

@Composable
fun DevScreen(
    setUiReady: () -> Unit,
    onBackClicked: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        setUiReady()
    }

    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dev_avi),
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = devIntro,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        openWebPage(
                            "https://x.com/ThaddeusOjike",
                            context
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    SelectionContainer {
                        Text(text = "@thaddeusojike")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        openWebPage(
                            "https://github.com/O-Thadd",
                            context
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_github),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    SelectionContainer {
                        Text(text = "O-Thadd")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mail),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    SelectionContainer {
                        Text(text = "thaddeusojike@gmail.com")
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { onBackClicked() }
                    .align(Alignment.TopStart)
            )
        }
    }
}

@Preview(widthDp = 412, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PrevDevScreen() {
    OziComposeTheme {
        DevScreen(
            setUiReady = {  },
            onBackClicked = {  }
        )
    }
}

fun openWebPage(url: String, context: Context) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

const val devIntro =
    "Android developer with 3+ years experience in creating and maintaining mobile applications for various clients.\nStrong knowledge of Java, Kotlin, Android SDK, and Firebase.\nOpen to working on challenging and interesting projects.\nOf course!, eager to learn new technologies and frameworks to enhance my android development capabilities."