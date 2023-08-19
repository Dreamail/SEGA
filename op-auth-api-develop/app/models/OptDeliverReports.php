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
class OptDeliverReports
{
    /**
     * オプション配信レポートを登録します
     *
     * @param $form
     * @throws Exception
     */
    public static function save($form)
    {
        $query =
            'insert into'
            .'  opt_deliver_reports'
            .'    (serial, download_state, segs_total, segs_downloaded, order_time, release_time, auth_time, auth_state'
            .'    , description, ap_ver_released, os_ver_released, files_released, files_working, create_user_id, update_user_id)'
            .'  values'
            .'    ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)'
            .'  on conflict on constraint opt_deliver_reports_pkey'
            .'  do update set download_state = $2, segs_total = $3, segs_downloaded = $4, order_time = $5, release_time = $6, auth_time = $7,'
            .'    auth_state = $8, description = $9, ap_ver_released = $10, os_ver_released = $11, '
            .'    files_released = $12, files_working = $13, update_date = current_timestamp, update_user_id = $15';
            $params = array($form->serial, $form->rf_state, $form->tsc, $form->tdsc, date('Y/m/d H:i:s',$form->ot), date('Y/m/d H:i:s',$form->rt), date('Y/m/d H:i:s',$form->at), $form->as,
                $form->gd, $form->dav, $form->dov, CommonUtil::convertListToStr($form->dfl), CommonUtil::convertListToStr($form->wfl), DB_REPORT_CREATE_USER_ID, DB_REPORT_UPDATE_USER_ID);
        LogUtil::sqlLog("[オプション配信レポート:登録]"
            ." query=".$query
            ." param=(".$form->serial.", ".$form->rf_state.", ".$form->tsc.", ".$form->tdsc.", ".date('Y/m/d H:i:s',$form->ot).", ".date('Y/m/d H:i:s',$form->rt).", ".date('Y/m/d H:i:s',$form->at).", ".$form->as
            .", ".$form->gd.", ".$form->dav.", ".$form->dov.", ".CommonUtil::convertListToStr($form->dfl).", ".CommonUtil::convertListToStr($form->wfl).", ".DB_REPORT_CREATE_USER_ID.", ".DB_REPORT_UPDATE_USER_ID.")");
        try {
            $queryResult = pg_query_params($query, $params);
            if (!$queryResult) {
                throw new Exception("オプション配信レポートテーブルの登録に失敗しました。"
                    ." query=".$query
                    ." param=(".$form->serial.", ".$form->rf_state.", ".$form->tsc.", ".$form->tdsc.", ".$form->ot.", ".$form->rt.", ".$form->at.", ".$form->as
                    .", ".$form->gd.", ".$form->dav.", ".$form->dov.", ".CommonUtil::convertListToStr($form->dfl).", ".CommonUtil::convertListToStr($form->wfl).", ".DB_REPORT_CREATE_USER_ID.", ".DB_REPORT_UPDATE_USER_ID.")");
            }
        } catch (Exception $e) {
            throw $e;
        } finally {
            // 結果セットを開放する
            pg_free_result($queryResult);
        }
    }
}