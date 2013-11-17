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