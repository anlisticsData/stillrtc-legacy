package br.com.stilldistribuidora.stillrtc.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import br.com.stilldistribuidora.partners.views.LoginAppActivity
import br.com.stilldistribuidora.partners.views.PrivacyPolicyActivity

import br.com.stilldistribuidora.stillrtc.R

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/11/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            //val i = Intent(baseContext, LoginActivity::class.java)

            val i = Intent(baseContext, LoginAppActivity::class.java)

            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    companion object {
        private val SPLASH_TIME_OUT = 2000
    }
}
