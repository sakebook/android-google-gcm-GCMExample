package com.sakebook.android.gcmexample;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMReceiver extends GCMBroadcastReceiver{

	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		/**
		 * GCMBaseIntentService���p�����Ă���N���X����n��*/
		return GCMIntentService.class.getName();
	}

}
