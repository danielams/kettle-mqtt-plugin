package org.pentaho.kettle.plugin.mqtt.ui.steps.consumer;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.util.StringUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.trans.step.BaseStepXulDialog;
import org.pentaho.kettle.plugin.mqtt.steps.consumer.ConsumerStepMeta;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.components.XulTextbox;


/**
 * Created by dams on 02-08-2016.
 */
public class ConsumerStepDialog extends BaseStepXulDialog implements StepDialogInterface {

    private static final Class<?> PKG = ConsumerStepMeta.class;

    private String workingStepname;
    private String outputName;

    private ConsumerStepMetaMapper metaMapper;

    public ConsumerStepDialog(Shell parent, Object in, TransMeta tr, String sname) throws Throwable {
        super("org/pentaho/kettle/plugin/mqtt/ui/consumer/dialog.xul", parent, (BaseStepMeta) in, tr, sname);
        init();
    }

    public void init() throws Throwable {
        workingStepname = stepname;

        metaMapper = new ConsumerStepMetaMapper();

        metaMapper.loadMeta((ConsumerStepMeta) baseStepMeta);

        bf.setBindingType(Binding.Type.ONE_WAY);

        setTextBoxValue("output-field", metaMapper.getOutput());
        setTextBoxValue("hostname-field", metaMapper.getsBroker());
        setTextBoxValue("topic-field", metaMapper.getsTopic());
        setTextBoxValue("ClientID-field", metaMapper.getClientId());
        setTextBoxValue("qos-field", Integer.toString(metaMapper.getQos()));

        bf.createBinding("step-name", "value", this, "stepName");
        bf.createBinding(this, "stepName", "step-name", "value").fireSourceChanged();

    }

    private void setTextBoxValue(String textbox, String value) {

        ((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById(textbox)).setValue(value);
    }

    @Override
    protected Class<?> getClassForMessages() {
        return ConsumerStepMeta.class;
    }

    @Override
    public void onAccept() {
        metaMapper.setOutput(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById(
                "output-field")));
        metaMapper.setsBroker(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("hostname-field")));
        metaMapper.setSTopic(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("topic-field")));
        metaMapper.setClientId(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("ClientID-field")));
        metaMapper.setQos(Integer.parseInt(fetchValue((XulTextbox) getXulDomContainer().getDocumentRoot().getElementById("qos-field"))));

        if (!workingStepname.equals(stepname)) {
            stepname = workingStepname;
            baseStepMeta.setChanged();
        }

        metaMapper.saveMeta((ConsumerStepMeta) baseStepMeta);
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

}
