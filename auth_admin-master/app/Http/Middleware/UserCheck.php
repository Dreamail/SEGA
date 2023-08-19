<?php

namespace App\Http\Middleware;

use App\Models\Roles;
use App\Models\UserRole;
use App\Models\Users;
use Closure;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Config;

/**
 * ログイン状況チェック
 * Class UserCheck
 * @package App\Http\Middleware
 */
class UserCheck
{
    /**
     * ログイン情報チェック
     * @param $request
     * @param Closure $next
     * @param $role
     * @return mixed
     */
    public function handle($request, Closure $next, ...$role)
    {
        if(!$request->session()->has('login')) {
            return redirect('/login');
        }

        $users = new Users();
        $roles = new UserRole();

        // ユーザIDを取得
        $loginId = $request->session()->get('login');

        if(!$users->loginUserCheck($loginId)) {
            $request->session()->flush();
            return redirect('/login')->withErrors(Config::get('message.USER_EXI_ERROR'));
        }

        $roleData = $roles->getData($loginId);
        if(!empty($role) && $roleData[0]->role_id !== 'SUPERUSER') {
            $flg = true;
            foreach ($role as $row) {
                if (isset($request->session()->get('menu')[$row])) {
                    $flg = false;
                }
            }
            if($flg) {
                return redirect('/top')->withErrors(Config::get('message.ROLE_ERROR'));
            }
        }

        $request->session()->regenerate();

        return $next($request);
    }
}
