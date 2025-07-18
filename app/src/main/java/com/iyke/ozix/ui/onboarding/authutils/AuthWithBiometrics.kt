package com.iyke.ozix.ui.onboarding.authutils

import android.app.Activity
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun signInWithBiometrics(context: Context) {
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context.findFragmentActivity() as FragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    context,
                    "Authentication error: $errString", Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    context,
                    "Authentication succeeded!", Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    context, "Authentication failed",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    // Prompt appears when user clicks "Log in".
    // Consider integrating with the keystore to unlock cryptographic operations,
    // if needed by your app.
//    val biometricLoginButton =
//        findViewById<Button>(R.id.biometric_login)
//    biometricLoginButton.setOnClickListener {
//        biometricPrompt.authenticate(promptInfo)
//    }
//
    biometricPrompt.authenticate(promptInfo)
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

// Helper extension function to find the FragmentActivity
fun Context.findFragmentActivity(): FragmentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is FragmentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}