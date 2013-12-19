package com.sakebook.android.gcmexample;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService{

    public GCMIntentService() {
        super(Const.SENDER_ID);
    }
	
    /**
     * GCMサーバ登録成功時に呼ばれる*/
	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		Log.i(Const.TAG, "onRegistered");
		sendRegistarationIdToServer(registrationId);
	}

	
	/**
	 * GCMサーバ登録解除時に呼ばれる*/
	@Override
	protected void onUnregistered(Context context, String arg1) {
		Log.i(Const.TAG, "onUnregistered");
		GCMRegistrar.setRegisteredOnServer(context, false);
	}

	
	/**
	 * エラー時に呼ばれる*/
	@Override
	protected void onError(Context arg0, String arg1) {
		Log.i(Const.TAG, "onError");
	}

	
	/**
	 * Push受信時に呼ばれる*/
	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		Log.i(Const.TAG, "onMessage");
		showToast(arg0, arg1);
	}
	
	
	/**
	 * ここで自前サーバにregIdを送信
	 * 送信成功時に
	 * GCMRegistrar.setRegisteredOnServer(this, true);
	 * を呼ぶ。
	 */
	private void sendRegistarationIdToServer(String regId){
	}
	
	
	/**
	 * Toast表示
	 * test command
	 * 
	 * adb shell am broadcast -a com.google.android.c2dm.intent.RECEIVE -n 
	 * com.sakebook.android.gcmexample/com.sakebook.android.gcmexample.test.GCMReceiver --es "hoge" "huga"
	 * */
	private void showToast(final Context arg0, final Intent arg1){
		final HandlerThread ht = new HandlerThread("");
        ht.start();
        Handler h = new Handler(ht.getLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
        		Toast.makeText(arg0, arg1.getStringExtra("hoge"), Toast.LENGTH_SHORT).show();
            }
        });
	}
}
