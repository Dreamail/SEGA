<?php

namespace App\Http\Requests;

use App\Models\Machines;
use App\Models\Users;
use Illuminate\Support\Facades\Config;
use Illuminate\Http\Request;
use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Log;

class UserRegistRequest extends FormRequest
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
        Validator::extend('userExistCheck', function($attribute, $value, $parameters, $validator) {
            $user = new Users();
            return !$user->existsByPk($value);
        });

        $rule = array();
        if (Request::input('action') != 'back') {
            $rule = [
                'userId' => ['required','regex:/^[0-9a-zA-Z_-]+$/','max:16', 'userExistCheck'],
                'password' => ['required','regex:/^[0-9a-zA-Z_-]+$/','max:16'],
                'roleId' => 'required',
            ];
        }
        return $rule;
    }

    public function messages()
    {
        return [
            'userId.required' => Config::get('message.REQUIRED')['USERID'],
            'userId.regex' => Config::get('message.USERID_ERROR'),
            'userId.max' => Config::get('message.USERID_MAX_ERROR'),
            'userId.user_exist_check' => Config::get('message.USER_EXIST_ERROR'),
            'password.required' => Config::get('message.REQUIRED')['PASSWORD'],
            'password.regex' => Config::get('message.PASSWORD_ERROR'),
            'password.max' => Config::get('message.PASSWORD_MAX_ERROR'),
            'roleId.required' => Config::get('message.REQUIRED')['ROLEID'],
        ];
    }
}
