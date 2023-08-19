<?php


namespace app\utils;

use DateTime;
use DateTimeZone;
use Exception;

class CMSUtil
{

    public static function createAuthResultInfo($result, $keychip_id, $seq) {

        try {

            // 再認証時刻、有効期限の取得
            $now = new DateTime("now",new DateTimeZone(LOCAL_TIMEZONE));
            $renewal = new DateTime($now->setTime(7,0,0)->format('Y-m-d H:i:s'),new DateTimeZone(LOCAL_TIMEZONE));
            $limit = new DateTime($now->setTime(10,0,0)->format('Y-m-d H:i:s'),new DateTimeZone(LOCAL_TIMEZONE));
            $renewal_sec = strtotime($renewal->format('Y-m-d H:i:s'));
            $limit_sec = strtotime($limit->format('Y-m-d H:i:s'));
            $renewal_add_time = "+". self::addTimeCalc($keychip_id) . " seconds";
            $limit_add_time = "";
            if ( strtotime($now->format('Y-m-d H:i:s')) > $renewal_sec ) {
                $renewal_add_time = $renewal_add_time . " +1 day";
                $limit_add_time = " +1 day";
            }
            $renewal = new DateTime(date('Y-m-d H:i:s', strtotime($renewal_add_time, $renewal_sec)), new DateTimeZone(LOCAL_TIMEZONE));
            $limit = new DateTime(date('Y-m-d H:i:s', strtotime($limit_add_time, $limit_sec)), new DateTimeZone(LOCAL_TIMEZONE));
            $renewal->setTimezone(new DateTimeZone('UTC'));
            $limit->setTimezone(new DateTimeZone('UTC'));

            // 署名対象メッセージ生成
            $message['result'] = hexdec(bin2hex($result)) > 0 ? 1 : 0;   //認証の結果 認証成功：0 認証失敗：1
            $message['key_id'] = $keychip_id;   //認証の対象のキーチップID
            $message['limit'] = $limit->format('Y-m-d\TH:i:s\Z');    //認証の結果の有効期限
            $message['renewal'] = $renewal->format('Y-m-d\TH:i:s\Z');  //再認証日時
            $message['serial'] = $seq;   //発番シリアル

            return $message;

        } catch ( Exception $e ) {

            throw $e;

        }

    }

    public static function signedAuthResultInfo($auth_result_info, $private_key_file, $certificate_file, $cms_tmp_dir ) {

        try {
            // TODO 署名付与は、PHP8.0のリリースまで仮実装　それまでは、opensslコマンドの外部実行で実装
            // 参考：openssl_cms_sign(string $infile, string $outfile, $signcert, $signkey, ?array $headers, int $flags = 0, int $encoding = OPENSSL_ENCODING_SMIME, ?string $extracertsfilename = null): bool {}
            // 署名データ作成用一時ファイル名
            $cms_tmp_dir .= substr($cms_tmp_dir,-1)=="/"?"":"/";
            $infile = $cms_tmp_dir."in_".session_id();
            $outfile = $cms_tmp_dir."out_".session_id();

            $data = "result=".(string)$auth_result_info['result']
                ."&key_id=".$auth_result_info['key_id']
                ."&limit=".$auth_result_info['limit']
                ."&renewal=".$auth_result_info['renewal']
                ."&serial=".$auth_result_info['serial'];

            // 署名対象ファイル作成
            $fh = fopen($infile,"w");
            fwrite($fh, $data);
            fclose($fh);

            // OPENSSLの署名コマンド生成
            $command = "openssl cms -sign -signer ".$certificate_file;
            $command .= " -inkey ".$private_key_file;
            $command .= " -binary -in ".$infile;
            $command .= " -outform pem -out ".$outfile;
            $command .= " -nodetach -md sha256 -nocerts -nosmimecap -noattr";

            // OPENSSL署名コマンドの実行
            exec($command,$out, $ret);
            if ( $ret != 0 ) {
                LogUtil::debugLog($command);
                return false;
            }
            // 署名済みデータの取得
            $fh = fopen($outfile, "r");
            $sined_auth_result_data = fread($fh, filesize($outfile));
            $sined_auth_result_data = str_replace(array("\r\n","\r","\n"),'', $sined_auth_result_data);
            $sined_auth_result_data = str_replace('-----BEGIN CMS-----',"",$sined_auth_result_data);
            $sined_auth_result_data = str_replace('-----END CMS-----',"",$sined_auth_result_data);

            // 署名済みデータ返却
            return $sined_auth_result_data;

        } catch ( Exception $e ) {

            throw $e;

        }
    }

    private static function addTimeCalc($keychip_id) {

        try {
            return ((intval(substr($keychip_id,7,1)) * 1000  +
                     intval(substr($keychip_id,8,1)) * 100  +
                     intval(substr($keychip_id,9,1)) * 10   +
                     intval(substr($keychip_id,10,1))) % 270 + 30 ) * 2;

        } catch ( Exception $e ) {

            throw $e;

        }

    }

}