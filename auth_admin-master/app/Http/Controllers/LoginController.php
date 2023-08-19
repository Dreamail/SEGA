<?php

namespace App\Http\Controllers;

use App\Http\Requests\LoginRequest;
use App\Models\UserRole;
use App\Models\Users;
use Illuminate\Support\Facades\Config;
use Illuminate\Http\Request;
use Log;
use Session;

/**
 * ログイン処理
 *
 * Class LoginController
 * @package App\Http\Controllers
 */
class LoginController extends Controller
{
    /**
     * 画面表示
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function login(LoginRequest $request) {

        $data = $request->all();

        $loginId = $data["loginId"];
        $password = $data["password"];

        $users = new Users();
        $userData = $users->getUserData($loginId, hash('sha256', $password));
        if(!count($userData) == 1) {
            return view('admin.login')
                ->withErrors(Config::get('message.LOGIN_ERROR'));
        }

        $userRole = new UserRole();
        $roleData = $userRole->getData($loginId);

        $menu = $this->getMenuData($loginId);
        // ユーザーIDを保存する
        $request->session()->put('login', $loginId);
        $request->session()->put('gameOpenFlg', $userData[0]->default_game_open);
        $request->session()->put('menu', $menu);
        $request->session()->put('role', $roleData[0]->role_id);

        return redirect('/top');
    }

    /**
     * ログアウト処理
     * @param Request $request
     * @return mixed
     */
    public function logout(Request $request) {

        $request->session()->flush();
        return redirect('/login');
    }

    /**
     * 捜査権限を取得し表示可能なメニューのリストを作成
     * @param $userId
     * @return array
     */
    private function getMenuData($userId) {

        $userRole = new UserRole();
        $data = $userRole->getAuthSetsData($userId);

        $menu = array();
        foreach ($data as $key => $val) {
            $menu[$val->auth_set_id] = $val->auth_set_id;
        }

        return $menu;
    }
}
