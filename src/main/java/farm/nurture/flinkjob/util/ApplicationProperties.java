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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationProperties {

    private static ParameterTool parameterTool;

    public static void init() throws IOException {
        if(parameterTool != null) {
            return;
        }
        synchronized (ApplicationProperties.class) {
            if(parameterTool != null) {
                return;
            }
            parameterTool = ParameterTool.fromPropertiesFile(new FileInputStream("/opt/flink/conf/flink-conf.yaml"));
            String env = parameterTool.get("env");
            env = env == null ? "dev" : env;
            String fileName = "config_" + env + ".yaml";

            InputStream ioStream = ApplicationProperties.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (ioStream == null) {
                throw new IllegalArgumentException(fileName + " is not found");
            }
            parameterTool = ParameterTool.fromPropertiesFile(ioStream);
        }
    }

    public static String get(String key) {
        return parameterTool.get(key);
    }

    public static int getInt(String key) {
        return parameterTool.getInt(key);
    }
}
