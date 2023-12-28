import android.widget.TextView
import cl.inacap.apps.mqttexample.MainActivity
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttClientHelper(mainActivity: MainActivity) {
    private val SERVER_URI = "tcp://mqtt.eclipseprojects.io:1883"
    private val CLIENT_ID = "santiago2813_mqtt"

    companion object {
        const val SENSOR_TOPIC = "sensorTopic"  // Tópico para el sensor
        const val DEVICE_TOPIC = "deviceTopic"  // Tópico para el dispositivo
    }

    private lateinit var mqttClient: MqttClient

    init {
        connectToMqttBroker()
    }

    private fun connectToMqttBroker() {
        try {
            val persistence = MemoryPersistence()
            mqttClient = MqttClient(SERVER_URI, CLIENT_ID, persistence)
            val options = MqttConnectOptions()
            options.isCleanSession = true
            mqttClient.connect(options)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribeToTopic(topic: String, messageView: TextView) {
        try {
            mqttClient.subscribe(topic) { _, message ->
                val payload = String(message.payload)
                messageView.append("[$topic] $payload\n")
                println("[$topic] Mensaje recibido: $payload")

                // Parsear el mensaje como entero
                val humidity = payload.toIntOrNull() ?: return@subscribe

                // Actualizar la interfaz de usuario según el nivel de humedad
                updateUI(humidity)
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publishMessage(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttClient.publish(topic, mqttMessage)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun updateUI(humidity: Int) {
        if (humidity in 30..60) {

            publishMessage(DEVICE_TOPIC, "green")
        } else if (humidity in 15..29 || humidity in 61..75) {
            publishMessage(DEVICE_TOPIC, "yellow")
        } else {
            publishMessage(DEVICE_TOPIC, "red")
        }
    }

    fun disconnect() {
        mqttClient.disconnect()
    }
}
