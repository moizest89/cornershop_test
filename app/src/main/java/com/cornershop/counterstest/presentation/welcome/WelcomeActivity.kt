package com.cornershop.counterstest.presentation.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.usecases.user.FirstTimeUseCase
import com.cornershop.counterstest.domain.usecases.user.IsFirstTimeInAppUseCase
import com.cornershop.counterstest.presentation.counter.list.CounterListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    @Inject
    lateinit var firstTimeInAppUseCase: FirstTimeUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        this.findViewById<Button>(R.id.buttonStart).setOnClickListener { _ ->
            getStarted()
        }
    }

    private fun getStarted() {
        CoroutineScope(Dispatchers.IO).launch {
            firstTimeInAppUseCase.invoke()
            withContext(Dispatchers.Main) {
                goToCounterList()
            }
        }
    }

    private fun goToCounterList() {
        startActivity(Intent(this, CounterListActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

}
