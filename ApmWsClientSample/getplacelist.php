<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$path = "/ws/getplacelist/%s";

$subGameId = "";
$pass = "";
$countryCode = "";
$activeDays = "";
$placeType = "";
$allPlaces = true;

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $countryCode = $_POST['countryCode'];
    $activeDays = $_POST['activeDays'];
    $placeType = $_POST['placeType'];
    if (!isset($_POST['allPlaces'])) {
        $allPlaces = false;
    }

    // 店舗情報取得リクエスト用のJSONを生成するために連想配列を定義します
    $param = array(
        "pass" => $pass,    // アクセスパスワード
        "param" => array(
            "countryCode" => $countryCode,      // 国コード
            "activeDays" => $activeDays,    // 稼動日数
            "placeType" => $placeType,      // 店舗種別
            "allPlaces" => $allPlaces       // 全店舗表示フラグ
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
        <title>サンプル - 店舗情報取得</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>店舗情報取得</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード<dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>国コード</dt>
                <dd><input type="text" name="countryCode" value="<?php echo $countryCode; ?>"></dd>
                <dt>稼動日数</dt>
                <dd><input type="text" name="activeDays" value="<?php echo $activeDays; ?>"></dd>
                <dt>店舗種別</dt>
                <dd><input type="text" name="placeType" value="<?php echo $placeType; ?>"></dd>
                <dt>全店舗表示</dt>
                <dd>
                <?php if ($allPlaces): ?>
                    <input type="checkbox" name="allPlaces" value="true" checked="checked">
                <?php else: ?>
                    <input type="checkbox" name="allPlaces" value="true">
                <?php endif; ?>
                </dd>
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
                        <th>店舗ID</th>
                        <th>店舗名</th>
                        <th>電話番号</th>
                        <th>住所</th>
                        <th>郵便番号</th>
                        <th>地域コード0</th>
                        <th>地域コード1</th>
                        <th>地域コード2</th>
                        <th>地域コード3</th>
                        <th>最寄駅情報</th>
                        <th>開店時間</th>
                        <th>閉店時間</th>
                        <th>PR文</th>
                        <th>ニックネーム</th>
                        <th>ALL.Net ID</th>
                        <th>国コード</th>
                    </tr>
                    <?php foreach($res->data as $val): ?>
                        <tr>
                            <td><?php echo $val->locationId; ?></td>
                            <td><?php echo $val->name; ?></td>
                            <td><?php echo $val->tel; ?></td>
                            <td><?php echo $val->address; ?></td>
                            <td><?php echo $val->zipCode; ?></td>
                            <td><?php echo $val->region0Id; ?></td>
                            <td><?php echo $val->region1Id; ?></td>
                            <td><?php echo $val->region2Id; ?></td>
                            <td><?php echo $val->region3Id; ?></td>
                            <td><?php echo $val->station; ?></td>
                            <td><?php echo $val->open; ?></td>
                            <td><?php echo $val->close; ?></td>
                            <td><?php echo $val->pr; ?></td>
                            <td><?php echo $val->nickname; ?></td>
                            <td><?php echo $val->allnetId; ?></td>
                            <td><?php echo $val->countryCode; ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
