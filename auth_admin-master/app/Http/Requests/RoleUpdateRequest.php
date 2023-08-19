<?php

namespace App\Http\Requests;

use Illuminate\Support\Facades\Config;
use Illuminate\Http\Request;
use Validator;
use Illuminate\Foundation\Http\FormRequest;

class RoleUpdateRequest extends FormRequest
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
                'explanation' => ['required','max:200'],
                'authSetIds' => 'required',
            ];
        }
        return $rule;
    }

    public function messages()
    {
        return [
            'explanation.required' => Config::get('message.REQUIRED')['EXPLANATION'],
            'explanation.max' => Config::get('message.EXPLANATION_MAX_ERROR'),
            'authSetIds.required' => Config::get('message.REQUIRED')['AUTH_SET_IDS'],
        ];
    }
}
