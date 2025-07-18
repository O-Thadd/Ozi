package com.iyke.ozix.ui.onboarding.authutils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

suspend fun instantiateGoogleSignInRequest(context: Context ) {
    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder("284413699714-pfp30lacj9968f9oo4djb1fr5ckk0bb0.apps.googleusercontent.com")
//        .setFilterByAuthorizedAccounts(true)
//        .setServerClientId("284413699714-pfp30lacj9968f9oo4djb1fr5ckk0bb0.apps.googleusercontent.com")
//        .setAutoSelectEnabled(true)
//        .setNonce(<nonce string to use when generating a Google ID token>)
        .build()


    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()

    val credentialManager = CredentialManager.create(context)

    try {
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )
        handleSignIn(result)
    } catch (e: GetCredentialException) {
//        handleFailure(e)
        throw e
    }
}

private fun handleSignIn(result: GetCredentialResponse) {
    // Handle the successfully returned credential.
    when (val credential = result.credential) {

        // Passkey credential
        is PublicKeyCredential -> {
            // Share responseJson such as a GetCredentialResponse on your server to
            // validate and authenticate
            val responseJson = credential.authenticationResponseJson
        }

        // Password credential
        is PasswordCredential -> {
            // Send ID and password to your server to validate and authenticate.
            val username = credential.id
            val password = credential.password
        }

        // GoogleIdToken credential
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    // Use googleIdTokenCredential and extract the ID to validate and
                    // authenticate on your server.
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    val token = googleIdTokenCredential.idToken // this is what server needs.
                    Log.e("zzz", "gotten token from signin with google: $token")

                    //the following happens on the server.
//                    // You can use the members of googleIdTokenCredential directly for UX
//                    // purposes, but don't use them to store or control access to user
//                    // data. For that you first need to validate the token:
//                    // pass googleIdTokenCredential.getIdToken() to the backend server.
//                    GoogleIdTokenVerifier verifier = ... // see validation instructions
//                    GoogleIdToken idToken = verifier.verify(idTokenString);
//                    // To get a stable account identifier (e.g. for storing user data),
//                    // use the subject ID:
//                    idToken.getPayload().getSubject()
                } catch (e: GoogleIdTokenParsingException) {
//                    Log.e(TAG, "Received an invalid google id token response", e)
                }
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e("TAG", "Unexpected type of credential")
            }
        }

        else -> {
            // Catch any unrecognized credential type here.
            Log.e("TAG", "Unexpected type of credential")
        }
    }
}