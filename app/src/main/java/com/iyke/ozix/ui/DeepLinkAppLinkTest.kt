package com.iyke.ozix.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.iyke.ozix.common.OZI_APP_URI
import com.iyke.ozix.ui.chatScreen.CHAT_ID_KEY
//import kotlinx.serialization.Serializable

const val BASE_URI = "https://coastal-haven-309701.ew.r.appspot.com"

//@Serializable
//open class LinkTestRoute()
//
//@Serializable
//object LinkTestLandingRoute: LinkTestRoute()

fun NavGraphBuilder.deepLinkAppLinkTestGraph() {
    navigation(startDestination = "linkarenalanding", route = "linkarena") {
        composable(
            route = "linkarenalanding",
//            deepLinks = listOf(
//                navDeepLink<LinkTestLandingRoute>(basePath = BASE_URI)
//            ),
            deepLinks = listOf(navDeepLink { uriPattern = BASE_URI })
        ) {
            DeepLinkAppLinkTestScreen()
        }
    }
}

@Composable
fun DeepLinkAppLinkTestScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Deep Link Test")
    }
}
