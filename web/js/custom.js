/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
    "use strict";
    $.ajax({
        context: this,
        url: "/admin/ajax",
        type: "POST",
        data: { "height": height, "width": width, "color": color }
    });
});

function validate() {
	var validForm = true,
	    text = $.trim($("div#rant-form-container textarea").val()), 
	    pageLoaded = $("#pageLoadTime").val(),
	    now = (new Date).getTime(),
	    limitInSeconds =  7;
		
	if((now-pageLoaded) > limitInSeconds*1000) {
	    if (!text) {
	    	$('p#error-text').text("Don't you want to say something?").slideDown(300);
	        validForm = false;
	    } else{
		    $('input[name=emotion]').val($("div.questions:visible").prop("id"));
			$('input[name=question]').val($('#legend').text());
	    }
	} else {
		$('p#error-text').text("Slow down, you can't post that quick.").slideDown(300);
		validForm = false;
	}
    if(validForm) $('#submit').attr('disabled', 'disabled');
    return validForm;
}