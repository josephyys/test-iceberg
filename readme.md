## basic environment
https://github.com/zsvoboda/ngods-stocks/blob/main/docker-compose.yml

## in trino docker
- catalog information
  - /opt/trino# ls etc/catalog
- cli to test iceberg warehouse:
  - ./trino-cli-362-executable.jar --server localhost:8060 --catalog warehouse --schema information_schema 

- trino commands
  ```
  CREATE TABLE test.test_table (id INTEGER, name VARCHAR);
  use warehouse.test
  show TABLES;
  INSERT INTO test_table VALUES (11, 'Jane');
  ```



## in aio docker
- spark setting in AIO
  /opt/spark/conf/spark-defaults.conf 
    - spark.sql.extensions = org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions
      - This configuration adds Iceberg extensions to Spark, enabling features like time travel and other Iceberg-specific functionalities.
      
      ``` 
      spark.sql.catalog.warehouse = org.apache.iceberg.spark.SparkCatalog
      spark.sql.defaultCatalog = warehouse
      ```

  
- install maven
  ```
  sudo apt update
  sudo apt install maven
  ```
-  build & exec
  ```
  mvn clean compile
  mvn exec:java -Dexec.mainClass="com.example.App"
  mvn clean package
  exec:
  spark-submit --class com.example.App /opt/spark/expire/iceberg-expire/target/iceberg-expire-1.0-SNAPSHOT.jar
  ```

