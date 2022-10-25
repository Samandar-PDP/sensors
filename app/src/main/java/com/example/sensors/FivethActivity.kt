package com.example.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FivethActivity : AppCompatActivity(), SensorEventListener {
    // step counter

    private lateinit var textStepCounter: TextView
    private lateinit var textStepDetector:TextView
    private var sensorManager: SensorManager? = null
    private var sensorCounter: Sensor? = null
    private  var sensorDetector:Sensor? = null
    private var isStepAvailable = false
    private  var isDetectorAvailable:Boolean = false
    private var counterStep = 0
    private  var counterDetect:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fiveth)
        textStepCounter = findViewById(R.id.stepCounter)
        textStepDetector = findViewById(R.id.stepDetector)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            isStepAvailable = true
            sensorCounter = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        } else {
            isStepAvailable = false
            textStepCounter.text = "Not available"
        }
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            sensorDetector = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            isDetectorAvailable = true
        } else {
            isDetectorAvailable = false
            textStepDetector.text = "Not available"
        }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor == sensorCounter) {
            counterStep = sensorEvent.values[0].toInt()
            textStepCounter.text = counterStep.toString()
        }
        if (sensorEvent.sensor == sensorDetector) {
            counterDetect = (counterDetect + sensorEvent.values[0]).toInt()
            textStepDetector.text = counterDetect.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
        Toast.makeText(
            this, """
     ${sensor.name}
     $i
     """.trimIndent(), Toast.LENGTH_SHORT
        ).show()
    }

    override fun onResume() {
        super.onResume()
        if (isStepAvailable) {
            sensorManager?.registerListener(this, sensorCounter, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (isDetectorAvailable) {
            sensorManager?.registerListener(this, sensorDetector, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isStepAvailable) {
            sensorManager?.unregisterListener(this)
        }
        if (isDetectorAvailable) {
            sensorManager?.unregisterListener(this)
        }
    }
}