package com.example.kotlin_sharedpreferencesauthexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin_sharedpreferencesauthexample.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContentBinding.inflate(layoutInflater)
        prefManager = PrefManager(this) // Получаем ссылку на управление SharedPreferences
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkLogin() // Проверяем был ли залогинен пользователь
        setupUI() // Устанавливаем занчение залогиненного пользователя и списка пользователей на активити

        binding.logout.setOnClickListener { clickLogout() } // разлогиневает текущего пользователя
        binding.cleanData.setOnClickListener { cleanData() } // очищаем список пользователей
    }

    private fun checkLogin() {
        if (prefManager.isLogin() == false) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun setupUI() {
        val username = prefManager.getUsername().toString()
        binding.mainTextView.text = "Hello $username"

        // Получаем список зарегистрированных пользователей и выводим его в цикле
        val usersList = prefManager.getUsersArray()
        for (i in usersList){
            binding.accountsList.text = binding.accountsList.text.toString() + i + "\n"
        }
    }

    private fun clickLogout() {
        prefManager.setLogin(false)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun cleanData() {
        prefManager.removeData()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}