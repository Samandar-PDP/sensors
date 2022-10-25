package com.example.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    // all sensors
    private lateinit var sensorList: MutableList<Sensor>
    private lateinit var textView: TextView
    private lateinit var sensorManager: SensorManager
    private var stringBuilder = StringBuilder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            stringBuilder.append(sensor.name).append("\n")
        }

        val isSensorAvailable = if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) "Not Available" else "Available"
        textView.text = isSensorAvailable
    }
}