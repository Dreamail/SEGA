<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$path = "/ws/getcollection/%s";

$subGameId = "";
$pass = "";
$collectionId = "";
$current = false;
$offset = "";
$count = "";

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $collectionId = $_POST['collectionId'];
    if (isset($_POST['current']) && $_POST['current'] == "true") {
        $current = true;
    }
    $offset = $_POST['offset'];
    $count = $_POST['count'];

    // 集計データ取得リクエスト用のJSONを生成するために連想配列を定義します
    $param = array(
        "pass" => $pass,    // アクセスパスワード
        "param" => array(
            "collectionId" => $collectionId,    // 集計ID
            "current" => $current,              // 集計中データ取得フラグ
            "offset" => $offset,                // 取得開始位置
            "count" => $count                   // 取得件数
        )
    );
    // サーバにリクエストを行います（リクエストパスにはサブゲームIDを含めます）
    $url = $protocol."://".$host.":".$port.sprintf($path, $subGameId);
    $client = new JsonClient();
    $res = $client->executeRequest($url, $param); // $res->dataには集計データがセットされます
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>サンプル - 集計データ取得</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>集計データ取得</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード<dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>集計ID</dt>
                <dd><input type="text" name="collectionId" value="<?php echo $collectionId; ?>"></dd>
                <dt>現集計対象</dt>
                <dd>
                <?php if ($current): ?>
                    <input type="checkbox" name="current" value="true" checked="checked">
                <?php else: ?>
                    <input type="checkbox" name="current" value="true">
                <?php endif; ?>
                </dd>
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
                        <th>集計ID</th>
                        <th>ユーザ定義キー</th>
                        <th>v0</th>
                        <th>v1</th>
                        <th>v2</th>
                        <th>v3</th>
                        <th>v4</th>
                        <th>v5</th>
                        <th>v6</th>
                        <th>v7</th>
                    </tr>
                    <?php foreach($res->data as $val): ?>
                        <tr>
                            <td><?php echo $val->collectionId; ?></td>
                            <td><?php echo $val->key; ?></td>
                            <td><?php echo $val->value0; ?></td>
                            <td><?php echo $val->value1; ?></td>
                            <td><?php echo $val->value2; ?></td>
                            <td><?php echo $val->value3; ?></td>
                            <td><?php echo $val->value4; ?></td>
                            <td><?php echo $val->value5; ?></td>
                            <td><?php echo $val->value6; ?></td>
                            <td><?php echo $val->value7; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
