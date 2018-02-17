package com.addd.nextformatchat.activities

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.addd.nextformatchat.APP_TOKEN
import com.addd.nextformatchat.R
import com.addd.nextformatchat.model.AuthorizationRequest
import com.addd.nextformatchat.network.NetworkAuth
import com.addd.nextformatchat.toast
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity(), NetworkAuth.MyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        NetworkAuth.registerCallback(this)

        val mSettings: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (mSettings.contains(APP_TOKEN)) {
            startActivity(Intent(applicationContext, ChatsActivity::class.java))
            finish()
        }
        title = getString(R.string.auth)
        buttonLogin.setOnClickListener { goLogin() }
        textViewRegistration.setOnClickListener { goRegistration() }
    }

    private fun goLogin() {
        NetworkAuth.authorization(editTextLogin.text.toString(), editTextPassword.text.toString())
    }

    private fun goRegistration() {
        val intent = Intent(applicationContext, RegistrationActivity::class.java)
        startActivityForResult(intent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 200) {
            finish()
            startActivity(Intent(applicationContext, ChatsActivity::class.java))
        }
    }
    override fun resultAuth(result: Int) {
        when (result) {
            200 -> {
                startActivity(Intent(applicationContext, ChatsActivity::class.java))
                finish()
            }
            400 -> toast(R.string.wrong_login_password)
            else -> toast(R.string.something_wrong)
        }
    }

    override fun onDestroy() {
        NetworkAuth.registerCallback(null)
        super.onDestroy()
    }
}
