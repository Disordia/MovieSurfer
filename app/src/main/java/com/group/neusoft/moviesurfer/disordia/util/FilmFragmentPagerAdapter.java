package com.group.neusoft.moviesurfer.disordia.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.group.neusoft.moviesurfer.R;

/**
 * Created by ttc on 2017/3/13.
 */

public class FilmFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Home","Collect","History"};
    private Context mContext;
    private FilmListFragment mFilmListFragment;
    private int[] imageResId = {
            R.drawable.ic_home,
            R.drawable.ic_collection,
            R.drawable.ic_history
    };


    public FilmFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        mContext=context;
        mFilmListFragment=new FilmListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        LogUtil.print(position+"tapped");
        if(position==1){
            return new CollctionFragment();
        }
        if(position==2){
            return new HistoryFragment();
        }
        return mFilmListFragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = mContext.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, (int) (image.getIntrinsicWidth()*0.6), (int) (image.getIntrinsicHeight()*0.6));
        SpannableString sb = new SpannableString("  " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}
