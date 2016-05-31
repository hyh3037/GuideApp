package com.jyyl.jinyou.service.update;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.VersionInfo;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.utils.LogUtils;

import java.util.List;

/**
 * 检查更新
 */
public class UpdateManager {
    /* 保存版本信息 */
    public static VersionInfo versionInfo;

    private Context mContext;

    public UpdateManager(Context context) {
        this.mContext = context;
        checkUpdate();
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        // 获取当前软件版本
        final String versionCode = String.valueOf(getVersionCode(mContext));

        HttpMethods.getInstance().updateVersion(versionCode)
                .subscribe(new BaseSubscriber<List<VersionInfo>>(mContext) {
                    @Override
                    public void onNext(List<VersionInfo> versionInfos) {
                        UpdateManager.versionInfo = versionInfos.get(0);
                        LogUtils.d(versionInfo.toString());
                        showNoticeDialog();
                    }
                });
    }

    /**
     * 获取软件版本号
     * @param context
     *
     * @return
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
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        mContext.startService(new Intent(mContext, UpdateService.class));
                        // 显示下载对话框
                        //showDownloadDialog();
                    }
                });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later,
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }
}