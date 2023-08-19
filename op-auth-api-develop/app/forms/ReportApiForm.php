<?php
namespace app\forms;

require_once "app/utils/CommonUtil.php";
require_once "app/constant.php";

use app\forms\AbstractForm;
use app\utils\CommonUtil;
use app\utils\LogUtil;

/**
 * 配信ステータスAPIリクエストフォーム
 * Class ReportApiForm
 * @package app\forms
 */
class ReportApiForm
{
    /** エラーフラグ  */
    public $errorflag = true;

    /** キーチップシリアル */
    public $serial;
    /** download status 詳細 */
    public $rf_state;
    /** 認証時刻 */
    public $at;
    /** 指示書説明 */
    public $gd;
    /** ダウンロード開始時刻 */
    public $ot;
    /** 公開時刻 */
    public $rt;
    /** 認証状態 */
    public $as;
    /** 総セグメント数 */
    public $tsc;
    /** ダウンロード済みセグメント数 */
    public $tdsc;
    /** 公開済みアプリバージョン */
    public $dav;
    /** 作業中アプリバージョン ※オプションイメージレポートでは使用しない */
    public $wdav;
    /** 公開済みOSバージョン */
    public $dov;
    /** 作業中バージョン ※オプションイメージレポートでは使用しない */
    public $wdov;
    /** 公開済みファイルリスト */
    public $dfl;
    /** 作業中ファイルリスト */
    public $wfl;

    /**
     * 入力チェック
     */
    public function validateRequest($deliverData, $imageType)
    {
        LogUtil::debugLog("入力チェック ");

        if(!isset($deliverData['serial']) || CommonUtil::isEmpty($deliverData['serial'])) { LogUtil::warnLog('serial'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->serial = $deliverData['serial']; }
        if(!isset($deliverData['rf_state']) || CommonUtil::isEmpty($deliverData['rf_state'])) { LogUtil::warnLog('rf_state'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->rf_state = $deliverData['rf_state']; }
        if(!isset($deliverData['tsc']) || CommonUtil::isEmpty($deliverData['tsc'])) { LogUtil::warnLog('tsc'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->tsc = $deliverData['tsc']; }
        if(!isset($deliverData['tdsc']) || CommonUtil::isEmpty($deliverData['tdsc'])) { LogUtil::warnLog('tdsc'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->tdsc = $deliverData['tdsc']; }
        if(!isset($deliverData['gd']) || CommonUtil::isEmpty($deliverData['gd'])) { LogUtil::warnLog('gd'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->gd = $deliverData['gd']; }
        if(!isset($deliverData['dav']) || CommonUtil::isEmpty($deliverData['dav'])) { LogUtil::warnLog('dav'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->dav = $deliverData['dav']; }
        if(!isset($deliverData['dov']) || CommonUtil::isEmpty($deliverData['dov'])) { LogUtil::warnLog('dov'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->dov = $deliverData['dov']; }
        if (!$this->checkFileList($deliverData['dfl'])) { LogUtil::warnLog('dfl'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->dfl = $deliverData['dfl']; }
        if (!$this->checkFileList($deliverData['wfl'])) { LogUtil::warnLog('wfl'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->wfl = $deliverData['wfl']; }

        if($imageType === IMAGE_TYPE_APP) {
            if(!isset($deliverData['wdav']) || CommonUtil::isEmpty($deliverData['wdav'])) { LogUtil::warnLog('wdav'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->wdav = $deliverData['wdav']; }
            if(!isset($deliverData['wdov']) || CommonUtil::isEmpty($deliverData['wdov'])) { LogUtil::warnLog('wdov'.EMPTY_MESSAGE); $this->errorflag = false; } else { $this->wdov = $deliverData['wdov']; }
        }

        if(!isset($deliverData['as']) || ($deliverData['as'] !== AUTH_STATE_FAILED && $deliverData['as'] !== AUTH_STATE_SUCCESS)) {
            LogUtil::warnLog('as'.EMPTY_MESSAGE);
            $this->errorflag = false;
        } else {
            $this->as = $deliverData['as'];
        }

        if(!isset($deliverData['at']) || CommonUtil::isEmpty($deliverData['at'])) {
            $this->at = CALENDER_DEFAULT_DATE;
        } else {
            $this->at = CommonUtil::convertSecondToMillis($deliverData['at']);
        }

        if(!isset($deliverData['ot']) || CommonUtil::isEmpty($deliverData['ot'])) {
            $this->ot = CALENDER_DEFAULT_DATE;
        } else {
            $this->ot = CommonUtil::convertSecondToMillis($deliverData['ot']);
        }

        if(!isset($deliverData['rt']) || CommonUtil::isEmpty($deliverData['rt'])) {
            $this->rt = CALENDER_DEFAULT_DATE;
        } else {
            $this->rt = CommonUtil::convertSecondToMillis($deliverData['rt']);
        }
    }

    /**
     * ファイルリストのチェックを行います。（公開済・作業中ファイルリスト）
     *
     * @param $fileList ファイルリスト
     * @return bool チェックONの場合、true
     */
    private function checkFileList($fileList) {
        // 空はOK
        if (!isset($fileList)) {
            return true;
        }
        if (CommonUtil::isArrayEmpty($fileList)) {
            return true;
        }

        foreach ($fileList as $file) {
            // リスト内に空がある場合はNG
            if (!isset($file) || CommonUtil::isEmpty($file)) {
                return false;
            }
        }

        // リストがすべてセットされている
        return true;
    }
}
