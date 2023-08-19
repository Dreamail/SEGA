/**
 * admin.js
 *
 * Copyright 2012 Sega Corporation
 */

var displayState = new Object();

// event handler on document ready
$(document).ready(function() {
    loadDisplayState();
    // affect menu display
    if (displayState != null && displayState.menu == "hide") {
        hideMenu();
    } else {
        showMenu();
    }
    // enable bootstrap tooltip (admin-ext)
    $("[rel=tooltip]").exttooltip({html: false});
    // enable bootstrap popover
    $("[rel=popover]").popover();
    // enable bootstrap popover (admin-ext)
    $("[rel=popover_table]").popover_table();
    // enable bootstrap modal
    $("[rel=modal]").modal();
    // change behavior of input type="reset" to reset selected(inputed) init value
    $(":reset").click(function() {
        $(this.form).find(":input").each(function() {
            switch (this.type) {
            case "password":
            case "select-multiple":
            case "select-one":
            case "text":
            case "textarea":
                $(this).val("");
                break;
            case "checkbox":
            case "radio":
                this.checked = false;
                break;
            case "hidden":
                break;
            }
        });
        return false;
    });
});
// event handler for menu accordion
$('#accordionLogSearch').on('show', function () {
      $("#accordionLogSearchIcon").removeClass("icon-plus-sign");
      $("#accordionLogSearchIcon").addClass("icon-minus-sign");
});
$('#accordionLogSearch').on('hide', function () {
    $("#accordionLogSearchIcon").removeClass("icon-minus-sign");
    $("#accordionLogSearchIcon").addClass("icon-plus-sign");
});
$('#accordionViewSearch').on('show', function () {
    $("#accordionViewSearchIcon").removeClass("icon-plus-sign");
    $("#accordionViewSearchIcon").addClass("icon-minus-sign");
});
$('#accordionViewSearch').on('hide', function () {
    $("#accordionViewSearchIcon").removeClass("icon-minus-sign");
    $("#accordionViewSearchIcon").addClass("icon-plus-sign");
});
$('#accordionDataSearch').on('show', function () {
    $("#accordionDataSearchIcon").removeClass("icon-plus-sign");
    $("#accordionDataSearchIcon").addClass("icon-minus-sign");
});
$('#accordionDataSearch').on('hide', function () {
  $("#accordionDataSearchIcon").removeClass("icon-minus-sign");
  $("#accordionDataSearchIcon").addClass("icon-plus-sign");
});
$("#accordionDownloadorder").on("show", function () {
    $("#accordionDownloadorderIcon").removeClass("icon-plus-sign");
    $("#accordionDownloadorderIcon").addClass("icon-minus-sign");
});
$("#accordionDownloadorder").on("hide", function () {
  $("#accordionDownloadorderIcon").removeClass("icon-minus-sign");
  $("#accordionDownloadorderIcon").addClass("icon-plus-sign");
});
$("#accordionDownloadorderNu").on("show", function () {
    $("#accordionDownloadorderNuIcon").removeClass("icon-plus-sign");
    $("#accordionDownloadorderNuIcon").addClass("icon-minus-sign");
});
$("#accordionDownloadorderNu").on("hide", function () {
  $("#accordionDownloadorderNuIcon").removeClass("icon-minus-sign");
  $("#accordionDownloadorderNuIcon").addClass("icon-plus-sign");
});
$("#accordionLoaderState").on("show", function () {
    $("#accordionLoaderStateIcon").removeClass("icon-plus-sign");
    $("#accordionLoaderStateIcon").addClass("icon-minus-sign");
});
$("#accordionLoaderState").on("hide", function () {
  $("#accordionLoaderStateIcon").removeClass("icon-minus-sign");
  $("#accordionLoaderStateIcon").addClass("icon-plus-sign");
});
$("#accordionDeliverReport").on("show", function () {
    $("#accordionDeliverReportIcon").removeClass("icon-plus-sign");
    $("#accordionDeliverReportIcon").addClass("icon-minus-sign");
});
$("#accordionDeliverReport").on("hide", function () {
  $("#accordionDeliverReportIcon").removeClass("icon-minus-sign");
  $("#accordionDeliverReportIcon").addClass("icon-plus-sign");
});
$("#accordionDataManage").on("show", function () {
    $("#accordionDataManageIcon").removeClass("icon-plus-sign");
    $("#accordionDataManageIcon").addClass("icon-minus-sign");
});
$("#accordionDataManage").on("hide", function () {
  $("#accordionDataManageIcon").removeClass("icon-minus-sign");
  $("#accordionDataManageIcon").addClass("icon-plus-sign");
});
$("#accordionRewriteResponse").on("show", function () {
    $("#accordionRewriteResponseIcon").removeClass("icon-plus-sign");
    $("#accordionRewriteResponseIcon").addClass("icon-minus-sign");
});
$("#accordionRewriteResponse").on("hide", function () {
  $("#accordionRewriteResponseIcon").removeClass("icon-minus-sign");
  $("#accordionRewriteResponseIcon").addClass("icon-plus-sign");
});
$("#accordionAdminManage").on("show", function () {
    $("#accordionAdminManageIcon").removeClass("icon-plus-sign");
    $("#accordionAdminManageIcon").addClass("icon-minus-sign");
});
$("#accordionAdminManage").on("hide", function () {
  $("#accordionAdminManageIcon").removeClass("icon-minus-sign");
  $("#accordionAdminManageIcon").addClass("icon-plus-sign");
});
$("#accordionDeliverAdmin").on("show", function () {
    $("#accordionDeliverAdminIcon").removeClass("icon-plus-sign");
    $("#accordionDeliverAdminIcon").addClass("icon-minus-sign");
});
$("#accordionDeliverAdmin").on("hide", function () {
    $("#accordionDeliverAdminIcon").removeClass("icon-minus-sign");
    $("#accordionDeliverAdminIcon").addClass("icon-plus-sign");
});
// event handler for search form accordion
$("#searchFormBody").on("show", function () {
    $("#searchFormIcon").removeClass("icon-plus-sign");
    $("#searchFormIcon").addClass("icon-minus-sign");
    $("#hideSearchForm").val(false);
});
$("#searchFormBody").on("hide", function () {
    $("#searchFormIcon").removeClass("icon-minus-sign");
    $("#searchFormIcon").addClass("icon-plus-sign");
    $("#hideSearchForm").val(true);
});
// event handler for upload form accordion
$("#uploadFormBody").on("show", function () {
    $("#uploadFormIcon").removeClass("icon-plus-sign");
    $("#uploadFormIcon").addClass("icon-minus-sign");
});
$("#uploadFormBody").on("hide", function () {
    $("#uploadFormIcon").removeClass("icon-minus-sign");
    $("#uploadFormIcon").addClass("icon-plus-sign");
});
// event handler for basic info accordion
$("#basicInfoBody").on("show", function () {
    $("#basicInfoIcon").removeClass("icon-plus-sign");
    $("#basicInfoIcon").addClass("icon-minus-sign");
});
$("#basicInfoBody").on("hide", function () {
    $("#basicInfoIcon").removeClass("icon-minus-sign");
    $("#basicInfoIcon").addClass("icon-plus-sign");
});
// event handler for machine form tab
$("#searchMachineFormTab a").click(function (e) {
    e.preventDefault();
    $(this).tab("show");
});
//event handler for register order form tab
$("#registerOrderFormTab a").click(function (e) {
    e.preventDefault();
    $(this).tab("show");
});
// event handler for deliver report form tab
$("#searchDeliverReportFormTab a").click(function (e) {
    e.preventDefault();
    $(this).tab("show");
});
//event handler for register rewrite form tab
$("#registerRewriteFormTab a").click(function (e) {
    e.preventDefault();
    $(this).tab("show");
});
//event handler for display menu link
$("#displayMenuOn").click(function() {
    showMenu();
    changeDisplayState("menu", "show");
});
$("#displayMenuOff").click(function() {
    hideMenu();
    changeDisplayState("menu", "hide");
});
//event handler for login form
$("#loginForm").submit(function() {
    removeDisplayState();
});

/**
 * Load display state from cookie
 * @param name
 * @returns
 */
function loadDisplayState() {
    var displayStateStr = $.cookie("displaystate");
    if (displayStateStr == null) {
        return;
    }
    var elemSplit = displayStateStr.split(";");
    for (var i = 0; i < elemSplit.length; i++) {
        var keyValue = elemSplit[i].split(":");
        displayState[keyValue[0]] = keyValue[1];
    }
}

/**
 * Change specified display state and save in cookie
 * @param name
 * @param value
 */
function changeDisplayState(name, value) {
    displayState[name] = value;
    var str = "";
    for (var key in displayState) {
        if (str.length > 0) {
            str += ";";
        }
        str += key + ":" + displayState[key];
    }
    $.cookie("displaystate", str, {path: "/"});
}

/**
 * Remove all display state from cookie
 */
function removeDisplayState() {
    $.cookie("displaystate", null, {path: "/"});
}

/**
 * Show menu
 */
function showMenu() {
    $("#menu").toggle(true);
    $("#content").addClass("span10");
    $("#displayMenuOff").parent().removeClass("active");
    $("#displayMenuOn").parent().addClass("active");
}

/**
 * Hide menu
 */
function hideMenu() {
    $("#menu").toggle(false);
    $("#content").removeClass("span10");
    $("#displayMenuOn").parent().removeClass("active");
    $("#displayMenuOff").parent().addClass("active");
}

$("#checkfile").click(function () {
    var file = $("#file").get(0);
    if(file.files.length == 0){
        alert("現在ファイルは選択されていません");
    } else {
        var list = "";
        var fileList = file.files;
        for(var i=0; i < fileList.length; i++){
            list += fileList[i].name;
        }
        var res = confirm(list + "の取り込みを行いますか？");
        if( res == true ) {
            // OKなら移動
            document.placeimport.submit()
        }
    }
});
