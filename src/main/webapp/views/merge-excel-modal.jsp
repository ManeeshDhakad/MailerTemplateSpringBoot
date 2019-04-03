<!-- Modal -->
<div class="modal fade" id="mergeExcelModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<form id="mergeExcelForm">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="mergeExcelModalTitle">Merge
						Classroom and online courses Data</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					Click on merge button for merging both excel files. <br>Else
					click on Close button.<br> <br>
					
					<p>Note : Please wait, It might take some time.</p>
					
					<div class="row">
						<div class="col-4">
							<label>Browse Excel File1  </label>
						</div>
						<div class="col-8">
							<input name="excelFile1" type="file" disabled="disabled" id="excelFile1" accept=".xls,.xlsx">
						</div>
					</div>
					<div class="row">
						<div class="col-4">
							<label>Browse Excel File2  </label>
						</div>
						<div class="col-8">
							<input name="excelFile2" type="file" disabled="disabled" id="excelFile2" accept=".xls,.xlsx">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="buttonMergeFile" type="submit" class="btn btn-primary">Merge</button>
					<button type="button" class="btn btn-success" data-dismiss="modal">Close</button>
				</div>
			</div>
		</form>
	</div>
</div>