package org.pentaho.kettle.plugin.mqtt.ui.steps.consumer;

import org.pentaho.kettle.plugin.mqtt.steps.consumer.ConsumerStepMeta;
import org.pentaho.ui.xul.XulEventSourceAdapter;

/**
 * Created by dams on 02-08-2016.
 */
public class ConsumerStepMetaMapper extends XulEventSourceAdapter {

    public static String OUTPUT_NAME = "output_name";
    public static String SUB_BROKER = "sub_broker";
    public static String SUB_TOPIC = "sub_topic";
    public static String CLIENT_ID = "client_id";
    public static String QOS = "qos";

    private String output = "dummy";
    private String sBroker = "tcp://127.0.0.1:1883";
    private String sTopic = "iotdevices";
    private String clientI = "clientID";
    private int qos = 1;

    public void setOutput(String arg) {
        String previousVal = output;
        output = arg;
        firePropertyChange(OUTPUT_NAME, previousVal, output);
    }

    public void setsBroker(String arg) {
        String previousVal = sBroker;
        sBroker = arg;
        firePropertyChange(SUB_BROKER, previousVal, sBroker);
    }

    public void setSTopic(String arg) {
        String previousVal = sTopic;
        sTopic = arg;
        firePropertyChange(SUB_TOPIC, previousVal, sTopic);
    }

    public void setClientId(String arg) {
        String previousVal = clientI;
        clientI = arg;
        firePropertyChange(CLIENT_ID, previousVal, clientI);
    }

    public void setQos(int qos) {
        int previousVal = qos;
        this.qos = qos;
        firePropertyChange(QOS, previousVal, qos);
    }

    public String getOutput() {
        return output;
    }

    public String getsBroker() {
        return sBroker;
    }

    public String getsTopic() {
        return sTopic;
    }

    public String getClientId() {
        return clientI;
    }

    public int getQos() {
        return qos;
    }

    /**
     * Load data into the MetaMapper from the HadoopExitMeta
     *
     * @param meta
     */
    public void loadMeta(ConsumerStepMeta meta) {
        setOutput(meta.getOutputField());
        setsBroker(meta.getSubBroker());
        setClientId(meta.getClientId());
        setQos(meta.getQos());
        setSTopic(meta.getSubTopic());
    }

    /**
     * Save data from the MetaMapper into the HadoopExitMeta
     *
     * @param meta
     */
    public void saveMeta(ConsumerStepMeta meta) {
        meta.setOutputField(getOutput());
        meta.setSubBroker(getsBroker());
        meta.setClientId(getClientId());
        meta.setQos(getQos());
        meta.setSubTopic(getsTopic());
        meta.setChanged();
    }
}
