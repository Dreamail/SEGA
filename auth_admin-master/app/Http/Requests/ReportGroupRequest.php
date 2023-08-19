<?php

namespace App\Http\Requests;

use Log;
use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Config;

class ReportGroupRequest extends FormRequest
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
            'action' => 'required',
            'gameId' => 'required',
            //'client_group' => 'required',
        ];
    }

    public function messages() {
        return [
            'action.required' => Config::get('message.SYSTEM_ERROR'),
            'gameId.required' => Config::get('message.REQUIRED')['TITLEID'],
            //'client_group.required' => Config::get('message.REQUIRED')['CLIENT_GROUP'],
        ];
    }
}
