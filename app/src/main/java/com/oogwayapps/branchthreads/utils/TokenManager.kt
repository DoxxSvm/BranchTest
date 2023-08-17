package com.oogwayapps.branchthreads.utils

import android.content.Context
import com.oogwayapps.branchthreads.utils.Constants.PREFS_TOKEN_FILE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

class TokenManager @Inject constructor(@ApplicationContext var context: Context) {

    companion object {
        private const val AUTH_TOKEN_ALIAS = "auth_token"
    }

    fun storeAuthToken(authToken: String) {
        try {

            val sharedPreferences = getSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(AUTH_TOKEN_ALIAS, authToken)
            editor.apply()

        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getAuthToken(): String? {
        return try {
            val sharedPreferences = getSharedPreferences(context)
            sharedPreferences.getString(AUTH_TOKEN_ALIAS, null)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_TOKEN_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
