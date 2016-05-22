package com.example.admin.cellmoblie;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<ImageView> imageViewList;
    private String[] imageDescriptionArrays;
    private ViewPager mViewPage;
    private TextView tvImageDescription;
    private LinearLayout llPointGroup;

    private int previousPosition = 0;
    private boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop){
                    SystemClock.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int newCurrentPosition = mViewPage.getCurrentItem() + 1;
                            mViewPage.setCurrentItem(newCurrentPosition);
                        }
                    });
                }
            }
        }).start();
    }

    private void init() {
        mViewPage = (ViewPager) findViewById(R.id.viewpage);
        tvImageDescription = (TextView) findViewById(R.id.tv_image_descriptioin);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);

        int[] imageResIDs = {
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e};
        imageDescriptionArrays =  new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"};

        imageViewList = new ArrayList<>();
        ImageView iv;
        View view;
        for(int i = 0;i<imageResIDs.length;i++){
            iv = new ImageView(this);
            iv.setBackgroundResource(imageResIDs[i]);
            imageViewList.add(iv);


            view = new View(this);
            view.setBackgroundResource(R.drawable.point_background);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5,5);
            params.leftMargin = 5;
            view.setLayoutParams(params);
            llPointGroup.addView(view);
        }
        MyPageAdapter myPageAdapter = new MyPageAdapter();
        mViewPage.setAdapter(myPageAdapter);

        MyPageChangeListener myPageChangeListener = new MyPageChangeListener();
        mViewPage.setOnPageChangeListener(myPageChangeListener);

        int currentItem = Integer.MAX_VALUE/2-3;
        mViewPage.setCurrentItem(currentItem);
    }
    public class MyPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            mViewPage.addView(imageViewList.get(position%imageViewList.size()));
            return imageViewList.get(position%imageViewList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mViewPage.removeView( imageViewList.get(position%imageViewList.size()));
        }
    }
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int newposition = position % imageViewList.size();
            tvImageDescription.setText(imageDescriptionArrays[newposition]);
            llPointGroup.getChildAt(previousPosition).setEnabled(true);
            llPointGroup.getChildAt(newposition).setEnabled(false);

            previousPosition = newposition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
    }
}
