package com.example.lr4pokemon

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: RestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchId = findViewById<EditText>(R.id.editTextNumber)
        searchId.setFilters(arrayOf<InputFilter>(InputFilterMinMax("1", "898")))
        val showData = findViewById<Button>(R.id.showPokemon)
        showData.setOnClickListener {
            viewModel = ViewModelProviders.of(this).get(RestViewModel::class.java)
            showPokemon()
        }
        val id = findViewById<TextView>(R.id.idValue)
//        val intent = Intent(this, NotifSender::class.java)
//        val notifOn = findViewById<Button>(R.id.butTurnOnNotif)
//        val notifOff = findViewById<Button>(R.id.butTurnOffNotif)
//        notifOn.setOnClickListener {
//            if (!isServiceRunning(NotifSender::class.java)) {
//                intent.putExtra("id", searchId.text.toString())
//                startService(intent)
//
//                Toast.makeText(
//                    applicationContext,
//                    "The service is running",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        notifOff.setOnClickListener {
//            if (isServiceRunning(NotifSender::class.java)) {
//                stopService(intent)
//
//                Toast.makeText(
//                    applicationContext,
//                    "The service is not running",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
    }
    fun showPokemon(){
        val searchId = findViewById<EditText>(R.id.editTextNumber)
        val sendId:Int
        if (searchId.text.toString()==""){
            sendId = 1
        } else  {
            sendId = searchId.text.toString().toInt()
        }
        viewModel.getFirstTodo(sendId).observe(this, Observer {
            val id = findViewById<TextView>(R.id.idValue)
            val name = findViewById<TextView>(R.id.nameValue)
            val height = findViewById<TextView>(R.id.heightValue)
            val weight = findViewById<TextView>(R.id.weightValue)
            id.text = it.id.toString()
            name.text = it.name
            height.text = it.height.toString()
            weight.text = it.weight.toString()
        })
    }
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}