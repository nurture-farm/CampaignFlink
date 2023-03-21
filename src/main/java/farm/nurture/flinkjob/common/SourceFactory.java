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

package farm.nurture.flinkjob.common;

import farm.nurture.flinkjob.util.ApplicationProperties;
import farm.nurture.flinkjob.util.Constants;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import farm.nurture.flinkjob.enums.SourceType;

public class SourceFactory<T> {

    private final KafkaSource<T> kafkaSource;

    public SourceFactory(DeserializationSchema<T> deserializationSchema) {
        this.kafkaSource = KafkaSource.<T>builder()
                .setBootstrapServers(ApplicationProperties.get(Constants.KAFKA_BOOTSTRAP_SERVERS_KEY))
                .setTopics(ApplicationProperties.get(Constants.KAFKA_TOPIC_KEY))
                .setGroupId(ApplicationProperties.get(Constants.KAFKA_GROUP_KEY))
                .setStartingOffsets(OffsetsInitializer.committedOffsets())
                .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(deserializationSchema))
                .build();
    }

    public KafkaSource<T> getSource(SourceType sourceType) {
        switch (sourceType) {
            case KAFKA:
                return kafkaSource;
            default:
                throw new RuntimeException("Source Not Defined");
        }
    }
}
