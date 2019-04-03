<!-- Modal -->
<div class="modal fade" id="generateEmailModal" tabindex="-1"
	role="dialog" aria-labelledby="exampleModalCenterTitle"
	aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
	
	
		
	
	
	  <form id="learningMailForm">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="generateEmailModalTitle">Generate
						Personal Learning Email</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Click on send button for sending personal learning mail. <br>Else
					click on Close button.<br> <br>
					<p>Note : Please wait, It might take some time.</p>
					<div class="row">
						<div class="col-4">
							<label>Browse Excel File  </label>
						</div>
						<div class="col-8">
							<input name="excelFile" type="file" disabled="disabled" id="excelFile" accept=".xls,.xlsx">
						</div>
					</div>
				</div>

				<div class="modal-footer">
					<button id="buttonSendMail" type="submit" class="btn btn-primary">Send</button>
					<button type="button" class="btn btn-success" data-dismiss="modal">Close</button>
				</div>
			</div>
		</form>
		
		
		<!--  <form id="learningMailForm" method="POST" action="${pageContext.request.contextPath}/learning-email/file" enctype="multipart/form-data">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="generateEmailModalTitle">Generate
						Personal Learning Email</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Click on send button for sending personal learning mail. <br>Else
					click on Close button.<br> <br>
					<p>Note : Please wait, It might take some time.</p>
					<div class="custom-file">
						<label>Browse Excel File  </label>
						<input name="excelFile" type="file" id="excelFile">
					</div>
				</div>

				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">Send</button>
					<button type="button" class="btn btn-success" data-dismiss="modal">Close</button>
				</div>
			</div>
		</form>-->
		
	</div>
</div>