package com.jyyl.jinyou.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 安装APK
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载Android应用程序
     */
    public static boolean uninstallApk(Context context, String packageName) {
        if (packageName == null)
            return false;

        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);

        return true;
    }

    /**
     * 对外分享内容
     */
    public static void shareText(Context context, String title, String text,
                                 long appId) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        List<ResolveInfo> ris = getShareTargets(context);
        // LogUtils.d("aaa", ris.size() + "");
        if (ris != null && ris.size() > 0) {
            context.startActivity(Intent.createChooser(intent, title));
        }
    }

    /**
     * 手机里具有"分享"功能的所有应用
     * @return
     */
    public static List<ResolveInfo> getShareTargets(Context context) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pm = context.getPackageManager();
        mApps = pm.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }


    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        final String packageName = context.getPackageName();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * [获取应用程序版本名称信息]
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拨打电话
     */
    public static void call(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 跳转至拨号界面
     */
    public static void callDial(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    /**
     * 发送短信
     */
    public static void sendSms(Context context, String phoneNumber,
                               String content) {
        Uri uri = Uri.parse("smsto:"
                + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

}
