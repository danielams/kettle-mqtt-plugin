package org.pentaho.kettle.plugin.mqtt.ui.steps.producer;

import org.pentaho.kettle.plugin.mqtt.steps.producer.ProducerStepMeta;
import org.pentaho.ui.xul.XulEventSourceAdapter;

/**
 * Created by dams on 02-08-2016.
 */
public class ProducerStepMetaMapper extends XulEventSourceAdapter {

    private static String PUB_BROKER = "pub_broker";
    private static String PUB_MESSAGE = "pub_message";
    private static String PUB_TOPIC = "pub_topic";
    private static String PUB_CLIENTID = "pub_clientid";
    private static String PQOS = "pqos";

    private String pBroker = "tcp://127.0.0.1:1883";
    private String pMessage = "";
    private String pTopic = "iotdevices";
    private String pClientId = "clientID";
    private int pQOS = 1;

    public void setpBroker(String args) {
        String previousVal = pBroker;
        this.pBroker = args;
        firePropertyChange(PUB_BROKER, previousVal, pBroker);
    }

    public void setpMessage(String arg) {
        String previousVal = pMessage;
        this.pMessage = arg;
        firePropertyChange(PUB_MESSAGE, previousVal, pMessage);
    }

    public void setpTopic(String arg) {
        String previousVal = pTopic;
        this.pTopic = arg;
        firePropertyChange(PUB_TOPIC, previousVal, pTopic);
    }

    public void setpClientId(String arg) {
        String previousVal = pClientId;
        this.pClientId = arg;
        firePropertyChange(PUB_CLIENTID, previousVal, pClientId);
    }

    public void setpQOS(int arg) {
        int previousVal = pQOS;
        this.pQOS = arg;
        firePropertyChange(PQOS, previousVal, pQOS);
    }

    public String getpBroker() {
        return pBroker;
    }

    public String getpMessage() {
        return pMessage;
    }

    public String getpClientId() {
        return pClientId;
    }

    public int getpQOS() {
        return pQOS;
    }

    public String getpTopic() {
        return pTopic;
    }

    public void loadMeta(ProducerStepMeta meta) {
        setpBroker(meta.getPubBroker());
        setpMessage(meta.getPubMessage());
        setpClientId(meta.getPubClientId());
        setpTopic(meta.getPubTopic());
        setpQOS(meta.getPubQos());
    }

    public void saveMeta(ProducerStepMeta meta) {
        meta.setPubBroker(getpBroker());
        meta.setPubQos(getpQOS());
        meta.setPubTopic(getpTopic());
        meta.setPubMessage(getpMessage());
        meta.setPubClientId(getpClientId());
        meta.setChanged();
    }
}
