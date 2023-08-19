/**
 * viewOrder.js
 *
 * Copyright 2011-2014 Sega Corporation
 */

// 配信指示書テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteGame_']").click(function () {
    var type = $(this).val();
    var titleId = $(this).parents("tr").children().eq(0).text();
    var titleVer = $(this).parents("tr").children().eq(1).text();
    var uri = $(this).parents("tr").children().eq(3).text();
    $("#orderTitleId").text(titleId);
    $("#orderTitleVer").text(titleVer);
    if (type == 1) {
        $("#orderImageTypeApp").css("display", "none");
        $("#orderImageTypeOpt").css("display", "inline");
    } else {
        $("#orderImageTypeOpt").css("display", "none");
        $("#orderImageTypeApp").css("display", "inline");
    }
    $("#orderUri").text(uri);
    $("#delTitleId").val(titleId);
    $("#delTitleVer").val(titleVer);
    $("#delImageType").val(type);
    $("#confirmGameModal").modal('show');
});

//国別配信指示書テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteCountry_']").click(function () {
    var type = $(this).val();
    var titleId = $(this).parents("tr").children().eq(0).text();
    var titleVer = $(this).parents("tr").children().eq(1).text();
    var countryCode = $(this).parents("tr").children().eq(2).text();
    var uri = $(this).parents("tr").children().eq(4).text();
    $("#countryOrderTitleId").text(titleId);
    $("#countryOrderTitleVer").text(titleVer);
    $("#countryOrderCountryCode").text(countryCode);
    if (type == 1) {
        $("#countryOrderImageTypeApp").css("display", "none");
        $("#countryOrderImageTypeOpt").css("display", "inline");
    } else {
        $("#countryOrderImageTypeOpt").css("display", "none");
        $("#countryOrderImageTypeApp").css("display", "inline");
    }
    $("#countryOrderUri").text(uri);
    $("#deleteCountryForm #delTitleId").val(titleId);
    $("#deleteCountryForm #delTitleVer").val(titleVer);
    $("#deleteCountryForm #delCountryCode").val(countryCode);
    $("#deleteCountryForm #delImageType").val(type);
    $("#confirmCountryModal").modal('show');
});

//基板指示書テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteMachine_']").click(function () {
    var type = $(this).val();
    var titleId = $(this).parents("tr").children().eq(1).text();
    var clientId = $(this).parents("tr").children().eq(0).text();
    var uri = $(this).parents("tr").children().eq(4).text();
    $("#machineOrderTitleId").text(titleId);
    $("#machineOrderClientId").text(clientId);
    if (type == 1) {
        $("#machineOrderImageTypeApp").css("display", "none");
        $("#machineOrderImageTypeOpt").css("display", "inline");
    } else {
        $("#machineOrderImageTypeOpt").css("display", "none");
        $("#machineOrderImageTypeApp").css("display", "inline");
    }
    $("#machineOrderUri").text(uri);
    $("#deleteMachineForm #delTitleId").val(titleId);
    $("#deleteMachineForm #delClientId").val(clientId);
    $("#deleteMachineForm #delImageType").val(type);
    $("#confirmMachineModal").modal('show');
});