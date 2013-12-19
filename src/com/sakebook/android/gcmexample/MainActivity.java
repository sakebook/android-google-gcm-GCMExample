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
		/** �o�[�W���������������ꍇ��regId���قȂ�\��������B */
		if (isNewVersion(currentVersionCode)) {
			/** regId�ɕύX�����邩������Ȃ��̂ŃT�[�o�̓o�^��Ԃ���������
			 * regId����ɂȂ� */
			GCMRegistrar.setRegisteredOnServer(this, false);
		}
		setVersionCode(currentVersionCode);
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		/** regId����̏ꍇ�AGCM�T�[�o�ɓo�^�ł��Ă��Ȃ��̂ŁA
		 * GCMRegistrar.register��@���B */
		((TextView)findViewById(R.id.gcm_text)).setText(regId);
		if (TextUtils.isEmpty(regId)) {
			/** ���ʂ�GCMIntentService.java�Ŏ󂯎�� */
			GCMRegistrar.register(this, Const.SENDER_ID);
		}else {
			/** GCM�o�^�����Ȃ��̂ŁA���ЃT�[�o �̓o�^���Ԃ���������*/
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
	 * ���݂̃o�[�W�����R�[�h���擾*/
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
	 * ���݂̃o�[�W�����R�[�h���v���t�@�ɕۑ�*/
	private void setVersionCode(int currentVersionCode){
		SharedPreferences pref = getSharedPreferences("gcmexample", MODE_PRIVATE);
		pref.edit().putInt("versionCode", currentVersionCode).commit();
	}
	
	
	/**
	 * ���݂̃o�[�W�����R�[�h�ƃv���t�@�ɕۑ����ꂽ�l���r*/
	private boolean isNewVersion(int currentVersionCode){
		SharedPreferences pref = getSharedPreferences("gcmexample", MODE_PRIVATE);
		int oldVersionCode = pref.getInt("versionCode", 0);
		if (currentVersionCode > oldVersionCode) {
			return true;
		}
		return false;
	}
	
	
}
