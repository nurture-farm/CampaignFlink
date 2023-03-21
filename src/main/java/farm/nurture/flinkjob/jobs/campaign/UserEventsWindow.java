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
import java.util.Arrays;

import com.google.protobuf.ByteString;
import farm.nurture.flinkjob.common.dto.SinkOutput;

public class UserEventsWindow extends SinkOutput implements Serializable {

    private long user_id;
    private int user_type;
    private int namespace;
    private long start_time;
    private long end_time;
    private String[] event_names;
    private Long[] event_times;
    private Boolean[] event_bits_1;
    private Boolean[] event_bits_2;
    private Boolean[] event_bits_3;
    private Boolean[] event_bits_4;
    private Boolean[] event_bits_5;
    private String[] event_meta_data;

    public UserEventsWindow(long user_id, int user_type, int namespace, long start_time, long end_time, String[] event_names, Long[] event_times, Boolean[] event_bits_1, Boolean[] event_bits_2, Boolean[] event_bits_3, Boolean[] event_bits_4, Boolean[] event_bits_5, String[] event_meta_data) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.namespace = namespace;
        this.start_time = start_time;
        this.end_time = end_time;
        this.event_names = event_names;
        this.event_times = event_times;
        this.event_bits_1 = event_bits_1;
        this.event_bits_2 = event_bits_2;
        this.event_bits_3 = event_bits_3;
        this.event_bits_4 = event_bits_4;
        this.event_bits_5 = event_bits_5;
        this.event_meta_data = event_meta_data;
    }


    @Override
    public String toString() {
        return "UserEventsWindow{" +
                "user_id=" + user_id +
                ", user_type=" + user_type +
                ", namespace=" + namespace +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", event_names=" + Arrays.toString(event_names) +
                ", event_times=" + Arrays.toString(event_times) +
                ", event_bits_1=" + Arrays.toString(event_bits_1) +
                ", event_bits_2=" + Arrays.toString(event_bits_2) +
                ", event_bits_3=" + Arrays.toString(event_bits_3) +
                ", event_bits_4=" + Arrays.toString(event_bits_4) +
                ", event_bits_5=" + Arrays.toString(event_bits_5) +
                ", event_meta_data=" + Arrays.toString(event_meta_data) +
                '}';
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}
