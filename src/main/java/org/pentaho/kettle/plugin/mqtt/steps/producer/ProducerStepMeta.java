package org.pentaho.kettle.plugin.mqtt.steps.producer;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.pentaho.kettle.plugin.mqtt.DialogClassUtil;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.util.List;


/**
 * Created by dams on 02-08-2016.
 */
@Step(
        id = "mqttProducer",
        image = "org/pentaho/kettle/plugin/mqtt/steps/producer/producer.svg",
        i18nPackageName = "org.pentaho.kettle.plugin.mqtt.steps.producer",
        name = "mqttProducer.Name",
        description = "mqttProducer.TooltipDesc",
        categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform"
)
public class ProducerStepMeta extends BaseStepMeta implements StepMetaInterface {

    private static Class<?> PKG = ProducerStepMeta.class;

    private String pubBroker;
    private String pubMessage;
    private String pubTopic;
    private String pubClientId;
    private int pubQos;

    public static final String DIALOG_NAME = DialogClassUtil.getDialogClassName(PKG);

    public ProducerStepMeta() throws Throwable {
        super();
    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
        return new ProducerStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
    }

    public StepDataInterface getStepData() {
        return new ProducerStepData();
    }

    public void setDefault() {
        pubBroker = "tcp://127.0.0.1:1883";
        pubMessage = "";
        pubTopic = "iotdevices";
        pubClientId = "client_id";
        pubQos = 1;
    }

    public void setPubBroker(String pubBroker) {
        this.pubBroker = pubBroker;
    }

    public void setPubClientId(String pubClientId) {
        this.pubClientId = pubClientId;
    }

    public void setPubMessage(String pubMessage) {
        this.pubMessage = pubMessage;
    }

    public void setPubQos(int pubQos) {
        this.pubQos = pubQos;
    }

    public void setPubTopic(String pubTopic) {
        this.pubTopic = pubTopic;
    }

    public String getPubBroker() {
        return pubBroker;
    }

    public String getPubClientId() {
        return pubClientId;
    }

    public String getPubMessage() {
        return pubMessage;
    }

    public String getPubTopic() {
        return pubTopic;
    }

    public int getPubQos() {
        return pubQos;
    }

    public Object clone() {
        Object retval = super.clone();
        return retval;
    }

    public String getXML() throws KettleValueException {
        StringBuffer retval = new StringBuffer(300);

        retval.append("    ").append(XMLHandler.addTagValue("pubBroker", getPubBroker())); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    ").append(XMLHandler.addTagValue("pubMessage", getPubMessage()));
        retval.append("    ").append(XMLHandler.addTagValue("pubTopic", getPubTopic()));
        retval.append("    ").append(XMLHandler.addTagValue("pubClientId", getPubClientId()));
        retval.append("    ").append(XMLHandler.addTagValue("pubQOS", getPubQos()));

        return retval.toString();
    }

    public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {

        try {
            setPubBroker(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "pubBroker")));
            setPubClientId(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "pubClientId")));
            setPubMessage(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "pubMessage")));
            setPubTopic(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "pubTopic")));
            setPubQos(Integer.parseInt(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "pubQOS"))));
        } catch (Exception e) {
            throw new KettleXMLException("Demo plugin unable to read step info from XML node", e);
        }

    }

    public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step) throws KettleException {
        try {
            rep.saveStepAttribute(id_transformation, id_step, "pubBroker", pubBroker); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "pubMessage", pubMessage);
            rep.saveStepAttribute(id_transformation,id_step,"pubTopic",pubTopic);
            rep.saveStepAttribute(id_transformation,id_step,"pubClientId", pubClientId);
            rep.saveStepAttribute(id_transformation, id_step, "pubQOS", pubQos);
        } catch (Exception e) {
            throw new KettleException("Unable to save step into repository: " + id_step, e);
        }
    }

    public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException {
        try {
            pubBroker = rep.getStepAttributeString(id_step, "pubBroker"); //$NON-NLS-1$
            pubMessage = rep.getStepAttributeString(id_step, "pubMessage");
            pubTopic = rep.getStepAttributeString(id_step, "pubTopic");
            pubClientId = rep.getStepAttributeString(id_step, "pubClientId");
            pubQos = (int) rep.getStepAttributeInteger(id_step, "pubQOS");
        } catch (Exception e) {
            throw new KettleException("Unable to load step from repository", e);
        }
    }

    public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {

		/*
         * This implementation appends the outputField to the row-stream
		 */

        // a value meta object contains the meta data for a field
        ValueMetaInterface v = new ValueMeta();

        // setting trim type to "both"
        v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);

        // the name of the step that adds this field
        v.setOrigin(name);

        // modify the row structure and add the field this step generates
        //inputRowMeta.addValueMeta(v);

    }

    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info, VariableSpace space, Repository repository, IMetaStore metaStore) {

        CheckResult cr;

        // See if there are input streams leading to this step!
        if (input.length > 0) {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, BaseMessages.getString(PKG, "Demo.CheckResult.ReceivingRows.OK"), stepMeta);
            remarks.add(cr);
        } else {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, BaseMessages.getString(PKG, "Demo.CheckResult.ReceivingRows.ERROR"), stepMeta);
            remarks.add(cr);
        }

    }

    @Override
    public String getDialogClassName() {
        return DIALOG_NAME;
    }

}
