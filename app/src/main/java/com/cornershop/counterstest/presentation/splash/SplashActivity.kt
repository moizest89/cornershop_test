package com.cornershop.counterstest.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.usecases.user.IsFirstTimeInAppUseCase
import com.cornershop.counterstest.presentation.counter.list.CounterListActivity
import com.cornershop.counterstest.presentation.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var firstTimeInAppUseCase: IsFirstTimeInAppUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startTimer()
    }

    private fun startTimer() {
        Handler().postDelayed({
            checkUserInformation()
        }, 1000)
    }

    private fun checkUserInformation() {
        CoroutineScope(Dispatchers.IO).launch {
            val intent = if(firstTimeInAppUseCase.invoke()){
                Intent(this@SplashActivity, WelcomeActivity::class.java)
            }else{
                Intent(this@SplashActivity, CounterListActivity::class.java)
            }
            withContext(Dispatchers.Main) {
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        }
    }
}