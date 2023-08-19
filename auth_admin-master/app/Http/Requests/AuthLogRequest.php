<?php

namespace App\Http\Requests;

use App\Library\Util;
use Illuminate\Support\Facades\Config;
use Log;
use Validator;
use Illuminate\Foundation\Http\FormRequest;

class AuthLogRequest extends FormRequest
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
        // ゲームID用のバリデーションチェック
        Validator::extend('gameId', function($attribute, $value, $parameters, $validator) {
            if(!is_null($value) && $value !== "") {
                return preg_match("/^[A-Z0-9]{3,5}$/", $value);
            }
            return true;
        });

        // シリアル用のバリデーションチェック
        Validator::extend('serial', function($attribute, $value, $parameters, $validator) {
            if(!is_null($value) && $value !== "") {
                return preg_match("/^[A-Z0-9]{11}$/", $value);
            }
            return true;
        });

        // ALL.NetID用のバリデーションチェック
        Validator::extend('allNetId', function($attribute, $value, $parameters, $validator) {
            if (!Util::isEmpty($value)) {
                if (!is_numeric($value)) return false;
                return true;
            }
            return true;
        });

        return [
            //'var' => 'required_without_all:gameId,serial,allNetId',
            'gameId' => 'gameId',
            'serial' => 'serial',
            'allNetId' => 'allNetId',
        ];
    }

    public function messages() {
        return [
            //'var.required_without_all' => '基板シリアルは必須項目です。',//Config::get('message.REQUIRED_WITHOUT_ALL'),
            'game_id' => Config::get('message.TITLEID_ERROR'),
            'serial' => Config::get('message.MASHINEID_ERROR'),
            'all_net_id' => Config::get('message.ALLNET_ID_ERROR'),
        ];
    }
}
