<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$path = "/ws/getregionlist/%s";

$subGameId = "";
$pass = "";
$countryCode = "";

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $countryCode = $_POST['countryCode'];

    // 店舗情報取得リクエスト用のJSONを生成するために連想配列を定義します
    $param = array(
        "pass" => $pass,    // アクセスパスワード
        "param" => array(
            "countryCode" => $countryCode          // 国コード
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
        <title>サンプル - 地域情報取得</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>地域情報取得</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード<dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>国コード</dt>
                <dd><input type="text" name="countryCode" value="<?php echo $countryCode; ?>"></dd>
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
                <table>
                    <tr>
                        <th>国コード</th>
                        <th>地域コードID</th>
                        <th>地域レベル</th>
                        <th>親地域コードID</th>
                        <th>地域名称</th>
                    </tr>
                    <?php foreach($res->data as $val): ?>
                        <tr>
                            <td><?php echo $val->countryCode; ?></td>
                            <td><?php echo $val->id; ?></td>
                            <td><?php echo $val->level; ?></td>
                            <td><?php echo $val->parent; ?></td>
                            <td><?php echo $val->name; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
