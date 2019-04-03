<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- CSS -->
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">

<title>Mailer Template</title>
</head>
<body id="page-top">
	<nav class="navbar red-color">
		<a class="navbar-brand" href="#" style="margin-left: 150px">Learning Mailer Template</a>

		<ul class="nav nav-pills" style="margin-right: 150px">
			<li class="nav-item"><a class="nav-link" href="/">Home</a></li>
			<li class="nav-item"><a class="nav-link" href="#"
				data-toggle="modal" data-target="#mergeExcelModal">Merge
					Classroom & Online Courses</a></li>
			<li class="nav-item"><a class="nav-link" href="#"
				data-toggle="modal" data-target="#generateEmailModal">Send Learning Emails</a></li>
		</ul>
	</nav>
		<!-- Header -->
		<header>
			<div class="container">
				<div class="slider-container">
					<div class="intro-text">
						<div class="intro-lead-in">Welcome To Learning Mail POC</div>
						<div style="font-size : 30px; padding-bottom : 40px;">#PulwamaAttack : Lets pray almighty to shower strength to withstand the great losses to you, your family and the nation. People will unit in this hour of crisis and should become more stronger to eliminate PAKISTAN and its terrorism!</div> 
						<!--  <div class="intro-heading">In the space between next and now is how.</div>-->
						<a href="#about" class="page-scroll btn btn-xl">Tell Me More</a>
					</div>
				</div>
			</div>
		</header>
		<!-- Modal -->
		<jsp:include page="learning-email-modal.jsp" />
		<jsp:include page="merge-excel-modal.jsp" />
		<jsp:include page="toast-message.jsp" />
		<section id="about" class="light-bg">
			<div class="container">
				<div class="row">
					<div class="col-lg-12 text-center">
						<div class="section-title">
							<h2>ABOUT</h2>
							<p>A small POC to read classroom and online coureses data and send mail to each user with their total learning and certification through out the year. Also we can merge classroom session and online courses data given in two sepeate seet. It gives a consolidate excel file.</p>
						</div>
					</div>
				</div>
			</div>
			<!-- /.container -->
		</section>
	
		<footer class="red-color">
			<div class="container text-center">
				<p>Designed by Maneesh</a></p>
			</div>
		</footer>


	

	<!-- JavaScript -->
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script>
		$(document).ready(function() {
			
			$('#learningMailForm').on('submit', function(e) {
				//var excelFile = $("#excelFile").val();
				//if (excelFile  === '') {
				//	document.getElementById("toastBodyMessage").innerHTML = "Please choose excel file before click on send button.";
				//	$('#divToast').toast('show');
				//	return false;
			    //}
				
				disableSendButton();
		        
		        $.ajax({//http://localhost:2019/learning-email/file
		            url : '/learning-email/file',
		            contentType: "application/json;charset=utf-8",
		            type: "POST",
		            data: JSON.stringify({ fullPath: 'D:\\Mailer\\Data_UAT Sample 2.xlsx'}),
		            complete: function (data) {
		            	console.log(data.responseText);
		            	document.getElementById("toastBodyMessage").innerHTML = data.responseText;
		            	$('#generateEmailModal').modal('hide');
		            	$('#divToastMessage').toast('show');
		            	
		            }
		        });
		        e.preventDefault();
		    });
			
			$('#mergeExcelForm').on('submit', function(e) {
				disableMergeButton();
		        $.ajax({//http://localhost:2019/merge-excel/files
		            url : '/merge-excel/files',
		            contentType: "application/json;charset=utf-8",
		            type: "POST",
		            data: JSON.stringify({ fullPath: 'D:\\Mailer\\classroom_data.xlsx', fullPath2: 'D:\\Mailer\\online_courses.xlsx'}),
		            complete: function (data) {
		            	console.log(data.responseText);
		            	document.getElementById("toastBodyMessage").innerHTML = data.responseText;
		            	$('#mergeExcelModal').modal('hide');
		            	$('#divToastMessage').toast('show');
		            }
		        });
		        e.preventDefault();
		    });
			
						
			
			$("#generateEmailModal").on("hidden.bs.modal", function(){
				$("#buttonSendMail").html('Send');
				$("#buttonSendMail").prop("disabled", false);
		        
			});
			
			$("#mergeExcelModal").on("hidden.bs.modal", function(){
				$("#buttonMergeFile").html('Merge');
				$("#buttonMergeFile").prop("disabled", false);
				
			});
		});	
		
		function disableSendButton() {
			$("#buttonSendMail").html('Please wait..');
			$("#buttonSendMail").prop("disabled", true);
		}
		
		function disableMergeButton() {
			$("#buttonMergeFile").html('Please wait..');
			$("#buttonMergeFile").prop("disabled", true);
		}
		

	</script>
</body>
</html>