/**
 * viewOrder.js
 * 
 * Copyright 2011-2014 Sega Corporation
 */

// 配信指示書テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteGame_']").click(function () {
    var titleId = $(this).parents("tr").children().eq(0).text();
    var titleVer = $(this).parents("tr").children().eq(1).text();
    var uri = $(this).parents("tr").children().eq(2).text();
    $("#orderTitleId").text(titleId);
    $("#orderTitleVer").text(titleVer);
    $("#orderUri").text(uri);
    $("#delTitleId").val(titleId);
    $("#delTitleVer").val(titleVer);
    $("#confirmGameModal").modal('show');
});

// 国別配信指示書テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteCountry_']").click(function () {
    var titleId = $(this).parents("tr").children().eq(0).text();
    var titleVer = $(this).parents("tr").children().eq(1).text();
    var countryCode = $(this).parents("tr").children().eq(2).text();
    var uri = $(this).parents("tr").children().eq(3).text();
    $("#countryOrderTitleId").text(titleId);
    $("#countryOrderTitleVer").text(titleVer);
    $("#countryOrderCountryCode").text(countryCode);
    $("#countryOrderUri").text(uri);
    $("#deleteCountryForm #delTitleId").val(titleId);
    $("#deleteCountryForm #delTitleVer").val(titleVer);
    $("#deleteCountryForm #delCountryCode").val(countryCode);
    $("#confirmCountryModal").modal('show');
});

// 基板指示書テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteMachine_']").click(function () {
    var clientId = $(this).parents("tr").children().eq(0).text();
    var titleId = $(this).parents("tr").children().eq(1).text();
    var uri = $(this).parents("tr").children().eq(3).text();
    $("#machineOrderTitleId").text(titleId);
    $("#machineOrderClientId").text(clientId);
    $("#machineOrderUri").text(uri);
    $("#deleteMachineForm #delTitleId").val(titleId);
    $("#deleteMachineForm #delClientId").val(clientId);
    $("#confirmMachineModal").modal('show');
});