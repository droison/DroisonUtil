package com.droison.updateapp;

import java.io.File;

import com.droison.constants.Constant;
import com.droison.library.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateHandler extends Handler {

	/**
	 * 更新进度
	 */
	private ProgressBar mProgress;

	/**
	 * 下载提示框
	 */
	private Dialog downloadDialog;

	private Activity context;

	private String appname;
	private AlertDialog dialog;
	private String appFile;

	public UpdateHandler(Activity context, String appFile, String appName) {
		this.context = context;
		this.appFile = appFile;
		this.appname = appName;
	}

	protected void installApk(File file) {

		Intent intent = new Intent();

		intent.setAction(Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

		context.startActivity(intent);
	}

	@Override
	public void handleMessage(Message mes) {
		switch (mes.what) {
		case Constant.HANDLER_APK_STOP:
			Toast.makeText(context, "您的应用被禁止", Toast.LENGTH_SHORT).show();
			context.finish();
			break;
		case Constant.HANDLER_VERSION_UPDATE:
			CheckVersionBase cvb = (CheckVersionBase) mes.obj;
			final String downloadUrl = cvb.getUrl();
			appname += "-" + cvb.getVersion() + ".apk";

			AlertDialog.Builder builer = new Builder(context);
			builer.setTitle("升级提示");
			builer.setMessage(cvb.getInfo().equals("") ? "新版本发布了，请您更新" : cvb.getInfo());

			builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("量子理财新版本下载更新中");
					final LayoutInflater inflater = LayoutInflater.from(context);
					View v = inflater.inflate(R.layout.update_progress, null);
					mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
					builder.setView(v);
					downloadDialog = builder.create();
					downloadDialog.setCancelable(false);
					downloadDialog.show();
					new Thread(new ApkDownloadService(downloadUrl, UpdateHandler.this,appFile, appname)).start();
				}
			});

			builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					context.finish();
				}
			});
			dialog = builer.create();
			dialog.show();
			break;
		case Constant.HANDLER_APK_DOWNLOAD_PROGRESS:
			mProgress.setProgress((Integer) mes.obj);
			break;
		case Constant.HANDLER_APK_DOWNLOAD_FINISH:
			dialog.dismiss();
			File file = new File(appFile, appname);
			installApk(file);
			break;
		case Constant.HANDLER_HTTPSTATUS_ERROR:
			Log.v("update", "检查失败");
			break;

		}
	}

}