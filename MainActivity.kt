package com.zentrarpg.launcher

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("launcher", Context.MODE_PRIVATE)
        val nick = findViewById<EditText>(R.id.nick)
        val play = findViewById<Button>(R.id.play)
        val status = findViewById<TextView>(R.id.status)

        nick.setText(prefs.getString("nick", ""))

        play.setOnClickListener {
            val name = nick.text.toString().trim()
            if (!Regex("^[A-Za-z0-9_]{3,20}$").matches(name)) {
                status.text = "Nickname must be 3-20 chars (letters, numbers, _)"
                return@setOnClickListener
            }
            prefs.edit().putString("nick", name).apply()

            // کپی نیک‌نیم (برای وقتی که کلاینت خودش از لانچر نیک نمی‌گیره)
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("nickname", name))

            if (packageManager.getLaunchIntentForPackage(BuildConfig.CLIENT_PACKAGE) == null) {
                status.text = "Game client is not installed (${BuildConfig.CLIENT_PACKAGE})"
                return@setOnClickListener
            }
            val ip = BuildConfig.SERVER_IP
            val port = BuildConfig.SERVER_PORT
            // 1) مستقیم با لینک samp:// به همون کلاینت (اگه پشتیبانی کنه، می‌ره سرور)
            try {
                val view = Intent(Intent.ACTION_VIEW, Uri.parse("samp://$ip:$port"))
                view.setPackage(BuildConfig.CLIENT_PACKAGE)
                view.putExtra("nickname", name)
                view.putExtra("name", name)
                view.putExtra("ip", ip)
                view.putExtra("port", port.toInt())
                startActivity(view)
            } catch (e: Exception) {
                // 2) اگه لینک رو قبول نکرد، فقط خود اپ رو باز کن
                val intent = packageManager.getLaunchIntentForPackage(BuildConfig.CLIENT_PACKAGE)!!
                intent.putExtra("nickname", name)
                intent.putExtra("ip", ip)
                intent.putExtra("port", port.toInt())
                startActivity(intent)
            }
        }
    }
}
