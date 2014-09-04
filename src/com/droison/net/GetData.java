package com.droison.net;

import com.droison.constants.Constant;
import com.droison.util.StringUtil;
import com.droison.net.Base.HttpResponseEntity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class GetData implements Runnable {
	private Activity mContext;
	private Handler mHandler;
	private String url;
	private String TAG = "GetList";

	public GetData(Activity mContext, Handler mHandler, String url) {
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.url = url;
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

		HttpResponseEntity hre = HTTP.get(url);
		if (hre == null) {
			mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
			return;
		}
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				mHandler.sendMessage(mHandler.obtainMessage(Constant.HANDLER_MESSAGE_NORMAL, json));
			} catch (Exception e) {
				mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("StringGet", "200", e);
			}
			break;
		default:
			mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("StringGet", url + hre.getHttpResponseCode());
			break;
		}
	}

}
