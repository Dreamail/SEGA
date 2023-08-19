<?php

namespace App\Http\Requests;

use App\Library\Util;
use App\Models\Games;
use App\Models\Machines;
use App\Models\Places;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\Log;
use Validator;
use Illuminate\Http\Request;

class MachineUpdateConfirmRequest extends FormRequest
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
        Validator::extend('serialCheck', function($attribute, $value, $parameters, $validator) {
            return Util::serialCheck($value);
        });

        Validator::extend('serialExistCheck', function($attribute, $value, $parameters, $validator) {
            $machine = new Machines();
            return $machine->existsByPk($value);
        });

        Validator::extend('allNetIdCheck',function($attribute,$value,$parameters,$validator){
           return Util::allNetIdCheck($value);
        });

        Validator::extend('allNetIdExistCheck',function($attribute,$value,$parameters,$validator){

            $places = new Places();
            return $places->existsByPk($value);
        });

        Validator::extend('gameIdExistCheck',function($attribute,$value,$parameters,$validator){

            $games = new Games();
            return $games->existsByPk($value);
        });

        Validator::extend('placeIpCheck', function($attribute, $value, $parameters, $validator) {
            return Util::placeIpCheck($value);
        });

        $rule = array();
        if (Request::input('action') != 'back') {
            $rule = [
                'serial' => ['bail','required','serialCheck','serialExistCheck'],
                'allNetId' => ['bail','required','allNetIdCheck','allNetIdExistCheck'],
                'gameId' =>['bail','required','regex:/^[A-Z0-9]{3,5}$/','gameIdExistCheck'],
                'setting' =>['bail','required','regex:/^[1-3]$/'],
                'placeIp' => ['bail','required','placeIpCheck'],
            ];
        }
        return $rule;
    }

    public function messages()
    {
        return [
            'serial.required' => Config::get('message.FRAUD_ERROR'),
            'serial.serial_check' => Config::get('message.FRAUD_ERROR'),
            'serial.serial_exist_check' => Config::get('message.MACHINEID_EXIST_ERROR'),
            'allNetId.required' => Config::get('message.FRAUD_ERROR'),
            'allNetId.all_net_id_check' => Config::get('message.FRAUD_ERROR'),
            'allNetId.all_net_id_exist_check' => Config::get('message.ALLNETID_EXIST_ERROR'),
            'gameId.required' => Config::get('message.FRAUD_ERROR'),
            'gameId.regex' => Config::get('message.FRAUD_ERROR'),
            'gameId.game_id_exist_check' => Config::get('message.TITLEID_EXIST_ERROR'),
            'setting.required' => Config::get('message.FRAUD_ERROR'),
            'setting.regex' => Config::get('message.FRAUD_ERROR'),
            'placeIp.required' => Config::get('message.FRAUD_ERROR'),
            'placeIp.place_ip_check' => Config::get('message.FRAUD_ERROR'),
        ];
    }

    protected function getRedirectUrl()
    {
        Log::debug('check');
        return '/top';
    }
}
