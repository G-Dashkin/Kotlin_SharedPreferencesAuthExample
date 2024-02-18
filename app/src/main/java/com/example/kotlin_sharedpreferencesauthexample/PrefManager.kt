package com.example.kotlin_sharedpreferencesauthexample

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PrefManager(context: Context?) {

    // Shared pref mode
    val PRIVATE_MODE = 0

    // SharedPref FileName
    private val PREF_FILE_NAME = "SharedPreferencesAuth" // Название файла с пользователями и паролями

    val pref: SharedPreferences? = context?.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor? = pref?.edit()

    // Функция для установки значения, что пользователь залогинен
    fun setLogin(isLogin: Boolean) {
        editor?.putBoolean("is_login", isLogin)
        editor?.commit()
    }

    // Функция для установки значения имени пользователя после логинга, для отображения на активити
    fun setUsername(username: String?){
        editor?.putString("user_name", username)
        editor?.commit()
    }

    // Функция для проверки, залогинен ли пользователь
    fun isLogin(): Boolean? {
        return pref?.getBoolean("is_login", false)
    }

    // Функция для получения имени залогиненного пользователя
    fun getUsername(): String? {
        return pref?.getString("user_name", "")
    }

    // Функция для удаления все зарегистрированных пользователей
    fun removeData() {
        editor?.clear()
        editor?.commit()
    }

    // Функция для получения всех зарегистрированных пользователей
    fun getUsersArray():Set<String>{
        val listOne = pref?.getStringSet("usersArray", mutableSetOf())
        for (i in listOne!!){
        }
        return listOne
    }

    // Функция для регистрации ногово пользователя (перезаписи старых пользователей с добавлением нового)
    fun signupUser(string: String, password: String){
        val usersArray = pref?.getStringSet("usersArray", mutableSetOf())
        usersArray!!.add(string)
        val usersData = mutableMapOf<String, String>()
        for (i in usersArray) {
            val userPassword = pref?.getString(i, "")
            usersData[i] = userPassword!!
        }
        usersData[string] = password
        editor?.clear()
        for (i in usersData){
            editor?.putString(i.key, i.value)
        }
        editor?.putStringSet("usersArray", usersArray)
        editor?.commit()
    }
}