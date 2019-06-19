package com.jetec.wicloud.Pageview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

public class PageAdapter extends PagerAdapter {

    private List<PageView> pageList;

    public PageAdapter(List<PageView> pageList){
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
        //return Integer.MAX_VALUE;   //unlimited circle
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //unlimited circle
        /*try {
            container.addView(pageList.get(position % pageList.size()));
        }catch (Exception ignored){

        }
        return pageList.get(position % pageList.size());*/
        container.addView(pageList.get(position), 0);
        return pageList.get(position);
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(pageList.get(position));
    }
}
