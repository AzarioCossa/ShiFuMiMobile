package com.example.shifumi_mobile.models

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeListener(private val onShake: () -> Unit) : SensorEventListener {
    private var lastShakeTime: Long = 0

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

            val currentTime = System.currentTimeMillis()
            if (acceleration > SHAKE_THRESHOLD && currentTime - lastShakeTime > SHAKE_TIME_LAPSE) {
                lastShakeTime = currentTime
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Evan, don't implement this method
    }

    companion object {
        private const val SHAKE_THRESHOLD = 3.0f  // Shake Sensitivity
        private const val SHAKE_TIME_LAPSE = 1300  // It avoids multiple rapid shakes
    }
}