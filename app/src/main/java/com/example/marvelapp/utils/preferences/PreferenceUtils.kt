package com.example.marvelapp.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class PreferenceUtils(private val context: Context) : IPreferenceUtils {

    companion object {
        private const val SECURE_PREF_NAME = "marvel_secure_shared_preferences"
    }

    private var secureSharedPreferences: SharedPreferences

    init {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        secureSharedPreferences = EncryptedSharedPreferences.create(
            SECURE_PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun putLong(key: String, value: Long) {
        val sp = context.getSharedPreferences(SECURE_PREF_NAME, Context.MODE_PRIVATE)
        with(sp.edit()) {
            this.putLong(key, value)
            this.apply()
        }
    }

    override fun getLong(key: String): Long {
        val sp = context.getSharedPreferences(SECURE_PREF_NAME, Context.MODE_PRIVATE)
        return sp.getLong(key, 0)
    }

    override fun remove(key: String) {
        val sp = context.getSharedPreferences(SECURE_PREF_NAME, Context.MODE_PRIVATE)
        with(sp.edit()) {
            this.remove(key)
            this.apply()
        }
    }
}