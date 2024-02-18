package com.example.kotlin_sharedpreferencesauthexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin_sharedpreferencesauthexample.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        prefManager = PrefManager(this) // Получаем ссылку на управление SharedPreferences
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // При клике запускаем функцию регистрации нового пользователя
        binding.apply {
            signupButton.setOnClickListener {clickSignup()}
        }
    }

    private fun clickSignup(){
        // Получаем значения из всех полей
        val username = binding.userNameField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        val passwordConfirm = binding.passwordConfirmField.text.toString().trim()

        // Получаем список всех зарегистрированных пользователей
        val usersArray = prefManager.pref?.getStringSet("usersArray", mutableSetOf())

        // Проверяем все ли поля заполнены
        if (username.isEmpty() || username == "") {
            binding.userNameField.error = "Поле Username должно быть заполнено"
            binding.userNameField.requestFocus()
        } else if (password.isEmpty() || password == ""){
            binding.passwordField.error = "Поле Password должно быть заполнено"
            binding.passwordField.requestFocus()
        } else if(passwordConfirm.isEmpty() || passwordConfirm == "") {
            binding.passwordConfirmField.error = "Подтвердите пароль"
            binding.passwordConfirmField.requestFocus()
        // Проверяем совпадает ли пароль с проверяемым паролем
        } else if(password != passwordConfirm) {
            binding.passwordConfirmField.error = "Пароли не совпадают"
            binding.passwordConfirmField.requestFocus()
        // Проверяем зарегистрирован ли пользователь
        } else if(usersArray?.contains(username)!!) {
            binding.userNameField.error = "Данный пользователь уже зарегистрирован"
            binding.userNameField.requestFocus()
        // Если все поля заполнены, то регистрируем нового пользователя
        } else {
            prefManager.signupUser(username, password) // Добавляем пользователя в список и прикрепляем к нему пароль
            prefManager.setLogin(true) // Устанавливаем занчение, что пользователь залогинен
            prefManager.setUsername(username) // Устанавливаем занчение имени пользователя для активити
            startActivity(Intent(this, ContentActivity::class.java))
            finish()
        }
    }
}