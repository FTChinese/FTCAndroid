package com.ft.tool;

import org.apache.cordova.DroidGap;

import com.ft.cordovaFT;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class Dialogtoo {
	public void exitdialog(final DroidGap cordova) {
		AlertDialog.Builder builder = new Builder(cordova);
		builder.setMessage("��ȷ��Ҫ�˳�FT��������?");
		// builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());
						cordova.finish();
					}
				});
		builder.setNegativeButton("ȡ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
}
