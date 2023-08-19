/*
 * $Id: CryptSetting.java 867 2005-12-27 01:30:02Z koi $
 *
 * Copyright (C) 2004 SEGA Corporation. All rights reserved.
 */

package jp.co.sega_am2.crypt;

/**
 * 暗号設定
 */
public class CryptSetting{
	/**
	 * 開発時のデバッグ用、非暗号モードをセット(Crypt用)
	 */
	public static void setIgnoreParameterEncription(boolean flag) {
		Crypt.setIgnoreParameterEncription(flag);
	}

	/**
	 * 開発時のデバッグ用、非暗号モードを得る
	 */
	public static boolean getIgnoreParameterEncription() {
		return Crypt.isIgnoreParameterEncription();
	}
}
