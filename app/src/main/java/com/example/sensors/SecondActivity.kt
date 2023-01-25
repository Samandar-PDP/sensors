package com.example.sensors

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView

class SecondActivity : AppCompatActivity(), SensorEventListener {
    // proximity sensor
    private lateinit var textView: TextView
    private var sensorManager: SensorManager? = null
    private var proximitySensor: Sensor? = null
    private var isProximitySensorAvailable = false
    private lateinit var nestedScrollView: NestedScrollView
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        textView = findViewById(R.id.textView)
        nestedScrollView = findViewById(R.id.constlayout)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            proximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            isProximitySensorAvailable = true
            textView.text = "Available"
        } else {
            textView.text = "Proximity sensor isn't available"
            isProximitySensorAvailable = false
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        textView!!.text = sensorEvent.values[0].toString() + " cm"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator!!.vibrate(
                VibrationEffect.createOneShot(
                    300,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator!!.vibrate(300)
        }
        nestedScrollView.setBackgroundColor(Color.GREEN)
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {
        nestedScrollView.setBackgroundColor(Color.WHITE)
    }

    override fun onResume() {
        super.onResume()
        if (isProximitySensorAvailable) {
            sensorManager!!.registerListener(
                this,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (isProximitySensorAvailable) {
            sensorManager!!.unregisterListener(this)
        }
    }
}