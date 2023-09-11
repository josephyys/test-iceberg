package com.example;
import org.apache.iceberg.Table;
import org.apache.iceberg.spark.SparkCatalog;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.connector.catalog.Identifier;
import org.apache.spark.sql.connector.catalog.TableCatalog;
import org.apache.spark.sql.connector.catalog.CatalogPlugin;
import org.apache.spark.sql.catalyst.analysis.NoSuchTableException;


public class App {

    public static void main(String[] args) {

        // Initialize Spark with Iceberg configurations

        SparkSession spark = SparkSession.builder()
            .appName("IcebergExpireSnapshots")
            .config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog")
            .config("spark.sql.catalog.spark_catalog.type", "hadoop")
            .config("spark.sql.catalog.spark_catalog.warehouse", "path_to_your_warehouse_location")
            .getOrCreate();

        try {

            CatalogPlugin warehouseCatalog = spark.sessionState().catalogManager().catalog("warehouse");
            TableCatalog tableCatalog = (TableCatalog) warehouseCatalog;
            // Table table = tableCatalog.loadTable(Identifier.of("test", "test_table"));
            Table table = (Table) tableCatalog.loadTable(Identifier.of(new String[]{"test"}, "test_table"));

            // Load the Iceberg table using Spark's catalog
            // TableCatalog catalog = (TableCatalog) spark.sessionState().catalogManager().v2SessionCatalog();
            // Table table = (Table) catalog.loadTable(Identifier.of(new String[]{"test"}, "test_table"));

            // Expire old snapshots

            table.expireSnapshots().expireOlderThan(System.currentTimeMillis() - 3000).commit();
        } catch (NoSuchTableException e) {
            System.err.println("Table not found: " + e.getMessage());
        } finally {
            // Stop Spark

            spark.stop();

        }

    }

}

