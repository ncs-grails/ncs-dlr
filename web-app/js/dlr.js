$(document).ready(function(){
     
    $("#buttonAdd").click(function(){
        var url = $("form[name='reportedEffort-create']").attr('action');
        $("#addOrEditForm").load(url);
        $("#showControls").hide();
        return false;
     });
     
    $("#buttonEdit").click(function(){
        var url = $("form[name='reportedEffort-create']").attr('action');
        $("#addOrEditForm").load(url);
        $("#showControls").hide();
        return false;
     });
     
 });

function resetForm() {
    
    $("#addOrEditForm").html("");
    $("#showControls").show();
    return false;
    
}