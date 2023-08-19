<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$path = "/ws/getranking/%s";

$subGameId = "";
$pass = "";
$rankingId = "";
$current = false;
$count = "";

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $rankingId = $_POST['rankingId'];
    if (isset($_POST['current']) && $_POST['current'] == "true") {
        $current = true;
    }
    $count = $_POST['count'];

    // ランキング取得リクエスト用のJSONを生成するために連想配列を定義します
    $param = array(
        "pass" => $pass,    // アクセスパスワード
        "param" => array(
            "rankingId" => $rankingId,  // ランキングID
            "current" => $current,      // 現ランキング取得フラグ
            "count" => $count           // 取得件数
        )
    );
    // サーバにリクエストを行います（リクエストパスにはサブゲームIDを含めます）
    $url = $protocol."://".$host.":".$port.sprintf($path, $subGameId);
    $client = new JsonClient();
    $res = $client->executeRequest($url, $param); // $res->dataにはランキングデータがセットされます
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>サンプル - ランキング取得</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>ランキング取得</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード<dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>ランキングID</dt>
                <dd><input type="text" name="rankingId" value="<?php echo $rankingId; ?>"></dd>
                <dt>現ランキング対象:</dt>
                <dd>
                <?php if ($current): ?>
                    <input type="checkbox" name="current" value="true" checked="checked">
                <?php else: ?>
                    <input type="checkbox" name="current" value="true">
                <?php endif; ?>
                </dd>
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
                        <th>ランキングID</th>
                        <th>AimeID</th>
                        <th>プレイヤー名</th>
                        <th>スコア</th>
                        <th>プロパティ0</th>
                        <th>プロパティ1</th>
                        <th>プロパティ2</th>
                        <th>プロパティ3</th>
                        <th>プロパティ4</th>
                        <th>プロパティ5</th>
                        <th>プロパティ6</th>
                        <th>プロパティ7</th>
                    </tr>
                    <?php foreach($res->data as $val): ?>
                        <tr>
                            <td><?php echo $val->rankingId; ?></td>
                            <td><?php echo $val->aimeId; ?></td>
                            <td><?php echo $val->playerName; ?></td>
                            <td><?php echo $val->score; ?></td>
                            <td><?php echo $val->property0; ?></td>
                            <td><?php echo $val->property1; ?></td>
                            <td><?php echo $val->property2; ?></td>
                            <td><?php echo $val->property3; ?></td>
                            <td><?php echo $val->property4; ?></td>
                            <td><?php echo $val->property5; ?></td>
                            <td><?php echo $val->property6; ?></td>
                            <td><?php echo $val->property7; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
