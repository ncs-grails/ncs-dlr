$(document).ready(function(){
     
    $("#buttonAdd").click(function(){

        var url = $("form[name='reportedEffort-create']").attr('action');       // /ncs-dlr/reportedEffort/create
        var assignedEffortId = $("form[name='directLaborReporting']").find("input[name='assignedEffort.id']").val();
        var data = {'assignedEffort.id': assignedEffortId}                      // [object Object]

        $("#addOrEditForm").load(url, data);
        $("#addDeleteEditCommitControls").hide();
        
        return false;

    });
     
    $("#buttonEdit").click(function(){
        
        var url = $("form[name='reportedEffort-create']").attr('action');
        $("#addOrEditForm").load(url);
        $("#addDeleteEditCommitControls").hide();
        return false;
        
     });
     
 });

function resetForm() {
    
    $("#addOrEditForm").html("");
    $("#addDeleteEditCommitControls").show();
    return false;
    
}

function reloadAssignedEffort(assignedEffortId) {
    
    var url = $("form[name='assignedEffort-show']").attr('action');
    var assignedEffortId = $("form[name='directLaborReporting']").find("input[name='assignedEffort.id']").val();
    var data = {'assignedEffort.id': assignedEffortId}

    $("#showAssignedEffortContainer").load(url, data);

}