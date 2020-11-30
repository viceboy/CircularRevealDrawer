package com.viceboy.circularReveal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.viceboy.circularrevealdrawer.interfaces.MenuItemClickListener
import com.viceboy.circularrevealdrawer.interfaces.OnProfileImageClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        crdMenu.setOnProfileImageClickListener(object : OnProfileImageClickListener {
            override fun onClick(view: View) {
                showToast("Clicked on profile image")
            }
        })

        crdMenu.setMenuItemClickListener(object : MenuItemClickListener {
            override fun getMenuListener(menus: Array<String>): HashMap<String, View.OnClickListener> {
                val mapOfClickListener = HashMap<String, View.OnClickListener>()
                menus.forEach {
                    val listener  = when (it) {
                        "Home" -> View.OnClickListener{
                            showToast("Clicked on Home")
                        }

                        "About Us" -> View.OnClickListener {
                            showToast("Clicked on About Us")
                        }

                        "Wallet" -> View.OnClickListener {
                            startActivity(Intent(this@MainActivity,Wallet::class.java))
                        }

                        "My Trips" -> View.OnClickListener {
                            showToast("Clicked on My Trips")
                        }

                        "Contributors" -> View.OnClickListener {
                            showToast("Clicked on Contributors")
                        }

                        else -> View.OnClickListener {
                            showToast("Click not configured")
                        }
                    }
                    mapOfClickListener[it] = listener
                }
                return mapOfClickListener
            }
        })
    }

    private fun showToast(text: String) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }
}