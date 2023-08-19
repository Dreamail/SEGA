/**
 * viewUser.js
 *
 * Copyright 2011-2014 Sega Corporation
 */

// 凍結および凍結解除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='freeze_']").click(function () {
    var userId = $(this).parents("tr").children().eq(2).text();
    var roleName = $(this).parents("tr").children().eq(3).text();
    $("#freUserIdText").text(userId);
    $("#freRoleNameText").text(roleName);
    $("#freUserId").val(userId);
    if ($(this).val() == 1) {
        $("[id^='modalFreeze']").css("display", "inline");
        $("[id^='modalRelease']").css("display", "none");
    } else {
        $("[id^='modalRelease']").css("display", "inline");
        $("[id^='modalFreeze']").css("display", "none");
    }
    $("#freInvalided").val($(this).val());
    $("#freezeConfirmModal").modal('show');
});

// 削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='delete_']").click(function () {
    var userId = $(this).parents("tr").children().eq(2).text();
    var roleName = $(this).parents("tr").children().eq(3).text();
    $("#delUserIdText").text(userId);
    $("#delRoleNameText").text(roleName);
    $("#delUserId").val(userId);
    $("#deleteConfirmModal").modal('show');
});