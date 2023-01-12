package com.example.lr4pokemon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotifSender: Service() {
    lateinit var searchId: String
    val BASE_URL = "https://pokeapi.co/api/v2/"
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    var name = "Ditto"
    var id = 132
    var weight =40
    var height =3
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "Created")
    }
    fun getNotif() {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RestAPI::class.java)
        val call: Call<Pokemon?>? = service.getPokemon(searchId.toLong())
        call?.enqueue(object : Callback<Pokemon?> {
            override fun onResponse(call: Call<Pokemon?>, response: Response<Pokemon?>) {
                Log.d("Responded", response.body().toString())
                try {
                    id = response.body()!!.id
                    name = response.body()!!.name
                    height = response.body()!!.height
                    weight = response.body()!!.weight
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<Pokemon?>, t: Throwable) {
                Log.d("fail", t.message.toString());
            }
        })
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        searchId = intent!!.extras!!["id"].toString()
        Log.d("Service", "Started")
        val notificationId: Int = 101
        val channelId = "channel-id"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        mHandler = Handler()
        mRunnable = Runnable {
            getNotif()
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notif_pic)
                .setContentTitle("Имя покемона $name")
                .setContentText("ID покемона $id, height: $height weight: $weight")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build()) // посылаем уведомление
            }
            mHandler.postDelayed(mRunnable, 10000)
        }
        mHandler.postDelayed(mRunnable, 5000)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Service", "Destroyed")
        mHandler.removeCallbacks(mRunnable)
    }
}