<?php

namespace App\Http\Requests;

use Illuminate\Support\Facades\Config;
use Illuminate\Http\Request;
use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Log;
use Session;

class PassUpdateRequest extends FormRequest
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
        Validator::extend('userId_check', function($attribute, $value, $parameters, $validator) {

            $loginId = Session::get('login');

            if($value === $loginId) {
                return true;
            }
            return false;
        });

        $rule = array();
        if (Request::input('action') != 'back') {
            $rule = [
                'userId' => ['required','userId_check'],
                'password' => ['required','regex:/^[0-9a-zA-Z_-]+$/','max:16'],
            ];
        }
        return $rule;
    }

    public function messages()
    {
        return [
            'userId.required' => Config::get('message.FRAUD_ERROR'),
            'userId.user_id_check' => Config::get('message.FRAUD_ERROR'),
            'password.required' => Config::get('message.REQUIRED')['PASSWORD'],
            'password.regex' => Config::get('message.PASSWORD_ERROR'),
            'password.max' => Config::get('message.PASSWORD_MAX_ERROR'),
        ];
    }
}
