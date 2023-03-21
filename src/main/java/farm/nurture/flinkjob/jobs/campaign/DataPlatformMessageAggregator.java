/*
 *  Copyright 2023 NURTURE AGTECH PVT LTD
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package farm.nurture.flinkjob.jobs.campaign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import farm.nurture.core.contracts.common.DataPlatformMessage;

public class DataPlatformMessageAggregator implements Serializable {

    private List<DataPlatformMessage> dataPlatformMessageList;

    public DataPlatformMessageAggregator() {
        dataPlatformMessageList = new ArrayList<>();
    }

    public List<DataPlatformMessage> getDataPlatformMessageList() {
        return dataPlatformMessageList;
    }

    public DataPlatformMessageAggregator add(DataPlatformMessage dataPlatformMessage) {
        dataPlatformMessageList.add(dataPlatformMessage);
        return this;
    }

    public DataPlatformMessageAggregator add(DataPlatformMessageAggregator dataPlatformMessageAggregator) {
        this.dataPlatformMessageList.addAll(dataPlatformMessageAggregator.getDataPlatformMessageList());
        return this;
    }

    @Override
    public String toString() {
        return "DataPlatformMessageAggregator{" +
                "dataPlatformMessageList=" + dataPlatformMessageList +
                '}';
    }
}
