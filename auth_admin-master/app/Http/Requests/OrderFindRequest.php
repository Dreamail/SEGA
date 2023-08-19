<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class OrderFindRequest extends FormRequest
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
            'titleId' => ['required','regex:/^[A-Z0-9]{3,5}$/'],
        ];
    }

    public function messages()
    {
        return [
            'titleId.required' => 'ゲームIDを選択してください。',
            'titleId.regex' => 'ゲームIDのフォーマットが不正です。',
        ];
    }
}
