<?php

namespace App\Http\Requests;

use App\Library\Util;
use App\Models\Games;
use App\Models\Machines;
use App\Models\Places;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Config;
use Validator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Session;

class MachineUpdateRequest extends FormRequest
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
            'serial.required' => Config::get('message.REQUIRED_MACHINEID'),
            'serial.serial_check' => Config::get('message.MASHINEID_ERROR'),
            'serial.serial_exist_check' => Config::get('message.MACHINEID_EXIST_ERROR'),
            'allNetId.required' => Config::get('message.REQUIRED_ALLNETID'),
            'allNetId.all_net_id_check' => Config::get('message.ALLNET_ID_ERROR'),
            'allNetId.all_net_id_exist_check' => Config::get('message.ALLNETID_EXIST_ERROR'),
            'gameId.required' => Config::get('message.REQUIRED_TITLEID'),
            'gameId.regex' => Config::get('message.TITLEID_ERROR'),
            'gameId.game_id_exist_check' => Config::get('message.TITLEID_EXIST_ERROR'),
            'setting.required' => Config::get('message.REQUIRED_SETTING'),
            'setting.regex' => Config::get('message.SETTING_ERROR'),
            'placeIp.required' => Config::get('message.REQUIRED_PLACEIP'),
            'placeIp.place_ip_check' => Config::get('message.PLACEIP_ERROR'),
        ];
    }
}
