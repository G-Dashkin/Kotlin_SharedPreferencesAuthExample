package com.example.kotlin_sharedpreferencesauthexample

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.kotlin_sharedpreferencesauthexample.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        prefManager = PrefManager(this) // Получаем ссылку на управление SharedPreferences
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Проверка залогинен ли пользователь, если залогинен то переносимся на активити с контентом
        checkLogin()

        // Если мы не залогинены, то либо логинемся, лоба переносимся на страницу регистрации
        binding.apply {
            loginButton.setOnClickListener { clickLogin() }
            signupButton.setOnClickListener { startActivity(Intent(this@LoginActivity, SignupActivity::class.java)) }
        }
    }

    private fun checkLogin(){
        if (prefManager.isLogin()!!) {
            startActivity(Intent(this, ContentActivity::class.java))
            finish()
        }
    }
    private fun clickLogin(){
        // Получаем значения из полей логин и пароль
        val username = binding.userLogin.text.toString().trim()
        val password = binding.userPassword.text.toString().trim()

        // Получаем ссылку на менеджер SharedPreferences для управления
        val pref = PrefManager(this).pref!!

        // Получаем список всех зарегистрированных пользователей
        val usersArray = pref.getStringSet("usersArray", mutableSetOf())

        // Получаем пароль залогиненного пользователя
        val validPassword = pref.getString(username, "")

        // Проверяем заполнены ли поля "логин" и "пароль"
        if (username.isEmpty() || username == "") {
            binding.userLogin.error = "Поле Username должно быть заполнено"
            binding.userLogin.requestFocus()
        } else if (password.isEmpty() || password == ""){
            binding.userPassword.error = "Поле Password должно быть заполнено"
            binding.userPassword.requestFocus()
        // Проверяем зарегистрирован ли логиниющийся пользователь
        } else if (!usersArray?.contains(username)!!) {
            binding.userLogin.error = "Такого пользователя не существует"
            binding.userLogin.requestFocus()
        // Проверяем верно ли указан пароль зарегистрированного пользователя
        } else if (password != validPassword) {
            binding.userPassword.error = "Пароль указан не верно"
            binding.userPassword.requestFocus()
        // Если все проверки пройдены то устанавливаем значения, что пользователь залогинен
        // и устанавливаем имя пользователя для работы в активити
        } else {
            prefManager.setLogin(true)
            prefManager.setUsername(username)
            startActivity(Intent(this, ContentActivity::class.java))
            finish()
        }
    }
}