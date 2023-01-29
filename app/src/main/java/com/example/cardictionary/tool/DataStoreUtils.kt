package com.example.cardictionary.tool

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.preferenceData: DataStore<Preferences> by preferencesDataStore("carDictionary_dataStore")

private val topScoreOfFirstKey = stringPreferencesKey("topScoreOfFirst")
private val topScoreOfSecondKey = stringPreferencesKey("topScoreOfSecond")
private val usernameKey = stringPreferencesKey("username")
private val passwordKey = stringPreferencesKey("password")
private val avatarKey = stringPreferencesKey("avatar")
private val themeIdKey = intPreferencesKey("themeId")

fun saveTopScoreRecord1(context: Context, score: String) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        context.preferenceData.edit {
            it[topScoreOfFirstKey] = score
        }
    }
}

fun saveTopScoreRecord2(context: Context, score: String) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        context.preferenceData.edit {
            it[topScoreOfSecondKey] = score
        }
    }
}

fun loadTopScoreRecord1(context: Context, callback: (String) -> Unit = {}) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        val score = context.preferenceData.data.map {
            it[topScoreOfFirstKey] ?: "无"
        }
        withContext(Dispatchers.Main) {
            callback(score.first())
        }
    }
}

fun loadTopScoreRecord2(context: Context, callback: (String) -> Unit = {}) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        val score = context.preferenceData.data.map {
            it[topScoreOfSecondKey] ?: "无"
        }
        withContext(Dispatchers.Main) {
            callback(score.first())
        }
    }
}

fun saveUserIntoLocal(context: Context, user: User) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        context.preferenceData.edit {
            it[usernameKey] = user.username
            it[passwordKey] = user.password
            it[avatarKey] = user.userAvatar
        }
    }
}

fun loadUserFromLocal(context: Context, loadSuccess: (User) -> Unit = {}) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        val username = loadUsernameFromLocal(context)
        val a = async(Dispatchers.IO) {
            loadUserPasswordFromLocal(context)
        }
        val b = async(Dispatchers.IO) {
            loadUserAvatarFromLocal(context)
        }
        val password = a.await()
        val avatar = b.await()
        if (username != null && password != null && avatar != null) {
            withContext(Dispatchers.Main) {
                loadSuccess(User(username, password, avatar))
            }
        }
    }
}

fun saveThemeId(context: Context, id: Int) {
    CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
        context.preferenceData.edit {
            it[themeIdKey] = id
        }
    }
}

suspend fun loadThemeId(context: Context): Int? {
    val themeId = context.preferenceData.data.first {
        true
    }
    return themeId[themeIdKey]
}

private suspend fun loadUsernameFromLocal(context: Context): String? {
    val username = context.preferenceData.data.first {
        true
    }
    return username[usernameKey]
}

private suspend fun loadUserPasswordFromLocal(context: Context): String? {
    val password = context.preferenceData.data.first {
        true
    }
    return password[passwordKey]
}

private suspend fun loadUserAvatarFromLocal(context: Context): String? {
    val avatar = context.preferenceData.data.first {
        true
    }
    return avatar[avatarKey]
}

class User(val username: String, val password: String, val userAvatar: String)
class UserWithNoPsd(val username: String, val userAvatar: String)