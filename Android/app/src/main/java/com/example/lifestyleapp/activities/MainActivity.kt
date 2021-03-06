package com.example.lifestyleapp.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lifestyleapp.R
import com.example.lifestyleapp.common.*
import com.example.lifestyleapp.fragments.*
import com.example.lifestyleapp.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_menu.*

class MainActivity : AppCompatActivity(), MenuFragment.DataParser, SettingFragment.SettingData,
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val localData = LocalData(this)
        if (!localData.getRegister()) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        menu_bt?.setOnClickListener(this)
        if (isTablet()) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            val usr = localData.getUser() ?: fakeUser2
            fragmentTransaction.replace(
                R.id.frame_master,
                MenuFragment.newInstance(),
                "menu_frag"
            )
            fragmentTransaction.commit()
        }
        dataHandler(currentSignals)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.menu_bt -> {
//                val localData = LocalData(this)
//                val usr = localData.getUser() ?: fakeUser2
                val menuFragment = MenuFragment.newInstance()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_detail, menuFragment, "menu_frag")
                fragmentTransaction.commit()
            }
        }
    }

    private fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    override fun dataHandler(command: Signals) {
        when (command) {
            Signals.SUMMARY -> {
                currentSignals = Signals.SUMMARY
//                val localData = LocalData(this)
//                val usr = localData.getUser() ?: fakeUser2
//                val userModel = UserModel(usr)
//                val calculateData = CalculateData(
//                    userModel.calculateBMI(),
//                    userModel.calculateBMR(),
//                    userModel.calculateDailyCaloriesNeededForGoal()
//                )
                val summaryFragment = SummaryFragment.newInstance()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_detail, summaryFragment, "summary_frag")
                fragmentTransaction.commit()
            }
            Signals.LOGOUT -> {
                currentSignals = Signals.SUMMARY
                val localData = LocalData(this)
                localData.unregister()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            Signals.WEATHER -> {
                currentSignals = Signals.WEATHER
                val localData = LocalData(this)
                val usr = localData.getUser() ?: fakeUser2
                val weatherFragment = WeatherFragment.newInstance(usr)
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_detail, weatherFragment, "weather_frag")
                fragmentTransaction.commit()
            }
            Signals.HIKE -> {
                currentSignals = Signals.SUMMARY
//                val localData = LocalData(this)
//                val usr = localData.getUser() ?: fakeUser2
                val userViewModel = application?.let { UserViewModel(it) }
                var city = userViewModel?.allUsers?.value?.get(0)?.city ?: "me"
                userViewModel?.allUsers?.observe(this, Observer {
                    if (it.isNotEmpty()) {
                        val usr = it[0]
                        if (usr.city != null && usr.city != "null") {
                            city = usr.city!!
                        }
                    }
                })
                val gmmIntentUri = Uri.parse("geo:0,0?q=trails near $city")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            Signals.SETTING -> {
                currentSignals = Signals.SETTING
//                val localData = LocalData(this)
//                val usr = localData.getUser() ?: fakeUser2
                val settingFragment = SettingFragment.newInstance()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_detail, settingFragment, "summary_frag")
                fragmentTransaction.commit()
            }
            Signals.STEP -> {
                currentSignals = Signals.STEP
                val stepCounterFragment = StepCounterFragment.newInstance()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_detail, stepCounterFragment, "step_frag")
                fragmentTransaction.commit()
            }
        }
    }

//    override fun settingDataHandler(
//        height: String,
//        weight: String,
//        goal: String,
//        activityLevel: String
//    ) {
//        val localData = LocalData(this)
//        val usr = localData.getUser() ?: fakeUser2
//        if (height != "") usr.heightInches = height.toInt()
//        if (weight != "") usr.weightLbs = weight.toInt()
//        if (goal != "") usr.weightChangeGoalPerWeek = goal.toFloat()
//        if (activityLevel != "") usr.activityLevel = activityLevel
//        val userModel = UserModel(usr)
//        val calculateData = CalculateData(
//            userModel.calculateBMI(),
//            userModel.calculateBMR(),
//            userModel.calculateDailyCaloriesNeededForGoal()
//        )
//        localData.saveUser(usr)
//        val summaryFragment = SummaryFragment.newInstance(usr, calculateData)
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_detail, summaryFragment, "summary_frag")
//        fragmentTransaction.commit()
//    }


    override fun settingDataHandler() {
        val summaryFragment = SummaryFragment.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_detail, summaryFragment, "summary_frag")
        fragmentTransaction.commit()
    }
}
