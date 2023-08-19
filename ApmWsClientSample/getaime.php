<?php
require_once 'client/SimpleClient.php';

$protocol = "https";
$host = "net-aime-test.naominet.jp";
$port = "443";
$path = "/aime/websys/check_account.jsp?sidn=%s&psn=%s&gidn=%s";

$sidn = "";
$psn = "";
$gidn = "";

if ($_POST != null) {
    $sidn = $_POST['sidn'];
    $psn = $_POST['psn'];
    $gidn = $_POST['gidn'];
    // サーバにリクエストを行います
    $url = $protocol."://".$host.":".$port.sprintf($path, $sidn, $psn, $gidn);
    $client = new SimpleClient();
    $res = $client->executeRequest($url, null, "GET");

    $line = explode("\n", $res);
    $errorCode = $line[0];
    $count = $line[2];
    for($i = 3; $i < $count + 3; $i++) {
        $list[] = explode("\t", $line[$i]);
    }
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>サンプル - Aime/SEGA ID 認証</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>Aime/SEGA ID 認証</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>sidn</dt>
                <dd><input type="text" name="sidn" value="<?php echo $sidn; ?>"></dd>
                <dt>psn<dt>
                <dd><input type="text" name="psn" value="<?php echo $psn; ?>"></dd>
                <dt>gidn</dt>
                <dd><input type="text" name="gidn" value="<?php echo $gidn; ?>"></dd>
                <dt>&nbsp;</dt>
                <dd><button type="submit">送信</button></dd>
            </dl>
        </form>
        <hr>
        <?php if ($_POST != null): ?>
            <?php if ($errorCode != 0): ?>
                <h2>処理失敗</h2>
                <p>処理コード:<?php echo $errorCode; ?></p>
            <?php else: ?>
                <h2>処理成功</h2>
                <h2>Aimeカードデータ</h2>
                <table>
                    <tr>
                        <th>カードタイプ</th>
                        <th>AimeID</th>
                        <th>アクセスコード</th>
                        <th>コメント</th>
                    </tr>
                    <?php foreach ($list as $data): ?>
                        <tr>
                            <td><?php echo $data[0]; ?></td>
                            <td><?php echo $data[1]; ?></td>
                            <td><?php echo $data[2]; ?></td>
                            <td><?php echo $data[3]; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
