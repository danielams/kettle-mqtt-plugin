package org.pentaho.kettle.plugin.mqtt.ui.steps.producer;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.util.StringUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepXulDialog;
import org.pentaho.kettle.plugin.mqtt.steps.producer.ProducerStepMeta;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.di.core.row.ValueMetaInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dams on 02-08-2016.
 */
public class ProducerStepDialog extends BaseStepXulDialog implements StepDialogInterface{

    private static final Class<?> PKG = ProducerStepMeta.class;

    private String workingStepname;

    private XulMenuList<?> messageFieldsNames;
    private List<ValueMetaInterface> messageFields = new ArrayList<ValueMetaInterface>();

    private ProducerStepMetaMapper metaMapper;

    public ProducerStepDialog(Shell parent, Object in, TransMeta tr, String sname) throws Throwable {
        super("org/pentaho/kettle/plugin/mqtt/ui/producer/dialog.xul", parent, (BaseStepMeta) in, tr, sname);
        init();
    }

    public void init() throws Throwable {
        workingStepname = stepname;

        metaMapper = new ProducerStepMetaMapper();

        metaMapper.loadMeta((ProducerStepMeta) baseStepMeta);

        RowMetaInterface inputRow = null;
        try {
            inputRow = transMeta.getPrevStepFields(stepMeta);
        }catch (KettleStepException e){

        }

        if(!StringUtil.isEmpty(metaMapper.getpMessage())){
            messageFields.add(new ValueMeta(metaMapper.getpMessage()));
        }

        if(inputRow != null){
            for (ValueMetaInterface field : inputRow.getValueMetaList()){
                if(StringUtil.isEmpty(metaMapper.getpMessage()) || !metaMapper.getpMessage().equals(field.getName())){
                    messageFields.add( new ValueMeta(field.getName()));
                }
            }
        }

        setTextBoxValue("pBroker-field", metaMapper.getpBroker());
        setTextBoxValue("pTopic-field", metaMapper.getpTopic());
        setTextBoxValue("pClientID-field", metaMapper.getpClientId());
        setTextBoxValue("qos-field", Integer.toString(metaMapper.getpQOS()));

        bf.setBindingType(Binding.Type.ONE_WAY);
        bf.createBinding("step-name", "value", this, "stepName");
        bf.createBinding(this, "stepName", "step-name", "value").fireSourceChanged();
        bf.createBinding(this, "messageFields", "message-fieldname", "elements").fireSourceChanged();

        messageFieldsNames = (XulMenuList<?>) getXulDomContainer().getDocumentRoot().getElementById("message-fieldname");
        if ((messageFieldsNames != null) && (messageFieldsNames.getElements().size() > 0)){
            messageFieldsNames.setSelectedIndex(0);
        }

    }

    private void setTextBoxValue(String textbox, String value) {

        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById(textbox)).setValue(value);
    }

    @Override
    protected Class<?> getClassForMessages() {
        return ProducerStepMeta.class;
    }

    @Override
    public void onAccept() {
        metaMapper.setpBroker(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("pBroker-field")));
        metaMapper.setpMessage(messageFieldsNames.getValue());
        metaMapper.setpTopic(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("pTopic-field")));
        metaMapper.setpClientId(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("pClientID-field")));
        metaMapper.setpQOS(Integer.parseInt(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("qos-field"))));

        if (!workingStepname.equals(stepname)) {
            stepname = workingStepname;
            baseStepMeta.setChanged();
        }

        metaMapper.saveMeta((ProducerStepMeta) baseStepMeta);
        dispose();
    }

    private String fetchValue(XulTextbox textbox) {
        String result = "";

        if (textbox != null && !StringUtil.isEmpty(textbox.getValue())) {
            try {
                result = textbox.getValue();
            } catch (NumberFormatException e) {
                log.logError(BaseMessages.getString("HadoopEnter.Error.ParseInteger", textbox.getValue()));
            }
        }

        return result;
    }

    @Override
    public void onCancel() {
        setStepName(null);
        dispose();
    }

    public void setStepName(String stepname) {
        workingStepname = stepname;
    }

    public String getStepName() {
        return workingStepname;
    }

    public List<ValueMetaInterface> getMessageFields() {
        return messageFields;
    }
}
