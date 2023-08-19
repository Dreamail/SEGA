/**
 * viewRewrite.js
 * 
 * Copyright 2011-2015 Sega Corporation
 */

// ゲーム別テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteGame_']").click(function () {
    var titleId = $(this).parents("tr").children().eq(0).text();
    var titleVer = $(this).parents("tr").children().eq(1).text();
    var year = $(this).parents("tr").children().eq(2).text();
    var month = $(this).parents("tr").children().eq(3).text();
    var day = $(this).parents("tr").children().eq(4).text();
    var hour = $(this).parents("tr").children().eq(2).text();
    var minute = $(this).parents("tr").children().eq(2).text();
    var second = $(this).parents("tr").children().eq(2).text();
    $("#rewriteTitleId").text(titleId);
    $("#rewriteTitleVer").text(titleVer);
    $("#rewriteYear").text(year);
    $("#rewriteMonth").text(month);
    $("#rewriteDay").text(day);
    $("#rewriteHour").text(hour);
    $("#rewriteMinute").text(minute);
    $("#rewriteSecond").text(second);
    $("#delTitleId").val(titleId);
    $("#delTitleVer").val(titleVer);
    $("#confirmGameModal").modal('show');
});

// 基板別テーブルにて削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='deleteMachine_']").click(function () {
    var clientId = $(this).parents("tr").children().eq(0).text();
    var year = $(this).parents("tr").children().eq(1).text();
    var month = $(this).parents("tr").children().eq(2).text();
    var day = $(this).parents("tr").children().eq(3).text();
    var hour = $(this).parents("tr").children().eq(4).text();
    var minute = $(this).parents("tr").children().eq(5).text();
    var second = $(this).parents("tr").children().eq(6).text();
    $("#machineRewriteClientId").text(clientId);
    $("#machineRewriteYear").text(year);
    $("#machineRewriteMonth").text(month);
    $("#machineRewriteDay").text(day);
    $("#machineRewriteHour").text(hour);
    $("#machineRewriteMinute").text(minute);
    $("#machineRewriteSecond").text(second);
    $("#deleteMachineForm #delClientId").val(clientId);
    $("#confirmMachineModal").modal('show');
});