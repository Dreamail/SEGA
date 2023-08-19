<?php

namespace App\Http\Requests;

use app\Library\Util;
use Illuminate\Support\Facades\Config;
use Illuminate\Http\Request;
use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Log;

class UserUpdateRequest extends FormRequest
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
        $rule = array();
        if (Request::input('action') != 'back') {
            $rule = [
                'password' => ['nullable','regex:/^[0-9a-zA-Z_-]+$/','max:16'],
                'roleId' => 'required',
            ];
        }
        return $rule;
    }

    public function messages()
    {
        return [
            'password.regex' => Config::get('message.PASSWORD_ERROR'),
            'password.max' => Config::get('message.PASSWORD_MAX_ERROR'),
            'roleId.required' => Config::get('message.REQUIRED')['ROLEID'],
        ];
    }
}
