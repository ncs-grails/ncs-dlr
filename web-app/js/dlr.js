$(document).ready(function(){
    
    // ADD button to add effort is pressed
    $("#buttonAdd").click(function(){

        // get url to go to
        var url = $("form[name='reportedEffort-create']").attr('action');       // /ncs-dlr/reportedEffort/create
        alert("(#buttonAdd).click(function().url = " + url)
        
        // get asignedEffort.id from main.show
        var assignedEffortId = $("form[name='main']").find("input[name='assignedEffort.id']").val();
        alert("(#buttonAdd).click(function().assignedEffortId = " + assignedEffortId)
                
        // get data to load
        var data = {'assignedEffort.id': assignedEffortId}                      // [object Object]
        alert("(#buttonAdd).click(function().data = " + data)

        $("#addOrEditEffortForm").load(url, data);
        $("#addDeleteEditCommitControls").hide();
        
        return false;

    });
     
    // EDIT button to edit effort is pressed
    $("#buttonEdit").click(function(){
        
        var url = $("form[name='reportedEffort-create']").attr('action');
        //alert("(#buttonEdit).click(function().url = " + url)

        $("#addOrEditEffortForm").load(url);
        $("#addDeleteEditCommitControls").hide();
        
        return false;
        
     });

 });

// reload main.show page
function loadAssignedEffort() {

    alert("in function loadAssignedEffort");

    //var url = $("form[name='main']").attr('action');
    var url = $("form[name='main']").attr('action');
    alert("function loadAssignedEffort.url = " + url);

    var assignedEffortId = $("form[name='reportedEffort-create']").find("input[name='assignedEffort.id']").val();
    alert("function loadAssignedEffort.assignedEffortId = " + assignedEffortId);

    var data = {'assignedEffort.id': assignedEffortId};
    alert("function loadAssignedEffort.data = " + data);

    //display (assignedEffort.show): 1) message boxes: ASSIGNED, REPORTED COMMITED message and 2) EFFORT REPORTED so far
    $("#showAssignedEffortContainer").load(url, data);

    return false;
    
}

// reset main.show, when CANCEL button is pressed
function resetForm() {
    
    alert("in function resetForm()");
    
    $("#addOrEditEffortForm").html("");
    $("#addDeleteEditCommitControls").show();

    return false;
    
}

