import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object RetailAnalysisLocal {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("E-commerce Retail Analysis - Local Mode")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    println("=" * 50)
    println("开始执行电商数据统计分析（本地模式）")
    println("=" * 50)

    // 1. 读取数据（从本地文件系统）
    println("1. 读取数据...")
    val df = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv("file:///home/djj/data/Online_Retail.csv")

    println(s"原始数据量: ${df.count()} 条")

    // 2. 数据清洗
    println("2. 数据清洗...")
    val salesDF = df
      .filter($"CustomerID".isNotNull && $"Quantity" > 0 && $"UnitPrice" > 0)
      .withColumn("TotalAmount", $"Quantity" * $"UnitPrice")

    println(s"清洗后数据量: ${salesDF.count()} 条")

    // 3. 统计任务1：各国销售额 TOP10
    println("3. 各国销售额 TOP10...")
    salesDF.groupBy("Country")
      .agg(sum("TotalAmount").as("TotalSales"))
      .orderBy($"TotalSales".desc)
      .limit(10)
      .show()

    // 4. 统计任务2：月度销售趋势
    println("4. 月度销售趋势...")
    salesDF.withColumn("YearMonth", date_format($"InvoiceDate", "yyyy-MM"))
      .groupBy("YearMonth")
      .agg(sum("TotalAmount").as("MonthlySales"))
      .orderBy("YearMonth")
      .show(24)

    // 5. 统计任务3：商品热度 TOP10（按销量）
    println("5. 商品热度 TOP10...")
    salesDF.groupBy("Description")
      .agg(sum("Quantity").as("TotalQuantity"))
      .orderBy($"TotalQuantity".desc)
      .limit(10)
      .show()

    // 6. 统计任务4：商品销售额 TOP10
    println("6. 商品销售额 TOP10...")
    salesDF.groupBy("Description")
      .agg(sum("TotalAmount").as("TotalRevenue"))
      .orderBy($"TotalRevenue".desc)
      .limit(10)
      .show()

    // 7. 统计任务5：时段销售分析
    println("7. 时段销售分析...")
    salesDF.withColumn("Hour", hour($"InvoiceDate"))
      .groupBy("Hour")
      .agg(count("*").as("OrderCount"), sum("TotalAmount").as("TotalSales"))
      .orderBy("Hour")
      .show(24)

    // 8. 统计任务6：星期销售分析
    println("8. 星期销售分析...")
    salesDF.withColumn("Weekday", date_format($"InvoiceDate", "EEEE"))
      .groupBy("Weekday")
      .agg(sum("TotalAmount").as("TotalSales"))
      .orderBy($"TotalSales".desc)
      .show()

    println("=" * 50)
    println("统计分析完成！")
    println("=" * 50)

    spark.stop()
  }
}
