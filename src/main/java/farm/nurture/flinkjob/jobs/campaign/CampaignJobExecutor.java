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

import java.time.Duration;
import java.time.Instant;

import farm.nurture.flinkjob.common.SinkFactory;
import farm.nurture.flinkjob.common.SourceFactory;
import farm.nurture.flinkjob.enums.SinkType;
import farm.nurture.flinkjob.enums.SourceType;
import farm.nurture.core.contracts.common.DataPlatformMessage;
import farm.nurture.flinkjob.util.ApplicationProperties;
import farm.nurture.flinkjob.jobs.JobExecutor;
import farm.nurture.flinkjob.util.Constants;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class CampaignJobExecutor implements JobExecutor {

    private final StreamExecutionEnvironment env;

    public CampaignJobExecutor() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(ApplicationProperties.getInt(Constants.CHECKPOINT_INTERVAL_KEY));
        env.getCheckpointConfig().setCheckpointStorage(ApplicationProperties.get(Constants.CHECKPOINT_STORAGE_KEY));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    }

    public void executeJob() throws Exception {
        KafkaSource<DataPlatformMessage> source = new SourceFactory<>(new CampaignEventDeserializer()).getSource(SourceType.KAFKA);

        WatermarkStrategy<DataPlatformMessage> watermarkStrategy = WatermarkStrategy
                .<DataPlatformMessage>forBoundedOutOfOrderness(Duration.ofSeconds(ApplicationProperties.getInt(Constants.LATENESS_KEY)))
                .withIdleness(Duration.ofSeconds((ApplicationProperties.getInt(Constants.IDLENESS_KEY))))
                .withTimestampAssigner((event, timestamp) -> Math.min(event.getTimestamp().getSeconds(), Instant.now().toEpochMilli()));

        DataStreamSource<DataPlatformMessage> dataStreamSource = env.fromSource(source, watermarkStrategy, "Kafka Source");
        SingleOutputStreamOperator<DataPlatformMessage> singleOutputStreamOperator = dataStreamSource.filter(m -> m.getActor().getActorId() != 0);
        KeyedStream<DataPlatformMessage, String> keyedStream = singleOutputStreamOperator.keyBy(x -> x.getActor().getActorId() + "_" + x.getActor().getActorTypeValue());

        WindowedStream<DataPlatformMessage, String, TimeWindow> windowedStream = keyedStream.window(TumblingEventTimeWindows.of(Time.seconds(ApplicationProperties.getInt(Constants.WINDOW_SIZE_KEY)))).trigger(EventTimeTrigger.create());

        DataStream<UserEventsWindow> dataStream = windowedStream.aggregate(new DataPlatformMessageAggregateFunction(), new DataPlatformMessageProccessWindowFunction());

        SinkFactory<UserEventsWindow> sinkFactory = new SinkFactory<>(UserEventsWindow.class);
        dataStream.addSink(sinkFactory.getSink(SinkType.PRINT_SINK));
        dataStream.addSink(sinkFactory.getSink(SinkType.S3_SINK));

        env.execute();
    }
}