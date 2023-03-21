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

package farm.nurture.flinkjob.util;

public class Constants {
    public static final String CHECKPOINT_INTERVAL_KEY = "flink.checkpoint.interval";
    public static final String CHECKPOINT_STORAGE_KEY = "flink.checkpoint.storage";
    public static final String LATENESS_KEY = "flink.window.allowed-lateness";
    public static final String IDLENESS_KEY = "flink.window.idleness";
    public static final String WINDOW_SIZE_KEY = "flink.window.size";
    public static final String SINK_PATH_KEY = "flink.sink.path";
    public static final String KAFKA_BOOTSTRAP_SERVERS_KEY = "flink.source.kafka.bootstrap.servers";
    public static final String KAFKA_TOPIC_KEY = "flink.source.kafka.topic";
    public static final String KAFKA_GROUP_KEY = "flink.source.kafka.group";
}
