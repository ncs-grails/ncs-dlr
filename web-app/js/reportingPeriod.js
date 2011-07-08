$(document).ready(function(){    
     
 });
 
function saveSuccess(fieldName) {
	var newValue = $("#successMessage").html();
	var elem = "input[name='" + fieldName + "']";
	var oldValue = $(elem).val();
	$(elem).css('color', 'black');
		
	if (oldValue != newValue) {
		// alert(oldValue + ' -> ' + newValue);
		$(elem).val(newValue);
	}
	
}

function saveFail(fieldName) {
	var elem = "input[name='" + fieldName + "']";
	$(elem).css('color', 'red');
	
	$(elem).delay(3000).css('color', 'red');
	
}