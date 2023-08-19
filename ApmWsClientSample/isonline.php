<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$path = "/ws/isonline/%s/%s";

$subGameId = "";
$pass = "";
$aimeId = "";

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $aimeId = $_POST['aimeId'];

    if (isset($subGameId) && isset($pass) && isset($aimeId)) {
        // タイトルオンライン判定リクエスト用のJSONを生成するために連想配列を定義します
        $param = array(
            "pass" => $pass // アクセスパスワード
        );
        // サーバにリクエストを行います（リクエストパスにはサブゲームIDとAimeIDを含めます）
        $url = $protocol."://".$host.":".$port.sprintf($path, $subGameId, $aimeId);
        $client = new JsonClient();
        $res = $client->executeRequest($url, $param); // $res->dataには判定結果（1:オンライン, 0:オフライン）がセットされます
    }
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>サンプル - タイトルオンライン判定</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>タイトルオンライン判定</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード<dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>AimeID:</dt>
                <dd><input type="text" name="aimeId" value="<?php echo $aimeId; ?>"></dd>
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
            <?php elseif ($res->data == 1): ?>
                <h2>処理成功</h2>
                <p>AimeID[<?php echo $aimeId ?>]はオンラインです。</p>
            <?php else: ?>
                <h2>処理成功</h2>
                <p>AimeID[<?php echo $aimeId ?>]はオフラインです。</p>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
