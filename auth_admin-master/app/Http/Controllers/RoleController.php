<?php

namespace App\Http\Controllers;

use App\Http\Requests\RoleRegistRequest;
use App\Http\Requests\RoleSearchRequest;
use App\Http\Requests\RoleUpdateRequest;
use App\Models\AuthSets;
use App\Models\UserRole;
use Illuminate\Http\Request;
use App\Models\RoleAuthSets;
use App\Models\Roles;
use Illuminate\Support\Facades\Config;
use Log;

/**
 * 権限管理
 *
 * Class RoleController
 * @package App\Http\Controllers
 */
class RoleController extends Controller
{
    /**
     * 画面表示
     * @return \Illuminate\Contracts\View\Factory|\Illuminate\View\View
     */
    public function top()
    {
        $role = new Roles();

        $roleData = $this->getRoleAuthSets($role->getNonSuperUserData(30));

        return view('admin.manage.role.top')
            ->with('roleData', $roleData)
            ->with('search', '0');
    }

    /**
     * 権限検索
     * @param Request $request
     * @return $this
     */
    public function search(Request $request) {

        $roleId = null;
        $role = new Roles();
        if($request->has('searchRoleId')) {
            $roleId = $request->input('searchRoleId');
            $request->session()->put('searchRoleId', $roleId);
        } else {
            if($request->session()->has('searchRoleId')) {
                $roleId = $request->session()->get('searchRoleId');
            }
        }

        $roleData = null;
        if(!empty($roleId)) {
            $roleData = $this->getRoleAuthSets($role->getData($roleId));
        } else {
            $roleData = $this->getRoleAuthSets($role->getNonSuperUserData(30));
        }

        return view('admin.manage.role.top')
            ->with('roleData', $roleData)
            ->with('search', '1')
            ->with('searchRoleId', $roleId);
    }

    /**
     * 新規登録
     *
     * @return $this
     */
    public function regist() {

        $authSets = new AuthSets();

        return view('admin.manage.role.regist')
            ->with('authSets', $authSets->getNotManData());
    }

    /**
     * 新規登録確認
     *
     * @param RoleRegistRequest $request
     * @return $this
     */
    public function reg_confirm(RoleRegistRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));
        $request->session()->put($data);

        $authSets = new AuthSets();

        $authSetsArray = array();
        foreach ($data['authSetIds'] as $key => $row) {
            $authId = $authSets->getData($row);
            if(!empty($authId)) {
                $authSetsArray[] = $authId[0];
            }
        }

        return view('admin.manage.role.reg_confirm')
            ->with('data', $data)
            ->with('authSets', $authSetsArray);
    }

    /**
     * 新規登録処理
     *
     * @param Request $request
     * @return $this
     */
    public function reg_process(Request $request) {

        $input = $request->except('action');
        // 戻るボタン押下時処理
        if ($request->get('action') === 'back') {
            return redirect()->action('RoleController@regist')->withInput($input);
        } else if($request->get('action') === 'end') {
            return redirect('role/top');
        }

        $data = $request->all();

        // ロールIDを生成

        $role = new Roles();
        $role->dataSave($data['roleId'], $data['explanation']);

        $roleAuth = new RoleAuthSets();

        $authSets = new AuthSets();
        $roleAuth->deleteRoleAuthData($data['roleId']);

        $authSetsArray = array();
        foreach ($data['authSetIds'] as $key => $row) {
            $authId = $authSets->getData($row);
            if(!empty($authId)) {
                $authSetsArray[] = $authId[0];
                $roleAuth->dataSave($data['roleId'], $row);
            }
        }

        $data = array_merge($data,array('confirming'=>'true'));

        return view('admin.manage.role.reg_confirm')
            ->with('data', $data)
            ->with('authSets', $authSetsArray);
    }

    /**
     * 更新
     *
     * @return $this
     */
    public function update(Request $request) {

        $roleId = null;
        if($request->has('roleId')) {
            $roleId = $request->input('roleId');
        } else {
            $roleId = $request->old('roleId');
        }

        $role = new Roles();
        $authSets = new AuthSets();
        $roleAuth = new RoleAuthSets();

        $roleData = $role->getData($roleId);
        $authSetsData =  $authSets->getNotManData();
        $roleAuthData = $roleAuth->getDataRoleId($roleId);

        foreach ($authSetsData as $key => $row) {
            foreach ($roleAuthData as $key2 => $row2) {
                if (strcmp($row2->auth_set_id, $row->auth_set_id) == 0) {
                    $row->check = true;
                    $authSetsData[$key] = $row;
                }
            }
        }
        return view('admin.manage.role.update')
            ->with('data', $roleData[0])
            ->with('authSets', $authSetsData);
    }

    /**
     * 更新確認
     *
     * @param RoleUpdateRequest $request
     * @return $this
     */
    public function upd_confirm(RoleUpdateRequest $request) {

        $data = $request->all();
        $data = array_merge($data,array('confirming'=>'false'));
        $request->session()->put($data);

        $authSets = new AuthSets();

        $authSetsArray = array();
        foreach ($data['authSetIds'] as $key => $row) {
            $authId = $authSets->getData($row);
            if(!empty($authId)) {
                $authSetsArray[] = $authId[0];
            }
        }

        $data = array_merge($data,array('confirming'=>'false'));

        return view('admin.manage.role.upd_confirm')
            ->with('data', $data)
            ->with('authSets', $authSetsArray);
    }

    /**
     * 更新処理
     *
     * @param Request $request
     * @return $this
     */
    public function upd_process(Request $request) {

        $data = $request->all();
        $input = $request->except('action');
        // 戻るボタン押下時処理
        if ($request->get('action') === 'back') {
            return redirect()->action('RoleController@update')->withInput($data);
        } else if($request->get('action') === 'end') {
            return redirect('role/top');
        }

        $role = new Roles();
        $role->dataSave($data['roleId'], $data['explanation']);

        $roleAuth = new RoleAuthSets();

        $authSets = new AuthSets();
        $roleAuth->deleteRoleAuthData($data['roleId']);

        $authSetsArray = array();

        foreach ($data['authSetIds'] as $key => $row) {
            $authId = $authSets->getData($row);
            if(!empty($authId)) {
                $authSetsArray[] = $authId[0];
                $roleAuth->dataSave($data['roleId'], $row);
            }
        }

        $data = array_merge($data,array('confirming'=>'true'));

        return view('admin.manage.role.upd_confirm')
            ->with('data', $data)
            ->with('authSets', $authSetsArray);
    }

    /**
     * 削除処理
     *
     * @param Request $request
     * @return $this
     */
    public function delete(Request $request) {

        $data = $request->all();
        $role = new Roles();
        $userRole = new UserRole();

        $dataCount = $userRole->getRoleCount($data['roleId']);
        if($dataCount > 0) {
            $roleData = $this->getRoleAuthSets($role->getNonSuperUserData(30));

            return view('admin.manage.role.top')
                ->withErrors(Config::get('message.ROLE_USER_ERROR'))
                ->with('roleData', $roleData)
                ->with('search', '1');
        }

        $role->deleteRoleData($data['roleId']);

        $roleAuth = new RoleAuthSets();
        $roleAuth->deleteRoleAuthData($data['roleId']);

        return redirect('role/top');
    }

    /**
     * 操作権限名を取得し設定
     * @param $roleData
     * @return mixed
     */
    private function getRoleAuthSets($roleData) {

        $roleAuthSets = new RoleAuthSets();

        foreach ($roleData as $key => $row) {

            $authSets = $roleAuthSets->getDataByRoleId($row->role_id);
            $row->auth_sets = $authSets;

            $roleData[$key] = $row;
        }
        return $roleData;
    }
}
