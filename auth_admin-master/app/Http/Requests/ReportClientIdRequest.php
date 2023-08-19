<?php

namespace App\Http\Requests;

use Log;
use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Config;

class ReportClientIdRequest extends FormRequest
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
            'serial' => 'required',
        ];
    }

    public function messages() {
        return [
            'serial.required' => Config::get('message.REQUIRED')['SERIAL'],
        ];
    }
}
