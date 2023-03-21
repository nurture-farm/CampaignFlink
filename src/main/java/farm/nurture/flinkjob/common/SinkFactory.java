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

import java.io.IOException;

import farm.nurture.flinkjob.common.dto.SinkOutput;
import farm.nurture.flinkjob.util.ApplicationProperties;
import farm.nurture.flinkjob.util.Constants;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.ParquetBuilder;
import org.apache.flink.formats.parquet.ParquetWriterFactory;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import farm.nurture.flinkjob.enums.SinkType;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.OutputFile;

public class SinkFactory<T extends SinkOutput> {

    private final PrintSinkFunction<T> printSinkFunction;
    private final StreamingFileSink<T> s3Sink;

    public SinkFactory(Class<T> clazz) {
        this.printSinkFunction = new PrintSinkFunction<>();
        this.s3Sink = StreamingFileSink
                .forBulkFormat(new Path(ApplicationProperties.get(Constants.SINK_PATH_KEY)), forReflectRecord(clazz))
                .withBucketAssigner(new EventTimeBuckets<>())
                .build();
    }

    public static <T> ParquetWriterFactory<T> forReflectRecord(Class<T> type) {
        final String schemaString = ReflectData.get().getSchema(type).toString();
        final ParquetBuilder<T> builder =
                (out) -> createAvroParquetWriter(schemaString, ReflectData.get(), out);
        return new ParquetWriterFactory<>(builder);
    }

    private static <T> ParquetWriter<T> createAvroParquetWriter(
            String schemaString, GenericData dataModel, OutputFile out) throws IOException {

        final Schema schema = new Schema.Parser().parse(schemaString);

        return AvroParquetWriter.<T>builder(out)
                .withSchema(schema)
                .withDataModel(dataModel)
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .build();
    }

    public SinkFunction<T> getSink(SinkType sinkType) {
        switch (sinkType) {
            case S3_SINK:
                return s3Sink;
            case PRINT_SINK:
                return printSinkFunction;
            default:
                throw new RuntimeException("Sink Not Supported");
        }
    }
}
