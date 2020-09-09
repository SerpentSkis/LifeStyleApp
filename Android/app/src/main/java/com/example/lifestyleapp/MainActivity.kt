package com.example.lifestyleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifestyleapp.activities.RegisterActivity
import com.example.lifestyleapp.activities.SummaryActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}