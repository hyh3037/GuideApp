package com.jyyl.guideapp.jpush;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jyyl.guideapp.MyApplication;
import com.jyyl.guideapp.entity.NoticeInfo;
import com.jyyl.guideapp.utils.ACache;
import com.jyyl.guideapp.utils.LogUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * @Fuction: 通知缓存
 * @Author: Shang
 * @Date: 2016/5/25  14:48
 */
public class NoticeCache {
    private static String TAG = "NoticeCache";
    private static String noticeChcheKey= "NoticeCacheKey";
    private ACache mCache;

    private static volatile NoticeCache instance = null;

    private NoticeCache() {
        mCache = ACache.get(MyApplication.getInstance().getApplicationContext());
    }

    /**
     * 获取单例
     * @return 实例
     */
    public static NoticeCache getInstance() {

        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (NoticeCache.class) {
                if (instance == null) {
                    instance = new NoticeCache();
                }
            }
        }
        return instance;
    }

    /**
     * 新增一个 通知 到缓存中
     * @param noticeInfo
     */
    public void addNoticeCache(NoticeInfo noticeInfo){
        LinkedList<NoticeInfo> noticeInfos = getNoticeCache();
        if (noticeInfos == null){
            noticeInfos = new LinkedList<>();
        }
        noticeInfos.addFirst(noticeInfo);
        saveNotcieCache(noticeInfos);
    }

    /**
     * 保存通知集合到缓存中
     * 用于批量删除
     * @param noticeInfos
     */
    public void saveNotcieCache(LinkedList<NoticeInfo> noticeInfos){
        clear();
        String cacheJsonString = new Gson().toJson(noticeInfos);
        LogUtils.d(TAG, cacheJsonString);
        mCache.put(noticeChcheKey,cacheJsonString);
    }

    /**
     * 获取缓存中的通知
     */
    public LinkedList<NoticeInfo> getNoticeCache() {
        LinkedList<NoticeInfo> noticeInfos;
        String cacheJsonString = mCache.getAsString(noticeChcheKey);
        LogUtils.d(TAG, cacheJsonString);

        if (cacheJsonString != null){
            Type listType = new TypeToken<LinkedList<NoticeInfo>>(){}.getType();
            noticeInfos = new Gson().fromJson(cacheJsonString,listType);
            return noticeInfos;
        }
        return null;
    }

    /**
     * 清空通知缓存
     */
    public void clear() {
        mCache.remove(noticeChcheKey);
    }
}
