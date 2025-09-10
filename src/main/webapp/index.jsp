  <%@ include file="chatbot.jsp" %> 
 <!DOCTYPE html>
<html>
<head>
    <title>AeroVista Technologies Dashboard</title>
    <link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
<div>    <a href="index.jsp">        <img class="mylogo" src="aerovista-logo.png" alt="AeroVista Logo" 
             style="height:60px; object-fit:contain; display:block;  margin:0 auto 15px auto; background: transparent;    border: none;    mix-blend-mode: multiply;">
    </a></div>
    
    <div class="container my-5 text-center">
    <h1 class="mb-4">Welcome to AeroVista Technologies</h1>
</div>

        
        <!-- Cards Row -->
        <div class="row g-4 justify-content-center">

            <!-- Products Card -->
            <div class="col-lg-3 col-md-4 col-sm-6">
                <div class="card card-module text-center">
                    <a href="productForm.jsp">
                        <div class="card-body">
                            <h3 class="card-title">Products</h3>
                            <p class="card-text">Add and manage products</p>
                        </div>
                    </a>
                </div>
            </div>

            <!-- Sales Card -->
            <div class="col-lg-3 col-md-4 col-sm-6">
                <div class="card card-module text-center">
                    <a href="sales.jsp">
                        <div class="card-body">
                            <h3 class="card-title">Sales</h3>
                            <p class="card-text">Record and view sales</p>
                        </div>
                    </a>
                </div>
            </div>

            <!-- Feedback Card -->
            <div class="col-lg-3 col-md-4 col-sm-6">
                <div class="card card-module text-center">
                    <a href="feedback.jsp">
                        <div class="card-body">
                            <h3 class="card-title">Feedback</h3>
                            <p class="card-text">Check user feedback</p>
                        </div>
                    </a>
                </div>
            </div>

            <!-- Investment Card -->
            <div class="col-lg-3 col-md-4 col-sm-6">
                <div class="card card-module text-center">
                    <a href="investment.jsp">
                        <div class="card-body">
                            <h3 class="card-title">Investment</h3>
                            <p class="card-text">Track investments</p>
                        </div>
                    </a>
                </div>
            </div>

            <!-- Insights Card -->
            <div class="col-lg-3 col-md-4 col-sm-6">
                <div class="card card-module text-center">
                    <a href="bi.jsp">
                        <div class="card-body">
                            <h3 class="card-title">Insights</h3>
                            <p class="card-text">Business Insights</p>
                        </div>
                    </a>
                </div>
            </div>

        </div>
    </div>
           
            
    
</body>
</html>
 