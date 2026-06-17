import pandas as pd
import matplotlib.pyplot as plt

print("=" * 50)
print("电商数据统计可视化")
print("=" * 50)

# 1. 各国销售额 TOP10 柱状图
print("1. 生成各国销售额柱状图...")
country_data = {
    'Country': ['United Kingdom', 'Netherlands', 'EIRE', 'Germany', 'France', 
                'Australia', 'Spain', 'Switzerland', 'Belgium', 'Sweden'],
    'TotalSales': [7308391, 285446, 265546, 228867, 209024, 
                   138521, 61577, 56444, 41196, 38378]
}
df_country = pd.DataFrame(country_data)

plt.figure(figsize=(12, 6))
plt.bar(df_country['Country'], df_country['TotalSales'], color='steelblue')
plt.xlabel('Country')
plt.ylabel('Total Sales (£)')
plt.title('Top 10 Countries by Sales')
plt.xticks(rotation=45, ha='right')
plt.tight_layout()
plt.savefig('/home/djj/country_sales.png', dpi=150)
print("   -> 已保存: /home/djj/country_sales.png")

# 2. 月度销售趋势折线图
print("2. 生成月度销售趋势折线图...")
monthly_data = {
    'Month': ['2010-12', '2011-01', '2011-02', '2011-03', '2011-04', 
              '2011-05', '2011-06', '2011-07', '2011-08', '2011-09', 
              '2011-10', '2011-11', '2011-12'],
    'Sales': [572714, 569445, 447137, 595501, 469200, 
              678595, 661214, 600091, 645344, 952838, 
              1039319, 1161817, 518193]
}
df_monthly = pd.DataFrame(monthly_data)

plt.figure(figsize=(12, 5))
plt.plot(df_monthly['Month'], df_monthly['Sales'], marker='o', linewidth=2, markersize=6, color='coral')
plt.xlabel('Month')
plt.ylabel('Sales (£)')
plt.title('Monthly Sales Trend')
plt.xticks(rotation=45)
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.savefig('/home/djj/monthly_sales.png', dpi=150)
print("   -> 已保存: /home/djj/monthly_sales.png")

# 3. 商品热度 TOP10 水平柱状图
print("3. 生成商品热度水平柱状图...")
product_data = {
    'Product': ['PAPER CRAFT', 'MEDIUM CERAMIC', 'WORLD WAR 2', 'JUMBO BAG', 
                'WHITE HANGING', 'ASSORTED COLOUR', 'PACK OF 72', 
                'POPCORN HOLDER', 'RABBIT LIGHT', 'MINI PAINT'],
    'Quantity': [80995, 77916, 54415, 46181, 36725, 35362, 33693, 30931, 27202, 26076]
}
df_product = pd.DataFrame(product_data)
df_product = df_product.sort_values('Quantity', ascending=True)

plt.figure(figsize=(10, 6))
plt.barh(df_product['Product'], df_product['Quantity'], color='green')
plt.xlabel('Total Quantity Sold')
plt.ylabel('Product')
plt.title('Top 10 Products by Quantity')
plt.tight_layout()
plt.savefig('/home/djj/top_products.png', dpi=150)
print("   -> 已保存: /home/djj/top_products.png")

# 4. 时段销售分析柱状图
print("4. 生成时段销售分析图...")
hourly_data = {
    'Hour': [6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
    'OrderCount': [1, 379, 8690, 21944, 37997, 49084, 72065, 64026, 54118, 45369, 24089, 13071, 2928, 3321, 802]
}
df_hourly = pd.DataFrame(hourly_data)

plt.figure(figsize=(12, 5))
plt.bar(df_hourly['Hour'], df_hourly['OrderCount'], color='purple')
plt.xlabel('Hour of Day')
plt.ylabel('Number of Orders')
plt.title('Order Distribution by Hour')
plt.xticks(range(6, 21))
plt.grid(True, alpha=0.3, axis='y')
plt.tight_layout()
plt.savefig('/home/djj/hourly_sales.png', dpi=150)
print("   -> 已保存: /home/djj/hourly_sales.png")

# 5. 星期销售分析饼图
print("5. 生成星期销售分析饼图...")
weekday_data = {
    'Weekday': ['Thursday', 'Tuesday', 'Wednesday', 'Friday', 'Monday', 'Sunday'],
    'Sales': [1976859, 1700635, 1588336, 1485917, 1367146, 792514]
}
df_weekday = pd.DataFrame(weekday_data)

plt.figure(figsize=(8, 8))
colors = ['#ff9999', '#66b3ff', '#99ff99', '#ffcc99', '#ff99cc', '#c2c2f0']
plt.pie(df_weekday['Sales'], labels=df_weekday['Weekday'], autopct='%1.1f%%', colors=colors, startangle=90)
plt.title('Sales Distribution by Weekday')
plt.axis('equal')
plt.tight_layout()
plt.savefig('/home/djj/weekday_sales.png', dpi=150)
print("   -> 已保存: /home/djj/weekday_sales.png")

print("=" * 50)
print("可视化完成！图片保存在 /home/djj/ 目录下")
print("文件列表:")
print("  - country_sales.png")
print("  - monthly_sales.png")
print("  - top_products.png")
print("  - hourly_sales.png")
print("  - weekday_sales.png")
print("=" * 50)
