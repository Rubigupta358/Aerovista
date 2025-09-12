
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="navbar.jsp"%>
<%@ include file="chatbot.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>Live Business Insights</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
canvas {
	margin: 20px;
	background: #f5f5f5;
	padding: 10px;
	border-radius: 10px;
}

h2 {
	margin-left: 20px;
}
</style>
<link rel="stylesheet" type="text/css" href="style.css">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap JS -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<h4 style="text-align: center">Company Live Dashboard</h4>
	<iframe title="COMPANY_PROJECTDATA" width="100%" height="100%"
		src="https://app.powerbi.com/reportEmbed?reportId=98c07dd6-d942-4dee-8665-d5fb5ccb2d9c&autoAuth=true&ctid=2db2b3e8-b382-4ee9-b046-c86750ab2a99"
		frameborder="0" allowFullScreen="true"></iframe>

	<!--   

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
</script>  -->

</body>
</html>
