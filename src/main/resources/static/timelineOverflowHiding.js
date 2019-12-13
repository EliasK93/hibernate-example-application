$(document).ready(function(){
    var childCount = 0;
    var totalChildHeight = 0;
    $("#company-timeline").children().each(function() {
        childCount++;
        totalChildHeight += $(this).outerHeight();
        if(totalChildHeight > 350 && childCount > 0) {
            $(this).hide();
        }
    });
});

$(document).ready(function(){
    var childCount = 0;
    var totalChildHeight = 0;
    $("#product-timeline").children().each(function() {
        childCount++;
        totalChildHeight += $(this).outerHeight();
        if(totalChildHeight > 350 && childCount > 0) {
            $(this).hide();
        }
    });
});