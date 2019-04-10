package com.example.attendence;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int tabcount;
    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
       this.tabcount=tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: {
                monday one = new monday();
                return one;
            }
            case 1: {
                tuesday two = new tuesday();
                return two;
            }
            case 2:
            {
                wednesday three=new wednesday();
                return three;
            }
            case 3:
            {
                thrusday four=new thrusday();
                return four;
            }
            case 4:
            {
                 friday five=new friday();
                return five;
            }
            case  5:
            {
                 saturday six=new saturday();
                 return six;
            }
            default: {
                return null;
            }
        }
    }
    @Override
    public int getCount() {
        return tabcount;
    }
}
