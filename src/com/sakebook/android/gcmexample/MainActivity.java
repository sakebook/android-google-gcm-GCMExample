package com.sakebook.android.gcmexample;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int currentVersionCode = getCurrentVersionCode();
		/** バージョンが挙がった場合はregIdが異なる可能性がある。 */
		if (isNewVersion(currentVersionCode)) {
			/** regIdに変更があるかもしれないのでサーバの登録状態を解除する
			 * regIdも空になる */
			GCMRegistrar.setRegisteredOnServer(this, false);
		}
		setVersionCode(currentVersionCode);
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		/** regIdが空の場合、GCMサーバに登録できていないので、
		 * GCMRegistrar.registerを叩く。 */
		((TextView)findViewById(R.id.gcm_text)).setText(regId);
		if (TextUtils.isEmpty(regId)) {
			/** 結果をGCMIntentService.javaで受け取る */
			GCMRegistrar.register(this, Const.SENDER_ID);
		}else {
			/** GCM登録が問題ないので、自社サーバ の登録期間を延長する*/
			GCMRegistrar.setRegisteredOnServer(this, true);
			GCMRegistrar.setRegisterOnServerLifespan(this, GCMRegistrar.DEFAULT_ON_SERVER_LIFESPAN_MS);
		}
	}

	
	@Override
	protected void onDestroy() {
		GCMRegistrar.onDestroy(this);
		super.onDestroy();
	}


	/**
	 * 現在のバージョンコードを取得*/
	private int getCurrentVersionCode(){
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo.versionCode;
	}
	
	
	/**
	 * 現在のバージョンコードをプリファに保存*/
	private void setVersionCode(int currentVersionCode){
		SharedPreferences pref = getSharedPreferences("gcmexample", MODE_PRIVATE);
		pref.edit().putInt("versionCode", currentVersionCode).commit();
	}
	
	
	/**
	 * 現在のバージョンコードとプリファに保存された値を比較*/
	private boolean isNewVersion(int currentVersionCode){
		SharedPreferences pref = getSharedPreferences("gcmexample", MODE_PRIVATE);
		int oldVersionCode = pref.getInt("versionCode", 0);
		if (currentVersionCode > oldVersionCode) {
			return true;
		}
		return false;
	}
	
	
}
