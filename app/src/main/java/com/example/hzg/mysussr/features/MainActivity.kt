package com.example.hzg.mysussr.features

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.features.uid.UidActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_save).setOnClickListener({ v ->
            startActivity(Intent(this, UidActivity::class.java))
        })
    }
}