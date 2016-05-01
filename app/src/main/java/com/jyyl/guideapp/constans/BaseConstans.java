package com.jyyl.guideapp.constans;

/**
 * 
 * @ClassName: BaseConstans
 * @Description: 全局常量
 * @author ShangBB
 * @date 2015-11-10 下午1:14:53
 * 
 */
public class BaseConstans {

    public static final String APP_NAME = "APP";

    public static final int RESULT_OK = 0;
    public static final int RESULT_FAILURE = 1;

    /**
     * 几个代表页面的常量
     */
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    /*****
     * 系统相册（包含有 照相、选择本地图片）
     */
    public class SystemPicture{
        /***
         * 保存到本地的目录
         */
        public static final String SAVE_DIRECTORY = "/guide";
        /***
         * 保存到本地图片的名字
         */
        public static final String SAVE_PIC_NAME="photo.jpg";
        /***
         * 裁剪后图片的名字
         */
        public static final String SAVE_CUT_PIC_NAME="photo_icon.jpg";
        /***
         *标记用户点击了从照相机获取图片  即拍照
         */
        public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
        /***
         *标记用户点击了从图库中获取图片  即从相册中取
         */
        public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
        /***
         * 返回处理后的图片
         */
        public static final int PHOTO_REQUEST_CUT = 3;// 结果
    }

}
