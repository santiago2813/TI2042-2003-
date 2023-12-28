package cl.inacap.apps.mqttexample

import MqttClientHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var messageView: TextView
    private lateinit var messageText: EditText
    private lateinit var sendButton: Button

    private lateinit var mqttClient: MqttClientHelper
    private val sensorTopic = MqttClientHelper.SENSOR_TOPIC
    private val deviceTopic = MqttClientHelper.DEVICE_TOPIC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageView = findViewById(R.id.messageView)
        messageText = findViewById(R.id.messageText)
        sendButton = findViewById(R.id.sendButton)

        mqttClient = MqttClientHelper(this)
        messageView.append("OK!\n")

        mqttClient.subscribeToTopic(sensorTopic, messageView)
        mqttClient.publishMessage(sensorTopic, "Sensor is Online!")

        mqttClient.subscribeToTopic(deviceTopic, messageView)
        mqttClient.publishMessage(deviceTopic, "Device is Online!")

        sendButton.setOnClickListener {
            messageView.append("Sending Message...\n")
            mqttClient.publishMessage(sensorTopic, messageText.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.disconnect()
    }

    fun onSendButtonClick(view: View) {
        val humidityStr = messageText.text.toString()
        val humidity = humidityStr.toIntOrNull() ?: 0
        mqttClient.publishMessage(MqttClientHelper.SENSOR_TOPIC, humidity.toString())
    }
}

