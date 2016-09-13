package org.pentaho.kettle.plugin.mqtt.steps.consumer;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.pentaho.di.core.row.RowDataUtil;

import java.util.Date;


/**
 * Created by dams on 23-08-2016.
 */
public class MqttClientSubscriber implements MqttCallback {

    private MqttClient client;
    private ConsumerStepMeta meta;
    private ConsumerStepData data;
    private ConsumerStep step;

    String topic;
    String broker;
    String clientId;
    int qos = 1;
    boolean cleanSession = true;
    Object[] r;


    public MqttClientSubscriber(ConsumerStepMeta smi, ConsumerStepData sdi, ConsumerStep step, Object[] r) {
        this.meta = smi;
        this.data = sdi;
        this.step = step;
        this.r = r;
        topic = smi.getSubTopic().toString();
        broker = smi.getSubBroker().toString();
        clientId = smi.getClientId();
        qos = smi.getQos();
    }

    public void start() {
        try {
            String tmpDir = System.getProperty("java.io.tmpdir");
            MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
            MqttConnectOptions conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(cleanSession);
            client = new MqttClient(broker, clientId);
            client.connect(conOpt);
            client.setCallback(this);
            client.subscribe(topic, qos);
            step.logBasic("Connected ...");

        } catch (MqttException me) {
            step.logError(me.getMessage());
            step.logError(me.getCause().toString());
            me.printStackTrace();
        }
    }

    public void stop() {
        try {
            client.unsubscribe(topic);
            client.disconnect();
            client.close();
        } catch (MqttException me) {
            step.logBasic("reason " + me.getReasonCode());
            step.logBasic("msg " + me.getMessage());
            step.logBasic("loc " + me.getLocalizedMessage());
            step.logBasic("cause " + me.getCause());
            step.logBasic("excep " + me);
            me.printStackTrace();
        }
    }

    public void connectionLost(Throwable cause) {

    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String time = new Date().toString();
        String messageStr = new String(message.getPayload());
        String messageQos = new String("" + message.getQos());

        try {
            Object[] outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, messageStr);
            step.putRow(data.outputRowMeta, outputRow);
            outputRow = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        time = null;
        messageStr = null;
        messageQos = null;
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }
}
