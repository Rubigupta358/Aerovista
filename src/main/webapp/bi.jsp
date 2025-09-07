 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Live Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        canvas { margin: 20px; background: #f5f5f5; padding: 10px; border-radius: 10px; }
        h2 { margin-left: 20px; }
    </style>
</head>
<body>
<h1>Company Live Dashboard</h1>

<h2>Monthly Sales</h2>
<canvas id="monthlySalesChart" width="600" height="300"></canvas>

<h2>Monthly Investment</h2>
<canvas id="monthlyInvestmentChart" width="600" height="300"></canvas>

<h2>Product ROI</h2>
<canvas id="productRoiChart" width="600" height="300"></canvas>

<h2>Feature Rating</h2>
<canvas id="featureRatingChart" width="600" height="300"></canvas>

<h2>Region Sales</h2>
<canvas id="regionSalesChart" width="600" height="300"></canvas>

<script>
let charts = {}; // all chart references

function loadChart(table, canvasId, chartType, labelKey, valueKey, chartLabel) {
    fetch('ChartData?table=' + table)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(row => row[labelKey]);
            const values = data.map(row => row[valueKey]);

            if(charts[canvasId]){
                charts[canvasId].data.labels = labels;
                charts[canvasId].data.datasets[0].data = values;
                charts[canvasId].update();
            } else {
                const ctx = document.getElementById(canvasId).getContext('2d');
                charts[canvasId] = new Chart(ctx, {
                    type: chartType,
                    data: {
                        labels: labels,
                        datasets: [{
                            label: chartLabel,
                            data: values,
                            borderColor: 'blue',
                            backgroundColor: 'rgba(0,0,255,0.2)',
                            fill: chartType === 'line' || chartType === 'bar'
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: { beginAtZero: true }
                        }
                    }
                });
            }
        })
        .catch(err => console.error('Error loading ' + table + ':', err));
}

// Initial load
loadChart('monthly_sales', 'monthlySalesChart', 'line', 'month', 'sales', 'Monthly Sales');
loadChart('monthly_investment', 'monthlyInvestmentChart', 'bar', 'month', 'investment', 'Monthly Investment');
loadChart('product_roi', 'productRoiChart', 'line', 'product', 'roi', 'Product ROI');
loadChart('feature_rating', 'featureRatingChart', 'radar', 'feature', 'rating', 'Feature Rating');
loadChart('region_sales', 'regionSalesChart', 'pie', 'region', 'sales', 'Region Sales');

// Auto-update every 10 seconds
setInterval(() => {
    loadChart('monthly_sales', 'monthlySalesChart', 'line', 'month', 'sales', 'Monthly Sales');
    loadChart('monthly_investment', 'monthlyInvestmentChart', 'bar', 'month', 'investment', 'Monthly Investment');
    loadChart('product_roi', 'productRoiChart', 'line', 'product', 'roi', 'Product ROI');
    loadChart('feature_rating', 'featureRatingChart', 'radar', 'feature', 'rating', 'Feature Rating');
    loadChart('region_sales', 'regionSalesChart', 'pie', 'region', 'sales', 'Region Sales');
}, 10000); // 10 sec
</script>

</body>
</html>
 