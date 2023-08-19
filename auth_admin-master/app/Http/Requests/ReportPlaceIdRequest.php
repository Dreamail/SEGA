<?php

namespace App\Http\Requests;

use Log;
use Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Config;

class ReportPlaceIdRequest extends FormRequest
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
            'placeId' => 'required',
        ];
    }

    public function messages() {
        return [
            'action' => Config::get('message.SYSTEM_ERROR'),
            'placeId.required' => Config::get('message.REQUIRED')['PLACEID'],
        ];
    }
}
