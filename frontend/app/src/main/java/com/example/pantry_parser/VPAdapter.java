package com.example.pantry_parser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    /**
     *
     * @param fm
     * @param behavior
     */
    public VPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    /**
     *
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    /**
     *
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
        notifyDataSetChanged();
    }

    /**
     *
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position){

        return fragmentTitle.get(position);
    }

}
