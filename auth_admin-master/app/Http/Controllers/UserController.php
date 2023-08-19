<?php

namespace App\Http\Controllers;

use App\Http\Requests\PassUpdateRequest;
use App\Http\Requests\UserRegistRequest;
use App\Http\Requests\UserUpdateRequest;
use app\Library\Util;
use App\Models\GameCompetences;
use App\Models\Games;
use App\Models\Roles;
use App\Models\UserRole;
use App\Models\Users;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Config;

/**
 * ユーザ管理
 *
 * Class UserController
 * @package App\Http\Controllers
 */
class UserController extends Controller
{
    /**
     * 画面表示
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function top()
    {
        $users = new Users();
        return view('admin.manage.user.top')
            ->with('userdata', $users->getFindData(30))
            ->with('search', '0');
    }

    /**
     * ユーザ検索
     * @param Request $request
     * @return $this
     */
    public function search(Request $request) {

        $userID = null;
        $invalided = null;

        if($request->has('searchUserId')) {
            $userID = $request->input('searchUserId');
            $request->session()->put('userid', $userID);
        } else {
            if($request->session()->has('userid')) {
                $userID = $request->session()->get('userid');
            }
        }

        if($request->has('onlyFreeze')) {
            $invalided = $request->input('onlyFreeze');
        }
        $users = new Users();
        return view('admin.manage.user.top')
            ->with('userdata', $users->getFindData(30, $userID, $invalided))
            ->with('search', '1')
            ->with('searchUserId', $userID)
            ->with('onlyFreeze', $invalided);
    }

    /**
     * 新規登録
     * @return $this
     */
    public function regist() {

        $game = new Games();
        $role = new Roles();

        return view('admin.manage.user.regist')
            ->with('roleData', $role->getNonSuperData())
            ->with('defaultGameOpen', 1);
    }

    /**
     * 新規登録確認
     *
     * @param UserRegistRequest $request
     * @return $this
     */
    public function reg_confirm(UserRegistRequest $request) {

        $data = $request->all();
        $request->session()->put($data);

        $game = new Games();
        $role = new Roles();

        $gameArray = array();

        $data = array_merge($data,array('roleData'=>$role->getData($data['roleId'])));
        $data = array_merge($data,array('confirming'=>'false'));

        return view('admin.manage.user.reg_confirm')
            ->with('data', $data);
    }

    /**
     * 新規登録処理
     *
     * @param Request $request
     * @return $this
     */
    public function reg_process(Request $request)
    {
        $input = $request->except('action');
        // 戻るボタン押下時処理
        if ($request->get('action') === 'back') {
            return redirect()->action('UserController@regist')->withInput($input);
        } else if ($request->get('action') === 'end') {
            return redirect('user/top');
        }

        $data = $request->all();

        $data = array_merge($data,array('confirming'=>'true'));

        $users = new Users();
        $userRole = new UserRole();
        $gameCom = new GameCompetences();
        $role = new Roles();

        $users->dataSave($data['userId'], $data['password']);
        $userRole->dataSave($data['userId'], $data['roleId']);

        $data = array_merge($data,array('roleData'=>$role->getData($data['roleId'])));

        return view('admin.manage.user.reg_confirm')
            ->with('data', $data);
    }

    /**
     * 更新
     *
     * @return $this
     */
    public function update(Request $request) {

        $userId = null;
        if($request->has('userId')) {
            $userId = $request->input('userId');
        } else {
            $userId = $request->old('userId');
        }

        $role = new Roles();
        $users = new Users();
        $userRole = new UserRole();

        $userData = $users->getData($userId);
        $userRoleDate =  $userRole->getData($userId);

        if(count($userRoleDate)<1){
            $obj = new \stdClass();
            $obj->role_id = "";
            $userRoleDate[0] = $obj;
        }

        return view('admin.manage.user.update')
            ->with('data', $userData[0])
            ->with('userRoleDate', $userRoleDate[0])
            ->with('roleData', $role->getNonSuperData())
            ->with('returnFlg', 1);
    }

    /**
     * 更新確認
     *
     * @param UserUpdateRequest $request
     * @return $this
     */
    public function upd_confirm(UserUpdateRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));
        $request->session()->put($data);

        $role = new Roles();

        $data = array_merge($data,array('roleData'=>$role->getData($data['roleId'])));
        $data = array_merge($data,array('confirming'=>'false'));

        return view('admin.manage.user.upd_confirm')
            ->with('data', $data);
   }

    /**
     * 更新処理
     *
     * @param Request $request
     * @return $this
     */
    public function upd_process(Request $request) {

        $input = $request->except('action');
        // 戻るボタン押下時処理
        if ($request->get('action') === 'back') {
            return redirect()->action('UserController@update')->withInput($input);
        } else if ($request->get('action') === 'end') {
            return redirect('user/top');
        }

        $data = $request->all();

        $data = array_merge($data,array('confirming'=>'true'));

        $users = new Users();
        $userRole = new UserRole();
        $role = new Roles();

        $users->dataSave($data['userId'], $data['password']);
        $userRole->dataSave($data['userId'], $data['roleId']);

        $data = array_merge($data,array('roleData'=>$role->getData($data['roleId'])));

        return view('admin.manage.user.upd_confirm')
            ->with('data', $data);
    }


    /**
     * 更新
     *
     * @return $this
     */
    public function pass_update(Request $request) {

        $userId = $request->session()->get('login');

        $users = new Users();

        $userData = $users->getData($userId);

        return view('admin.manage.user.pass_update')
            ->with('data', $userData[0]);
    }

    /**
     * 更新確認
     *
     * @param PassUpdateRequest $request
     * @return $this
     */
    public function pass_confirm(PassUpdateRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));
        $request->session()->put($data);

        return view('admin.manage.user.pass_confirm')
            ->with('data', $data);
    }

    /**
     * 更新処理
     *
     * @param Request $request
     * @return $this
     */
    public function pass_process(Request $request) {

        $input = $request->except('action');
        // 戻るボタン押下時処理
        if ($request->get('action') === 'back') {
            return redirect()->action('UserController@pass_update')->withInput($input);
        } else if ($request->get('action') === 'end') {
            return redirect('/top');
        }

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'true'));

        $users = new Users();
        $users->passSave($data['userId'], $data['password']);

        return view('admin.manage.user.pass_confirm')
            ->with('data', $data);
    }

    /**
     * 凍結処理
     *
     * @param Request $request
     * @return $this
     */
    public function freeze(Request $request) {

        $data = $request->all();

        $users = new Users();
        $users->updateInvelided($data['userId'], $data['invalided']);

        return redirect('user/top');
    }

    /**
     * 削除処理
     *
     * @param Request $request
     * @return $this
     */
    public function delete(Request $request) {

        $data = $request->all();

        $users = new Users();
        $userRole = new UserRole();

        $userRole->deleteUserData($data['userId']);
        $users->deleteUserData($data['userId']);

        return redirect('user/top');
    }
}
