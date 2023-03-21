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

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class DataPlatformMessageProccessWindowFunction extends ProcessWindowFunction<UserEventsWindow, UserEventsWindow, String, TimeWindow> {

    @Override
    public void process(String s, Context context, Iterable<UserEventsWindow> elements, Collector<UserEventsWindow> out) {
        UserEventsWindow userEventsWindow = elements.iterator().next();
        userEventsWindow.setStart_time(context.window().getStart());
        userEventsWindow.setEnd_time(context.window().getEnd());
        userEventsWindow.setEvent_time((context.window().getStart() + context.window().getEnd()) / 2);
        out.collect(userEventsWindow);
    }
}
