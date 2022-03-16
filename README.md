# FeedKA

This project provide data of cace (customer attributes collection) to Klient Analytics:

1. It reads Customer Attributes enriched data evaluated on S3. Only the Delta elaboration is evaluated

2. The data are filtered for CurrentValue=True and other QUality Check are performed

3. A distinct on the data read is poerformed in order to avoid possible dupllications due to multiple execution of the DataLake elaboration.

4. It normalizes the attributes performing the pivot of the different attributes: 
	on DocumentDB each attribute is stored separately, instead, on Klient Analytics all the attributes are different columns of the same records.

5. The final output is transformed considering the target table in Klient Analytics

6. The output is written to KA

```


Livy submit : 

{
  "file": "s3a://${s3.dev.bucket}/projects/mdm/caceSDG/convertor-assembly-1.0.0.jar",
  "className": "com.kering.datalake.main.ConvertorProcess",
  "name": "agg_to_usg_client_cace_datalake_${now():format('yyyyMMddHHmmss', 'UTC')}",
  "executorCores": 2,
  "numExecutors": 2,
  "queue" : "${queue_spark_batch}",
  "conf": {
    "spark.executor.memory": "4G",
    "spark.driver.memory": "2G",
    "spark.driver.extraJavaOptions": "-Doverride-app.spark.appName=${jobName} -Doverride-app.spark.inputFormatDF=parquet -Doverride-app.s3.input.inputKey=${s3.proddata.key}/date=${runDate} -Doverride-app.spark.transferId=${Transfer_id} -Doverride-app.msserver.url=${ka.url} -Doverride-app.msserver.table=${ka.table} -Doverride-app.spark.outputFormatDF=msjdbc -Doverride-app.spark.saveMode=append -Doverride-app.aws.ssmKey=datalake-dev-ka-dashboards-master-password -Doverride-app.s3.input.bucket=${s3.proddata.bucket} -XX:+AlwaysPreTouch -XX:+UseLargePages -XX:+UseG1GC -XX:+UseTLAB -XX:+ResizeTLAB",	
    "spark.executor.extraJavaOptions": "-Doverride-app.spark.appName=${jobName} -Doverride-app.spark.inputFormatDF=parquet -Doverride-app.s3.input.inputKey=${s3.proddata.key}/date=${runDate} -Doverride-app.spark.transferId=${Transfer_id} -Doverride-app.msserver.url=${ka.url} -Doverride-app.msserver.table=${ka.table} -Doverride-app.spark.outputFormatDF=msjdbc -Doverride-app.spark.saveMode=append -Doverride-app.aws.ssmKey=datalake-dev-ka-dashboards-master-password -Doverride-app.s3.input.bucket=${s3.proddata.bucket} -XX:+AlwaysPreTouch -XX:+UseLargePages -XX:+UseG1GC -XX:+UseTLAB -XX:+ResizeTLAB",
    "spark.jars.packages": "com.microsoft.azure:spark-mssql-connector:1.0.0" 
  }
}

```

## Release

We release the artifact on :
 - DEV : `kering-datalake-dev-data/projects/mdm/caceSDG/convertor-assembly-1.0.1.jar`
 - PRD : `kering-datalake-prod-data/projects/mdm/caceSDG/convertor-assembly-1.0.1.jar`



```
## Connection to Klient Analytics

The connection with Klient Analytics has been performed inside Spark passing to the system the mssql driver.
