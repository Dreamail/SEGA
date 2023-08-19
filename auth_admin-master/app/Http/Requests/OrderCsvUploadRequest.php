<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class OrderCsvUploadRequest extends FormRequest
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
            'target' => [
                'required',
                Rule::in(['game', 'machine', 'clientGroup']),
            ],
            'file' => 'required',
        ];
    }

    public function messages()
    {
        return [
            'target.*' => '対象の配信指示書を選択してください。',
            'file.required' => 'ファイルを選択してください。',
        ];
    }
}
