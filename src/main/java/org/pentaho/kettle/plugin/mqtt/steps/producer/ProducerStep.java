package org.pentaho.kettle.plugin.mqtt.steps.producer;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.FieldHelper;

/**
 * Created by dams on 02-08-2016.
 */
public class ProducerStep extends BaseStep implements StepInterface {

    private static final Class<?> PKG = ProducerStep.class;

    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient mqttClientProducer;
    private MqttConnectOptions connOpts;
    private MqttMessage msg;
    private String message;

    public ProducerStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
        super(s, stepDataInterface, c, t, dis);
    }

    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        ProducerStepMeta meta = (ProducerStepMeta) smi;
        ProducerStepData data = (ProducerStepData) sdi;
        return super.init(meta, data);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {

        // safely cast the step settings (meta) and runtime info (data) to specific implementations
        ProducerStepMeta meta = (ProducerStepMeta) smi;
        ProducerStepData data = (ProducerStepData) sdi;

        // get incoming row, getRow() potentially blocks waiting for more rows, returns null if no more rows expected
        Object[] r = getRow();
        String topic = meta.getPubTopic();
        String clientId = meta.getPubClientId();
        String broker = meta.getPubBroker();
        int outQos = meta.getPubQos();


        // if no more rows are expected, indicate step is finished and processRow() should not be called again
        if (r == null) {
            setOutputDone();
            try {
                mqttClientProducer.disconnect();
                logBasic("Disconnected to: " + broker + " & Topic: " + topic);
            } catch (MqttException me) {
                logErrorMsg(me.getReasonCode(), me.getMessage(), me.getLocalizedMessage(), me.getCause(), me);
                me.printStackTrace();
                return false;
            }
            return false;
        }

        // the "first" flag is inherited from the base step implementation
        // it is used to guard some processing tasks, like figuring out field indexes
        // in the row structure that only need to be done once
        if (first) {
            first = false;

            try {
                mqttClientProducer = new MqttClient(broker, clientId, persistence);
                connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
                mqttClientProducer.connect(connOpts);
                logBasic("Connected to: " + broker + " & Topic: " + topic);

            } catch (MqttException me) {
                logErrorMsg(me.getReasonCode(), me.getMessage(), me.getLocalizedMessage(), me.getCause(), me);
                me.printStackTrace();
            }
            // clone the input row structure and place it in our data object
            data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
            // use meta.getFields() to change it, so it reflects the output row structure
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this, null, null);
        }

        try {
            int index = getInputRowMeta().indexOfValue(meta.getPubMessage().toString());
            message = r[index].toString();
            msg = new MqttMessage(message.getBytes());
            msg.setQos(outQos);
            mqttClientProducer.publish(topic, msg);

        } catch (MqttException me) {
            logErrorMsg(me.getReasonCode(), me.getMessage(), me.getLocalizedMessage(), me.getCause(), me);
            me.printStackTrace();
            return false;
        }

        // put the row to the output row stream
        putRow(data.outputRowMeta, r);

        // log progress if it is time to to so
        if (checkFeedback(getLinesRead())) {
            logBasic("Linenr " + getLinesRead()); // Some basic logging
        }

        // indicate that processRow() should be called again
        return true;
    }

    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {

        // Casting to step-specific implementation classes is safe
        ProducerStepMeta meta = (ProducerStepMeta) smi;
        ProducerStepData data = (ProducerStepData) sdi;

        super.dispose(meta, data);
    }

    private void logErrorMsg(int reason, String message, String locMessage, Throwable cause, MqttException ex) {

    }

}
