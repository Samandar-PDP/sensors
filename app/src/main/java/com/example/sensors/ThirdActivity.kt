package com.example.sensors

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

class ThirdActivity : AppCompatActivity(), SensorEventListener {

    //ACCELEROMETER sensor
    private lateinit var textX: TextView
    private  lateinit var textY: TextView
    private  lateinit var textZ: TextView
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var isAccelerometer = false
    private  var isNotFirstTime: Boolean = false
    private var currX = 0f
    private  var currY:Float = 0f
    private  var currZ:Float = 0f
    private  var lastX:Float = 0f
    private  var lastY:Float = 0f
    private  var lastZ:Float = 0f
    private var xDiff = 0f
    private  var yDiff: Float = 0f
    private  var zDiff:Float = 0f
    private val shakeThreshold = 5f
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        textX = findViewById(R.id.textX)
        textY = findViewById<TextView>(R.id.textY)
        textZ = findViewById<TextView>(R.id.textZ)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            isAccelerometer = true
        } else {
            textX.setText("ACCELEROMETER is not available")
            isAccelerometer = false
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAccelerometer) {
            sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isAccelerometer) {
            sensorManager?.unregisterListener(this)
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        currX = sensorEvent.values[0]
        currY = sensorEvent.values[1]
        currZ = sensorEvent.values[2]
        textX!!.text = "$currX m/s2"
        textY.setText(currY.toString() + " m/s2")
        textZ.setText(currZ.toString() + " m/s2")
        if (isNotFirstTime) {
            xDiff = Math.abs(lastX - currX)
            yDiff = Math.abs(lastY - currY)
            zDiff = Math.abs(lastZ - currZ)
            if (xDiff > shakeThreshold && yDiff > shakeThreshold || xDiff > shakeThreshold && zDiff > shakeThreshold
                || yDiff > shakeThreshold && zDiff > shakeThreshold
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator!!.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator!!.vibrate(500)
                }
            }
        }
        lastX = currX
        lastY = currY
        lastZ = currZ
        isNotFirstTime = true
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
}