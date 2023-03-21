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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import farm.nurture.core.contracts.common.DataPlatformMessage;
import org.apache.flink.api.common.functions.AggregateFunction;

public class DataPlatformMessageAggregateFunction
        implements AggregateFunction<DataPlatformMessage, DataPlatformMessageAggregator, UserEventsWindow> {
    private final int COLUMN_COUNT = 5;
    private final int BITS_ARRAY_SIZE = 1000;

    @Override
    public DataPlatformMessageAggregator createAccumulator() {
        return new DataPlatformMessageAggregator();
    }

    @Override
    public DataPlatformMessageAggregator add(DataPlatformMessage value, DataPlatformMessageAggregator accumulator) {
        return accumulator.add(value);
    }

    @Override
    public UserEventsWindow getResult(DataPlatformMessageAggregator dataPlatformMessageAggregator) {
        List<String> eventNames = new ArrayList<>();
        List<Long> eventTimes = new ArrayList<>();
        List<String> eventMetaData = new ArrayList<>();
        Boolean[][] bitsArray = new Boolean[COLUMN_COUNT][BITS_ARRAY_SIZE];
        dataPlatformMessageAggregator.getDataPlatformMessageList().forEach(dpm -> {
            eventNames.add(dpm.getEventName());
            eventTimes.add(dpm.getTimestamp().getSeconds());
            eventMetaData.add(dpm.getEventData().toStringUtf8());
            int eventIndex = dpm.getEventIndex();
            if(eventIndex == 0) return;
            int col = (eventIndex - 1) / BITS_ARRAY_SIZE;
            int pos = (eventIndex - 1) % BITS_ARRAY_SIZE;
            bitsArray[col][pos] = true;
        });
        List<List<Boolean>> bitsList = arrayToList(bitsArray);
        DataPlatformMessage dataPlatformMessage = dataPlatformMessageAggregator.getDataPlatformMessageList().iterator().next();
        return new UserEventsWindow(dataPlatformMessage.getActor().getActorId(), dataPlatformMessage.getActor().getActorType().getNumber(), dataPlatformMessage.getNamespace().getNumber(),
                0L, 0L, eventNames.toArray(new String[0]), eventTimes.toArray(new Long[0]), bitsList.get(0).toArray(new Boolean[0]), bitsList.get(1).toArray(new Boolean[0]),
                bitsList.get(2).toArray(new Boolean[0]), bitsList.get(3).toArray(new Boolean[0]), bitsList.get(4).toArray(new Boolean[0]), eventMetaData.toArray(new String[0]));
    }

    private List<List<Boolean>> arrayToList(Boolean[][] arr) {
        for(int i = 0; i< COLUMN_COUNT; i++) {
            for(int j = 0; j< BITS_ARRAY_SIZE; j++) {
                if(arr[i][j] == null) {
                    arr[i][j] = false;
                }
            }
        }

        List<List<Boolean>> lists = new ArrayList<>();
        for(int i = 0; i< COLUMN_COUNT; i++) {
            lists.add(Arrays.stream(arr[i]).collect(Collectors.toList()));
        }

        return lists;
    }

    @Override
    public DataPlatformMessageAggregator merge(DataPlatformMessageAggregator a, DataPlatformMessageAggregator b) {
        return a.add(b);
    }
}
