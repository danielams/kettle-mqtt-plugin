package org.pentaho.kettle.plugin.mqtt.steps.producer;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * Created by dams on 02-08-2016.
 */
public class ProducerStepData extends BaseStepData implements StepDataInterface{

    public RowMetaInterface outputRowMeta;

    public ProducerStepData(){
        super();
    }
}
