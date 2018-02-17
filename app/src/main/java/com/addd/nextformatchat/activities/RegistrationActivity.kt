package com.addd.nextformatchat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.addd.nextformatchat.R
import com.addd.nextformatchat.model.UserReg
import com.addd.nextformatchat.network.NetworkAuth
import com.addd.nextformatchat.toast
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity(), NetworkAuth.RegistrationCallback {
    override fun resultReg(result: Boolean) {
        if (result) {
            setResult(200)
            finish()
        } else {
            toast(R.string.something_wrong)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setSupportActionBar(toolbarRegistration)
        title = getString(R.string.registration)
        toolbarRegistration.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbarRegistration.setNavigationOnClickListener { finish() }
        NetworkAuth.registerCallbackReg(this)
        button.setOnClickListener {
            goRegistration()
        }
    }

    private fun goRegistration() {
        if (editTextLogin.text.isEmpty() || editTextPassword.text.isEmpty() ||
                editTextFirstName.text.isEmpty() || editTextLastName.text.isEmpty()) {
            toast(getString(R.string.enter_all))
        } else {
            NetworkAuth.registration(UserReg(editTextLogin.text.toString(), editTextPassword.text.toString(),
                    editTextFirstName.text.toString(), editTextLastName.text.toString()))
        }
    }


    override fun onDestroy() {
        NetworkAuth.registerCallbackReg(null)
        super.onDestroy()
    }

}
