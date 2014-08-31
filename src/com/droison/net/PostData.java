package com.droison.net;

import com.droison.constants.Constant;
import com.droison.util.StringUtil;
import com.droison.net.Base.HttpResponseEntity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class PostData implements Runnable {
	private Context mContext;
	private Handler mHandler;
	private String url;
	private Object obj;
	private String TAG = "PostData";

	public PostData(Context mContext, Handler mHandler, Object obj, int type) {
		this.mContext = mContext;
		this.mHandler = mHandler;
		
		this.obj = obj;
	}

	public void run() {

		Boolean b = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			b = networkInfo.isAvailable();
		}
		if (!b) {
			mHandler.sendEmptyMessage(Constant.HANDLER_MESSAGE_NONETWORK);
			return;
		}
		HttpResponseEntity hre = null;
		
		hre = HTTP.postByHttpUrlConnection(url, obj);
		
		switch (hre.getHttpResponseCode()) {
		
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				Log.e(TAG, "JSON：" + json);
				mHandler.sendMessage(mHandler.obtainMessage(Constant.HANDLER_MESSAGE_NORMAL, json));

			} catch (Exception e) {
				mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
				Log.e(TAG, ";问题：", e);
			}
			break;
		default:
			mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
			Log.d(TAG, "问题：" + hre.getHttpResponseCode());
			break;
		}
	}
}
