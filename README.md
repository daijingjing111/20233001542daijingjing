基于Spark的电商零售数据统计分析与实现

项目简介
基于Spark + HDFS + MySQL三大框架，对电商交易数据进行统计分析，实现了三种不同的查询方式。

实验环境:
- 操作系统：Ubuntu 24.04
- Spark 3.3.2
- Hadoop 3.3.6（伪分布式）
- MySQL 8.0
- Java 1.8.0
- Scala 2.12.15

数据集:
- 来源：UCI Online Retail Dataset
- 记录数：541909条
- 字段：订单号、商品代码、商品描述、数量、订单时间、单价、客户ID、国家

统计分析任务:
1. 各国销售额TOP10
2. 月度销售趋势
3. 商品热度TOP10（按销量）
4. 商品销售额TOP10
5. 时段销售分析（按小时）
6. 星期销售分析

项目结构:
spark-project/
├── build.sbt
├── README.md
├── lib/
│ └── mysql-connector-j-8.0.33.jar
├── python/
│ └── visualization.py
└── src/main/scala/
├── RetailAnalysisLocal.scala # 方式一：本地模式
├── RetailAnalysis.scala # 方式二：HDFS模式
└── RetailAnalysisWithMySQL.scala # 方式三：MySQL模式


运行方法:

启动HDFS
start-dfs.sh
方式一：本地模式（读取本地文件）
spark-submit --class RetailAnalysisLocal --master local[2] --driver-memory 1g target/scala-2.12/retailanalysis_2.12-1.0.jar
方式二：HDFS模式（读取HDFS分布式存储）
spark-submit --class RetailAnalysis --master local[2] --driver-memory 1g target/scala-2.12/retailanalysis_2.12-1.0.jar
方式三：MySQL模式（结果存入MySQL）
spark-submit --class RetailAnalysisWithMySQL --master local[2] --driver-memory 1g --jars lib/mysql-connector-j-8.0.33.jar target/scala-2.12/retailanalysis_2.12-1.0.jar
生成可视化图表:python3 python/visualization.py

实验结果:
清洗前：541909条
清洗后：397884条
英国销售额最高：7308391英镑
销售高峰：2011年11月
订单高峰：中午12点
销售高峰日：周四

三个大数据框架:
Spark：核心计算引擎
HDFS：分布式存储
MySQL：结果存储

作者
姓名：戴晶晶
学号：20233001542
课程：大数据编程课程设计
日期：2026年6月
