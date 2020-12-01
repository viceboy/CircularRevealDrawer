package com.viceboy.circularReveal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Wallet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Wallet"
        setContentView(R.layout.activity_wallet)
    }
}