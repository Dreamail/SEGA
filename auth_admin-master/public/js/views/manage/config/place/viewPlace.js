/**
 * viewPlace.js
 *
 * Copyright 2011-2014 Sega Corporation
 */

// 削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='delete_']").click(function () {
    var placeId = $(this).parents("tr").children().eq(0).text();
    var name = $(this).parents("tr").children().eq(1).text();
    var tel = $(this).parents("tr").children().eq(2).text();
    $("#delPlaceIdText").text(placeId);
    $("#delPlaceNameText").text(name);
    $("#delTelText").text(tel);
    $("#delPlaceId").val(placeId);
    $("#confirmModal").modal('show');
});