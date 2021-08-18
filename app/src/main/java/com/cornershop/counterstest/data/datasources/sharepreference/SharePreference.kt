package com.cornershop.counterstest.data.datasources.sharepreference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.cornershop.counterstest.data.models.db.User
import com.google.gson.Gson
import javax.inject.Inject

private const val USER = "user_preferences"
class SharePreference @Inject constructor(val context: Context){

    private var encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        "cornershop.counterstest",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private fun get(): SharedPreferences = encryptedPrefs

    fun getUserInformation(): User {
        this.get().getString(USER, "")?.let {
            return if (it.isNotBlank()) Gson().fromJson(it, User::class.java) else User(true)
        }
        return User(false)
    }

    fun setUserInformation(userInformation: User): User {
        this.get().edit()
            .putString(USER, Gson().toJson(userInformation))
            .apply()
        return userInformation
    }

}