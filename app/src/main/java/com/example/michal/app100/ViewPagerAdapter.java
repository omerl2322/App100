package com.example.michal.app100;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.text.SpannableString;

import java.util.ArrayList;

/**
 * Created by MichaL on 22/11/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();

    public void addFragments(Fragment fragments ,
                             String tabTitles){
        this.fragments.add(fragments);
        this.tabTitles.add(tabTitles);
    }

    public ViewPagerAdapter(FragmentManager fm)
    {
      super(fm);
    }

    public Fragment getItem(int position){
        return fragments.get(position) ;
    }


    public int getCount(){
        return fragments.size() ;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }


}
