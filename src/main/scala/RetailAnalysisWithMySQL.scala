import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object RetailAnalysisWithMySQL {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("E-commerce Retail Analysis with MySQL")
      .getOrCreate()

    import spark.implicits._

    println("=" * 60)
    println("开始执行电商数据统计分析（结果存入MySQL）")
    println("=" * 60)

    // 1. 从 HDFS 读取数据
    println("1. 从 HDFS 读取数据...")
    val df = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv("hdfs://localhost:9000/user/djj/data/Online_Retail.csv")

    println(s"原始数据量: ${df.count()} 条")

    // 2. 数据清洗
    println("2. 数据清洗...")
    val salesDF = df
      .filter($"CustomerID".isNotNull && $"Quantity" > 0 && $"UnitPrice" > 0)
      .withColumn("TotalAmount", $"Quantity" * $"UnitPrice")

    println(s"清洗后数据量: ${salesDF.count()} 条")

    // MySQL 连接配置
    val mysqlUrl = "jdbc:mysql://localhost:3306/spark_results?useSSL=false&serverTimezone=UTC"
    val props = new java.util.Properties
    props.setProperty("user", "root")
    props.setProperty("password", "123456")
    props.setProperty("driver", "com.mysql.cj.jdbc.Driver")

    // 3. 各国销售额 TOP10
    println("3. 各国销售额 TOP10...")
    val countrySales = salesDF.groupBy("Country")
      .agg(sum("TotalAmount").as("TotalSales"))
      .orderBy($"TotalSales".desc).limit(10)
    countrySales.show()
    countrySales.write.mode("overwrite").jdbc(mysqlUrl, "country_sales", props)
    println("   -> 已存入 MySQL 表: country_sales")

    // 4. 月度销售趋势
    println("4. 月度销售趋势...")
    val monthlySales = salesDF.withColumn("month_year", date_format($"InvoiceDate", "yyyy-MM"))
      .groupBy("month_year").agg(sum("TotalAmount").as("monthly_sales"))
      .orderBy("month_year")
    monthlySales.show(24)
    monthlySales.write.mode("overwrite").jdbc(mysqlUrl, "monthly_sales", props)
    println("   -> 已存入 MySQL 表: monthly_sales")

    // 5. 商品热度 TOP10（按销量）
    println("5. 商品热度 TOP10...")
    val topProducts = salesDF.groupBy("Description")
      .agg(sum("Quantity").as("total_quantity"))
      .orderBy($"total_quantity".desc).limit(10)
    topProducts.show()
    topProducts.write.mode("overwrite").jdbc(mysqlUrl, "top_products", props)
    println("   -> 已存入 MySQL 表: top_products")

    // 6. 商品销售额 TOP10（按销售额）
    println("6. 商品销售额 TOP10...")
    val topProductsRevenue = salesDF.groupBy("Description")
      .agg(sum("TotalAmount").as("total_revenue"))
      .orderBy($"total_revenue".desc).limit(10)
    topProductsRevenue.show()
    topProductsRevenue.write.mode("overwrite").jdbc(mysqlUrl, "top_products_revenue", props)
    println("   -> 已存入 MySQL 表: top_products_revenue")

    // 7. 时段销售分析
    println("7. 时段销售分析...")
    val hourlySales = salesDF.withColumn("sale_hour", hour($"InvoiceDate"))
      .groupBy("sale_hour")
      .agg(count("*").as("order_count"), sum("TotalAmount").as("total_sales"))
      .orderBy("sale_hour")
    hourlySales.show(24)
    hourlySales.write.mode("overwrite").jdbc(mysqlUrl, "hourly_sales", props)
    println("   -> 已存入 MySQL 表: hourly_sales")

    // 8. 星期销售分析
    println("8. 星期销售分析...")
    val weekdaySales = salesDF.withColumn("week_day", date_format($"InvoiceDate", "EEEE"))
      .groupBy("week_day").agg(sum("TotalAmount").as("total_sales"))
      .orderBy($"total_sales".desc)
    weekdaySales.show()
    weekdaySales.write.mode("overwrite").jdbc(mysqlUrl, "weekday_sales", props)
    println("   -> 已存入 MySQL 表: weekday_sales")

    println("=" * 60)
    println("统计分析完成！所有结果已存入 MySQL")
    println("=" * 60)

    spark.stop()
  }
}
