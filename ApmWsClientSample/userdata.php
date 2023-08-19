<?php
require_once 'client/JsonClient.php';

$protocol = "https";
$host = "api.test-apm.sys-all.net";
$port = "443";
$pathRefer = "/ws/getuserdata/%s/%s";
$pathUpdate = "/ws/updateuserdata/%s/%s";

$subGameId = "";
$pass = "";
$aimeId = "";
$slotOrders = "";

if ($_POST != null) {
    $subGameId = $_POST['subGameId'];
    $pass = $_POST['pass'];
    $aimeId = $_POST['aimeId'];
    // カンマで区切られた入力文字列を配列に変換します
    $slotOrders = $_POST['slotOrders'];
    $slotOrderArray = explode(",", $slotOrders);

    $updateError = false;
    $client = new JsonClient();

    if (isset($_POST['update'])) {
        foreach ($_POST['slots'] as $s) {
            // slotsパラメータはBase64文字列である必要があります
            $base64Slots[] = base64_encode(pack("H*", $s));
        }
        // ユーザデータ更新リクエスト用のJSONを生成するために連想配列を定義します
        $param = array(
            "pass" => $pass,    // アクセスパスワード
            "data" => array(
                "playerName" => $_POST['playerName'],   // プレイヤー名
                "slotOrders" => $slotOrderArray,        // スロット指定（配列で指定します）
                "slots" => $base64Slots,                // スロットデータ（配列で指定します）
                "str0" => $_POST['str0'],               // 文字データ0
                "str1" => $_POST['str1'],               // 文字データ1
                "str2" => $_POST['str2'],               // 文字データ2
                "str3" => $_POST['str3'],               // 文字データ3
                "str4" => $_POST['str4'],               // 文字データ4
                "str5" => $_POST['str5'],               // 文字データ5
                "str6" => $_POST['str6'],               // 文字データ6
                "str7" => $_POST['str7'],               // 文字データ7
                "num0" => $_POST['num0'],               // 数値データ0
                "num1" => $_POST['num1'],               // 数値データ1
                "num2" => $_POST['num2'],               // 数値データ2
                "num3" => $_POST['num3'],               // 数値データ3
                "num4" => $_POST['num4'],               // 数値データ4
                "num5" => $_POST['num5'],               // 数値データ5
                "num6" => $_POST['num6'],               // 数値データ6
                "num7" => $_POST['num7'],               // 数値データ7
                "token" => $_POST['token']              // トークン
            )
        );
        // サーバにリクエストを行います
        $url = $protocol."://".$host.":".$port.sprintf($pathUpdate, $subGameId, $aimeId);
        $res = $client->executeRequest($url, $param);
        // $res->status->codeが1以外は失敗です
        if ($res->status->code != 1) {
            // $res->dataはnullとなります
            $msg = "ユーザデータ更新失敗";
            $updateError = true;
        } else {
            // $res->dataは1となります
            $msg = "ユーザデータ更新成功";
        }
    }

    if (!$updateError) {
        // ユーザデータ取得リクエスト用のJSONを生成するために連想配列を定義します
        $param = array(
            "pass" => $pass,    // アクセスパスワード
            "param" => array(
                "slotOrders" => $slotOrderArray // スロット指定（配列で指定します）
            )
        );
        // サーバにリクエストを行います
        $url = $protocol."://".$host.":".$port.sprintf($pathRefer, $subGameId, $aimeId);
        $res = $client->executeRequest($url, $param);
        // $res->status->codeが1以外は失敗です
        if ($res->status->code != 1) {
            // $res->dataはnullとなります
            $msg = "ユーザデータ取得失敗";
        } elseif (!isset($msg)) {
            // $res->dataにはユーザデータが含まれます
            $msg = "ユーザデータ取得成功";
        }
    }
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>サンプル - ユーザデータ取得・更新</title>
        <link type="text/css" rel="stylesheet" href="style.css">
    </head>
    <body>
        <h1>ユーザデータ取得・更新</h1>
        <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
            <dl>
                <dt>サブゲームID</dt>
                <dd><input type="text" name="subGameId" value="<?php echo $subGameId; ?>"></dd>
                <dt>アクセスパスワード</dt>
                <dd><input type="text" name="pass" value="<?php echo $pass; ?>"></dd>
                <dt>AimeID</dt>
                <dd><input type="text" name="aimeId" value="<?php echo $aimeId; ?>"></dd>
                <dt>スロット指定</dt>
                <dd><input type="text" name="slotOrders" value="<?php echo $slotOrders; ?>"></dd>
                <dt>&nbsp;</dt>
                <dd><button type="submit">送信</button></dd>
            </dl>
        </form>
        <hr>
        <?php if ($_POST != null): ?>
            <h2><?php echo $msg; ?></h2>
            <?php if ($res->status->code != 1): ?>
                <p>処理コード:<?php echo $res->status->code; ?>&nbsp;原因コード:<?php echo $res->status->subCode; ?></p>
            <?php else: ?>
                <form method="POST" action="<?php echo $_SERVER["PHP_SELF"]; ?>">
                    <table>
                        <tr>
                            <th>AimeID</th>
                            <td colspan="2"><?php echo $res->data->aimeId; ?></td>
                        </tr>
                        <tr>
                            <th>playerName</th>
                            <td colspan="2"><input type="text" name="playerName" value="<?php echo $res->data->playerName; ?>"></td>
                        </tr>
                        <?php for ($i = 0; $i < count($res->data->slots); $i++): ?>
                            <tr>
                                <?php if ($i == 0): ?>
                                    <?php if (count($res->data->slots) > 1): ?>
                                        <th rowspan="<?php echo count($res->data->slots); ?>">slots</th>
                                    <?php else: ?>
                                        <th>slots</th>
                                    <?php endif; ?>
                                <?php endif; ?>
                                <td><?php echo $slotOrderArray[$i]; ?></td>
                                <td>
                                    <textarea name="slots[]" cols="32" rows="10"><?php echo bin2hex(base64_decode($res->data->slots[$i])); ?></textarea>
                                </td>
                            </tr>
                        <?php endfor; ?>
                        <tr>
                            <th>str0</th>
                            <td colspan="2"><input type="text" name="str0" value="<?php echo $res->data->str0; ?>"></td>
                        </tr>
                        <tr>
                            <th>str1</th>
                            <td colspan="2"><input type="text" name="str1" value="<?php echo $res->data->str1; ?>"></td>
                        </tr>
                        <tr>
                            <th>str2</th>
                            <td colspan="2"><input type="text" name="str2" value="<?php echo $res->data->str2; ?>"></td>
                        </tr>
                        <tr>
                            <th>str3</th>
                            <td colspan="2"><input type="text" name="str3" value="<?php echo $res->data->str3; ?>"></td>
                        </tr>
                        <tr>
                            <th>str4</th>
                            <td colspan="2"><input type="text" name="str4" value="<?php echo $res->data->str4; ?>"></td>
                        </tr>
                        <tr>
                            <th>str5</th>
                            <td colspan="2"><input type="text" name="str5" value="<?php echo $res->data->str5; ?>"></td>
                        </tr>
                        <tr>
                            <th>str6</th>
                            <td colspan="2"><input type="text" name="str6" value="<?php echo $res->data->str6; ?>"></td>
                        </tr>
                        <tr>
                            <th>str7</th>
                            <td colspan="2"><input type="text" name="str7" value="<?php echo $res->data->str7; ?>"></td>
                        </tr>
                        <tr>
                            <th>num0</th>
                            <td colspan="2"><input type="text" name="num0" value="<?php echo $res->data->num0; ?>"></td>
                        </tr>
                        <tr>
                            <th>num1</th>
                            <td colspan="2"><input type="text" name="num1" value="<?php echo $res->data->num1; ?>"></td>
                        </tr>
                        <tr>
                            <th>num2</th>
                            <td colspan="2"><input type="text" name="num2" value="<?php echo $res->data->num2; ?>"></td>
                        </tr>
                        <tr>
                            <th>num3</th>
                            <td colspan="2"><input type="text" name="num3" value="<?php echo $res->data->num3; ?>"></td>
                        </tr>
                        <tr>
                            <th>num4</th>
                            <td colspan="2"><input type="text" name="num4" value="<?php echo $res->data->num4; ?>"></td>
                        </tr>
                        <tr>
                            <th>num5</th>
                            <td colspan="2"><input type="text" name="num5" value="<?php echo $res->data->num5; ?>"></td>
                        </tr>
                        <tr>
                            <th>num6</th>
                            <td colspan="2"><input type="text" name="num6" value="<?php echo $res->data->num6; ?>"></td>
                        </tr>
                        <tr>
                            <th>num7</th>
                            <td colspan="2"><input type="text" name="num7" value="<?php echo $res->data->num7; ?>"></td>
                        </tr>
                        <tr>
                            <th>token</th>
                            <td colspan="2"><?php echo $res->data->token; ?></td>
                        </tr>
                        <tr>
                            <td colspan="3" style="text-align: center">
                                <button type="submit">更新</button>
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" name="subGameId" value="<?php echo $subGameId ?>">
                    <input type="hidden" name="pass" value="<?php echo $pass ?>">
                    <input type="hidden" name="aimeId" value="<?php echo $aimeId ?>">
                    <input type="hidden" name="slotOrders" value="<?php echo $slotOrders ?>">
                    <input type="hidden" name="token" value="<?php echo $res->data->token; ?>">
                    <input type="hidden" name="update" value="true">
                </form>
            <?php endif; ?>
        <?php endif; ?>
        <div><a href="index.html">戻る</a></div>
    </body>
</html>
