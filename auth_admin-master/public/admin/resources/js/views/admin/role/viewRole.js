/**
 * viewRole.js
 *
 * Copyright 2011-2014 Sega Corporation
 */

// 削除ボタン押下でモーダルダイアログを表示するためのイベントハンドラ
$("[id^='delete_']").click(function () {
    var roleId = $(this).val();
    var id = $(this).parents("tr").children().eq(0).text();
    var exp = $(this).parents("tr").children().eq(1).text();
    $("#delRoleId").text(id);
    $("#delRoleExp").text(exp);
    $("#droleId").val(roleId);
    $("#deleteConfirmModal").modal('show');
});
