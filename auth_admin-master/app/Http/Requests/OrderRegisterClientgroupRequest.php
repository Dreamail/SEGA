<?php

namespace App\Http\Requests;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Validator;
use Illuminate\Foundation\Http\FormRequest;

/**
 * クライアントグループ配信指示書フォーム
 * Class OrderRegisterClientgroupRequest
 * @package App\Http\Requests
 */
class OrderRegisterClientgroupRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     *
     * @return bool
     */
    public function authorize()
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     */
    public function rules()
    {
        // 登録タイプ用のバリデーションチェック
        Validator::extend('registerType', function($attribute, $value, $parameters, $validator) {
            if(is_null($value) || $value === "") {
                return false;
            } elseif($value !== 'clientGroup') {
                return false;
            }
            return true;
        });

        $rule = array();
        if (Request::input('action') != 'back') {
            $rule = [
                'titleId' => ['required','regex:/^[A-Z0-9]{3,5}$/'],
                'titleVer' => ['required', 'numeric', 'max:65.54', 'min:0'],
                'clientGroup' => ['required','regex:/^[!-~]+$/'],
                'imageType' => ['required', 'integer', 'between:0,1'],
                'uri' => ['required','url'],
                'registerType' => 'registerType',
            ];
        }
        return $rule;
    }

    /**
     * @return array
     */
    public function messages()
    {
        return [
            'titleId.required'      => Config::get('message.REQUIRED.TITLEID'), // 'ゲームIDは必須項目です。',
            'titleId.regex'          => Config::get('message.TITLEID_ERROR'), // 'ゲームIDのフォーマットが不正です。',
            'titleVer.required'     => Config::get('message.REQUIRED.TITLEVER'), // 'ゲームVerは必須項目です。',
            'titleVer.*'             => Config::get('message.TITLEVER_ERROR'), // 'ゲームVerに数値以外か範囲外の数値が入力されています。',
            'clientGroup.required' => Config::get('message.REQUIRED.CLIENT_GROUP'), // 'シリアルグループは必須項目です。',
            'clientGroup.regex'    => Config::get('message.CLIENT_GROUP_ERROR'), // 'シリアルグループのフォーマットが不正です。',
            'imageType.required'   => Config::get('message.REQUIRED.TYPE'), // 'イメージタイプは必須項目です。',
            'imageType.*'           => Config::get('message.TYPE_ERROR'), // 'イメージタイプに不正な値が選択されました。',
            'uri.required'          => Config::get('message.REQUIRED.URI'), // '配信指示書URIは必須項目です。',
            'uri.url'                => Config::get('message.URI_ERROR'), // '配信指示書URIのURIフォーマットが不正です。',
            'register_type'         => Config::get('message.REGISTERTYPE_ERROR'), // '登録情報が不正です。',
        ];
    }
}
