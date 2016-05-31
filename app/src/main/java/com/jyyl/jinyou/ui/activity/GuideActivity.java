package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShangBB
 * @Description: (引导页)
 * @date 2016/1/20 0020 下午 2:11
 */
public class GuideActivity extends BaseActivity {

    // 引导页图片
    private int mImages[] = {
            R.drawable.splash_bg,
            R.drawable.splash_bg,
            R.drawable.splash_bg,
            R.drawable.splash_bg,
    };

    /**
     * 装分页显示的view的数组
     */
    private List<View> pageViews;
    private Context mContext;

    /**
     * 将小圆点的图片用数组表示
     */
    private ImageView[] imageViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mContext = this;

        //禁用SwipeBackLayout
        setSwipeBackEnable(false);

        initGuide();
    }

    /**
     * 设置分页和小圆点
     */
    private void initGuide() {

        // 将要分页显示的View装入数组中
        pageViews = new ArrayList<>();
        for (int i=0; i<mImages.length; i++){
            if (i == mImages.length-1){
                setPageViews(R.layout.guide_page_last, mImages[i]);
            }else {
                setPageViews(R.layout.guide_page, mImages[i]);
            }
        }

        // 创建imageviews数组，大小是要显示的图片的数量
        imageViews = new ImageView[pageViews.size()];

        // 实例化小圆点的linearLayout和viewpager
        ViewGroup viewPoints = (ViewGroup) findViewById(R.id.viewGroup_point);
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_pages);

        // 添加小圆点的图片
        for (int i = 0; i < pageViews.size(); i++) {
            // 设置小圆点imageview的参数
            // imageView = new ImageView(WelcomeActivity.this);
            // imageView.setPadding(20, 0, 20, 0);
            // imageView.setLayoutParams(new LayoutParams(30, 30));//
            // 创建一个宽高均为20的布局
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    30, 30);
            params.setMargins(0, 0, 20, 0);
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setLayoutParams(params);
            // 将小圆点layout添加到数组中
            imageViews[i] = imageView;

            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.drawable.skin_tabbar_dot_selected);
            } else {
                imageViews[i]
                        .setBackgroundResource(R.drawable.skin_tabbar_dot_normal);
            }

            // 将imageviews添加到小圆点视图组
            viewPoints.addView(imageViews[i]);
        }

        // 设置viewpager的适配器和监听事件
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.addOnPageChangeListener(new GuidePageChangeListener());
    }

    /**
     * 设置引导页图片
     * @param resid 资源id
     */
    private void setPageViews(int resource, int resid){
        View page = getLayoutInflater().inflate(resource, null);
        page.setBackgroundResource(resid);
        pageViews.add(page);
    }

    /**
     * Viewpager Adapter
     */
    class GuidePageAdapter extends PagerAdapter {
        // 获取当前窗体界面数
        @Override
        public int getCount() {
            return pageViews.size();
        }

        // 销毁position位置的界面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageViews.get(position));
            // 最后一个引导界面的按钮事件
            if (position == pageViews.size() - 1) {
                TextView guideEnd = (TextView) container.findViewById(R.id.guide_end);
                guideEnd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isLogin = (boolean) SPUtils.
                                get(mContext, Sp.SP_KEY_LOGIN_STATE, false);
                        if (isLogin) {
                            openActivity(mContext, MainActivity.class);
                        } else {
                            openActivity(mContext, LoginActivity.class);
                        }
                        GuideActivity.this.finish();
                    }
                });
            }
            return pageViews.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    /**
     * 设置页面切换小圆点的显示状态
     */
    class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position]
                        .setBackgroundResource(R.drawable.skin_tabbar_dot_selected);
                // 不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.skin_tabbar_dot_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_alpha_out);
    }

}
