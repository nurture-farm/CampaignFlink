FROM flink

RUN mkdir -p /flink-job

COPY target/FlinkJob-1.0-SNAPSHOT.jar $FLINK_HOME/usrlib/FlinkTestJob.jar
COPY target/dependency-jars $FLINK_HOME/usrlib/dependency-jars
COPY target/dependency-jars/flink-s3-fs-hadoop-1.15.0.jar $FLINK_HOME/plugins/s3-fs-hadoop/flink-s3-fs-hadoop-1.15.0.jar
COPY target/dependency-jars/flink-s3-fs-presto-1.15.0.jar $FLINK_HOME/plugins/s3-fs-presto/flink-s3-fs-presto-1.15.0.jar