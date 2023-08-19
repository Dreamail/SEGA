<?php

namespace App\Http\Requests;

use Illuminate\Http\Request;
use Validator;
use Illuminate\Foundation\Http\FormRequest;

class OrderRegisterGameRequest extends FormRequest
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
            } elseif($value !== 'game') {
                return false;
            }
            return true;
        });
        $rule = array();
        if (Request::input('action') != 'back') {
            $rule = [
                'gameId' => ['required','regex:/^[A-Z0-9]{3,5}$/'],
                'gameVer' => ['required', 'numeric', 'max:65.54', 'min:0'],
                //'imageType' => ['required', 'integer', 'between:0,1'],
                'uri' => ['required','url'],
                'registerType' => 'registerType',
            ];
        }
        return $rule;
    }

    public function messages()
    {
        return [
            'gameId.required' => 'ゲームIDは必須項目です。',
            'gameId.regex' => 'ゲームIDのフォーマットが不正です。',
            'gameVer.required' => 'ゲームVerは必須項目です。',
            'gameVer.*' => 'ゲームVerに数値以外か範囲外の数値が入力されています。',
            //'imageType.required' => 'イメージタイプは必須項目です。',
            //'imageType.integer' => 'イメージタイプに不正な値が選択されました。',
            //'imageType.between' => 'イメージタイプに不正な値が選択されました。',
            'uri.required' => '配信指示書URIは必須項目です。',
            'uri.url' => '配信指示書URIのURIフォーマットが不正です。',
            'register_type' => '登録情報が不正です。',
        ];
    }
}
