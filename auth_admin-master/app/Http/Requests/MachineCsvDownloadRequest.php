<?php

namespace App\Http\Requests;

use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Config;

class MachineCsvDownloadRequest extends FormRequest
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
     *
     * @return array
     */
    public function rules()
    {
        Validator::extend('day', function($attribute, $value, $parameters, $validator) {
            if ($this->input('type') == 'byTime')
            {
                return isset($value) && is_numeric($value); // 検索タイプ'byTime'の場合、必須、数値のみ
            }
            return true;
        });
        return [
            'gameId' => ['required','regex:/^[A-Z0-9]{3,5}$/'],
            'type' => [
                'required',
                Rule::in(['onlyAuth', 'byTime', 'onlyNotAuth']),
            ],
            'day' => 'day',
        ];
    }

    public function messages()
    {
        return [
            'gameId.required' => Config::get('message.CN.TITLEID_REQUIRED_ERROR'), // 'ゲームIDを選択してください。',
            'gameId.regex' => Config::get('message.CN.TITLEID_ERROR'), // 'ゲームIDのフォーマットが不正です。',
            'type.*' => Config::get('message.CN.SEARCH_TYPE_REQUIRED_ERROR'), // '検索タイプを選択してください。',
            'day.*' => Config::get('message.CN.DAY_ERROR'), // '日数の値が不正です。',
        ];
    }
}
