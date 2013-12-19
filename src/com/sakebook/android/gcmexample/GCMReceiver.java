package com.sakebook.android.gcmexample;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMReceiver extends GCMBroadcastReceiver{

	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		/**
		 * GCMBaseIntentServiceを継承しているクラス名を渡す*/
		return GCMIntentService.class.getName();
	}

}
