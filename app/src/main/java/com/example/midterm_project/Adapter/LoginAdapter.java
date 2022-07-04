package com.example.midterm_project.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.midterm_project.LoginFragment;
import com.example.midterm_project.SignupFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public  LoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                LoginFragment loginTabFragment = new LoginFragment();
                return loginTabFragment;
            case 1:
                SignupFragment signupTabFragment = new SignupFragment();
                return signupTabFragment;
            default:
                return null;
        }
    }
}
