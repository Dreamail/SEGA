<?php

namespace App\Http\Requests;

use Illuminate\Support\Facades\Config;
use Log;
use Validator;
use Illuminate\Foundation\Http\FormRequest;

class LoginRequest extends FormRequest
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
        return [
            'loginId' => 'required',
            'password' => 'required',
        ];
    }

    public function messages() {
        return [
            'loginId.required' => Config::get('message.LOGIN_ERROR'),
            'password.required' => Config::get('message.LOGIN_ERROR'),
        ];
    }
}
