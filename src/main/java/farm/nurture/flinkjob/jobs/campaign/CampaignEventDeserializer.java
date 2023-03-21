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

import java.io.IOException;

import farm.nurture.core.contracts.common.DataPlatformMessage;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class CampaignEventDeserializer implements DeserializationSchema<DataPlatformMessage> {
    @Override
    public DataPlatformMessage deserialize(byte[] bytes) throws IOException {
        return DataPlatformMessage.parseFrom(bytes);
    }

    @Override
    public boolean isEndOfStream(DataPlatformMessage campaignEvent) {
        return false;
    }

    @Override
    public TypeInformation<DataPlatformMessage> getProducedType() {
        return TypeInformation.of(DataPlatformMessage.class);
    }
}