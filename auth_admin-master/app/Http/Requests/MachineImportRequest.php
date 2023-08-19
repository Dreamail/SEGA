<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Config;

class MachineImportRequest extends FormRequest
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
            'gameId' => ['required','regex:/^[A-Z0-9]{3,5}$/'],
            'file' => 'required',
        ];
    }

    public function messages()
    {
        return [
            'gameId.required' => Config::get('message.TITLEID_REQUIRED_ERROR'), // 'ゲームIDを選択してください。',
            'gameId.regex' => Config::get('message.TITLEID_ERROR'), // 'ゲームIDのフォーマットが不正です。',
            'file.required' => Config::get('message.REQUIRED.FILE'), // 'ファイルを選択してください。',
        ];
    }
}
