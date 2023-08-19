<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$path = "/ws/getplaylog/%s/%s";

$subGameId = "";
$pass = "";
$aimeId = "";
$start = "";
$end = "";
$offset = "";
$count = "";

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $aimeId = $_POST['aimeId'];
    $start = $_POST['start'];
    $end = $_POST['end'];
    $offset = $_POST['offset'];
    $count = $_POST['count'];

    // プレイログ取得リクエスト用のJSONを生成するために連想配列を定義します
    $param = array(
        "pass" => $pass,    // アクセスパスワード
        "param" => array(
            "start" => $start,      // 取得開始日時
            "end" => $end,          // 取得終了日時
            "offset" => $offset,    // 取得開始位置
            "count" => $count       // 取得件数
        )
    );
    // サーバにリクエストを行います（リクエストパスにはサブゲームIDとAimeIDを含めます）
    $url = $protocol."://".$host.":".$port.sprintf($path, $subGameId, $aimeId);
    $client = new JsonClient();
    $res = $client->executeRequest($url, $param); // $res->dataにはプレイログデータがセットされます
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>サンプル - プレイログ取得</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>プレイログ取得</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード<dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>AimeID</dt>
                <dd><input type="text" name="aimeId" value="<?php echo $aimeId; ?>"></dd>
                <dt>取得開始日時</dt>
                <dd><input type="text" name="start" value="<?php echo $start; ?>"></dd>
                <dt>取得終了日時</dt>
                <dd><input type="text" name="end" value="<?php echo $end; ?>"></dd>
                <dt>取得開始位置</dt>
                <dd><input type="text" name="offset" value="<?php echo $offset; ?>"></dd>
                <dt>取得件数</dt>
                <dd><input type="text" name="count" value="<?php echo $count; ?>"></dd>
                <dt>&nbsp;</dt>
                <dd><button type="submit">送信</button></dd>
            </dl>
        </form>
        <hr>
        <?php if ($_POST != null): ?>
            <?php // $res->status->codeが1以外なら失敗です ?>
            <?php if ($res->status->code != 1): ?>
                <h2>処理失敗</h2>
                <p>処理コード:<?php echo $res->status->code; ?>&nbsp;原因コード:<?php echo $res->status->subCode; ?></p>
            <?php else: ?>
                <h2>処理成功</h2>
                <table border="1">
                    <tr>
                        <th>ゲーム日時1</th>
                        <th>ゲーム日時2</th>
                        <th>店舗ID</th>
                        <th>他プレイヤー1</th>
                        <th>他プレイヤー2</th>
                        <th>他プレイヤー3</th>
                        <th>ゲームステータス0</th>
                        <th>ゲームステータス1</th>
                        <th>ゲームステータス2</th>
                        <th>ゲームステータス3</th>
                        <th>ゲームステータス4</th>
                        <th>ゲームステータス5</th>
                        <th>任意データ</th>
                    </tr>
                    <?php foreach($res->data as $val): ?>
                        <tr>
                            <td><?php echo $val->time1; ?></td>
                            <td><?php echo $val->time2; ?></td>
                            <td><?php echo $val->locationId; ?></td>
                            <td><?php echo $val->otherAimeId1; ?></td>
                            <td><?php echo $val->otherAimeId2; ?></td>
                            <td><?php echo $val->otherAimeId3; ?></td>
                            <td><?php echo $val->stat0; ?></td>
                            <td><?php echo $val->stat1; ?></td>
                            <td><?php echo $val->stat2; ?></td>
                            <td><?php echo $val->stat3; ?></td>
                            <td><?php echo $val->stat4; ?></td>
                            <td><?php echo $val->stat5; ?></td>
                            <td><?php echo $val->optionalData; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
