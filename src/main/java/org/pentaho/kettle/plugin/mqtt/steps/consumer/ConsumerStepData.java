/*! ******************************************************************************
*
* Pentaho Data Integration
*
* Copyright (C) 2002-2013 by Pentaho : http://www.pentaho.com
*
*******************************************************************************
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
******************************************************************************/


package org.pentaho.kettle.plugin.mqtt.steps.consumer;

/**
 * Created by dams on 02-08-2016.
 */

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * This class is the implementation of StepDataInterface.
 * <p>
 * Implementing classes inherit from BaseStepData, which implements the entire
 * interface completely.
 * <p>
 * In addition classes implementing this interface usually keep track of
 * per-thread resources during step execution. Typical examples are:
 * result sets, temporary data, caching indexes, etc.
 * <p>
 * The implementation for the demo step stores the output row structure in
 * the data class.
 */
public class ConsumerStepData extends BaseStepData implements StepDataInterface {

    public boolean readsRows;
    public RowMetaInterface outputRowMeta;

    public ConsumerStepData() {
        super();
    }
}
