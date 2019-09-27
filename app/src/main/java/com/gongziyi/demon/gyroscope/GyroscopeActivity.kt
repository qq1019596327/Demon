package com.gongziyi.demon.gyroscope

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gongziyi.demon.R
import kotlinx.android.synthetic.main.activity_gyroscope.*


/**
 * Created on 2019/9/26
 * @author: gongziyi
 * Description:陀螺仪
 */
class GyroscopeActivity : AppCompatActivity(), SensorEventListener {


    private val MyManage: SensorManager? by lazy {
        //获得系统传感器服务管理权
        getSystemService(SENSOR_SERVICE) as? SensorManager?
    }

    private var mTimestamp = 0L
    private val NS2S = 1.0f / 1000000000.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyroscope)
    }

    private val angle by lazy {
        Array<Float>(3) { 0f }
    }

    override fun onStart() {
        super.onStart()
        MyManage?.apply {
            registerListener(
                this@GyroscopeActivity,
                getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onStop() {
        super.onStop()
        MyManage?.apply {
            unregisterListener(
                this@GyroscopeActivity
            )
        }
        angle[0] = 0f
        angle[1] = 0f
        angle[2] = 0f
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        event?.apply {

            if (timestamp != 0L) {
                // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                val dt = (timestamp - mTimestamp) * NS2S


                angle[0] += (values[0] * dt)
                angle[1] += (values[1] * dt)
                angle[2] += (values[2] * dt)

                text_x.text = "x=${String.format("%.2f", angle[0])}"
                text_y.text = "y=${String.format("%.2f", angle[1])}"
                text_z.text = "z=${String.format("%.2f", angle[2])}"

            }
            //将当前时间赋值给timestamp
            mTimestamp = timestamp

//
//            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) { //传感器发生改变获取加速度计传感器的数据
//                if (timestamp != 0) {
//                    // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
//                    final float dT = (event.timestamp - timestamp) * NS2S;
//                    // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
//                    positionXYZ.x = event.values[0] * dT;
//                    positionXYZ.y = event.values[1] * dT;
//                    positionXYZ.z = event.values[2] * dT;
//                    // 将弧度转化为角度
//                    float anglex =(float) Math . toDegrees (positionXYZ.x);
//                    float angley =(float) Math . toDegrees (positionXYZ.y);
//                    float anglez =(float) Math . toDegrees (positionXYZ.z);
//
//                    if (angley > 80) out.println(String.valueOf(angley));
//                    if (angley < -80) out.println(String.valueOf(angley));
//
//                    t0.setText(String.valueOf(anglex));
//                    t1.setText(String.valueOf(angley));
//                    t2.setText(String.valueOf(anglez));
//                    t3.setText(String.valueOf(dT));
//                }
//                timestamp = event.timestamp;
//            }
        }
    }
}