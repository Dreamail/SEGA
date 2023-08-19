<?php
namespace app\models;

require_once "app/constant.php";
require_once "app/utils/LogUtil.php";
require_once "app/utils/CommonUtil.php";

use app\utils\CommonUtil;
use app\utils\LogUtil;
use Exception;

/**
 * オプション配信レポート
 *
 * Class OptDeliverReports
 * @package app\models
 */
class OptDeliverReportsHistory
{
    /**
     * オプション配信レポートを登録します
     *
     * @param $form
     * @throws Exception
     */
    public static function save($form)
    {
        $now = date('Y/m/d H:i:s');
        $query =
            'insert into'
            .'  opt_deliver_report_logs'
            .'    (serial, download_state, segs_total, segs_downloaded, order_time, release_time, auth_time, auth_state'
            .'    , description, ap_ver_released, os_ver_released, files_released, files_working, create_user_id, access_time, create_date)'
            .'  values'
            .'    ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $15)';
            $params = array($form->serial, $form->rf_state, $form->tsc, $form->tdsc, date('Y/m/d H:i:s',$form->ot), date('Y/m/d H:i:s',$form->rt), date('Y/m/d H:i:s',$form->at), $form->as,
                $form->gd, $form->dav, $form->dov, CommonUtil::convertListToStr($form->dfl), CommonUtil::convertListToStr($form->wfl), DB_REPORT_CREATE_USER_ID, $now);
        LogUtil::sqlLog("[オプション配信レポート:登録]"
            ." query=".$query
            ." param=(".$form->serial.", ".$form->rf_state.", ".$form->tsc.", ".$form->tdsc.", ".date('Y/m/d H:i:s',$form->ot).", ".date('Y/m/d H:i:s',$form->rt).", ".date('Y/m/d H:i:s',$form->at).", ".$form->as
            .", ".$form->gd.", ".$form->dav.", ".$form->dov.", ".CommonUtil::convertListToStr($form->dfl).", ".CommonUtil::convertListToStr($form->wfl).", ".DB_REPORT_CREATE_USER_ID.", ".$now.")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("オプション配信レポートテーブルの登録に失敗しました。"
                    ." query=".$query
                    ." param=(".$form->serial.", ".$form->rf_state.", ".$form->tsc.", ".$form->tdsc.", ".$form->ot.", ".$form->rt.", ".$form->at.", ".$form->as
                    .", ".$form->gd.", ".$form->dav.", ".$form->dov.", ".CommonUtil::convertListToStr($form->dfl).", ".CommonUtil::convertListToStr($form->wfl).", ".DB_REPORT_CREATE_USER_ID.", ".$now.")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
}