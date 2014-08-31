
package com.droison.updateapp;

import com.droison.constants.Constant;
import com.alibaba.fastjson.JSON;
import com.droison.net.HTTP;
import com.droison.net.Base.HttpResponseEntity;
import com.droison.util.StringUtil;

import android.content.Context;
import android.os.Handler;

public class CheckVersionService implements Runnable {

    private Handler mHandler;

    private Context context;
   
    private String url;

    public CheckVersionService(Context context,Handler mHandler, String url) {
        this.mHandler = mHandler;
        this.context = context;
    }

    public void run() {

    		HttpResponseEntity hre = HTTP.get(url);
    		if(hre == null){
    			mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
    			return;
    		}
    		switch (hre.getHttpResponseCode()) {
    		case 200:
    			try {
    				String json = StringUtil.byte2String(hre.getB());
    				CheckVersionBase cvb = (CheckVersionBase) JSON.parseObject(json, CheckVersionBase.class);

    	            int versionCode = context.getPackageManager().getPackageInfo("com.QuantumFinance.ui", 0).versionCode; 	            

    	            if(!cvb.isResult()){
    	            	mHandler.sendEmptyMessage(Constant.HANDLER_APK_STOP);
    	            }else if (cvb.getVersion() > versionCode) {
    	            	mHandler.sendMessage(mHandler.obtainMessage(Constant.HANDLER_VERSION_UPDATE,cvb));
    	            }
    			} catch (Exception e) {
    				mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
    			}
    			break;
    		default:
    			mHandler.sendEmptyMessage(Constant.HANDLER_HTTPSTATUS_ERROR);
    			break;
    		}
    	
    }

}
