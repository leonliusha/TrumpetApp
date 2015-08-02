package com.skyline.trumpet.trumpetapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.skyline.trumpet.trumpetapp.common.Constant;
import com.skyline.trumpet.trumpetapp.common.UserLocalStore;

import java.util.List;

public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener{
    private List<Fragment> fragments;
    private RadioGroup rgs;
    private AppCompatActivity mainActivity;
    private int fragmentContentId;
    private int currentTab;
    private UserLocalStore userLocalStore;
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;

    public FragmentTabAdapter(AppCompatActivity mainActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.mainActivity = mainActivity;
        this.fragmentContentId = fragmentContentId;
        userLocalStore = new UserLocalStore(mainActivity);
        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        //home fragment is the default fragment
        ft.add(fragmentContentId, fragments.get(Constant.HOME_FRAGMENT));
        currentTab = Constant.HOME_FRAGMENT;
        ft.commit();

        rgs.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//        for(int i = 0; i < rgs.getChildCount(); i++){
//            if(rgs.getChildAt(i).getId() == checkedId){
//                Fragment fragment = fragments.get(i);
//                FragmentTransaction ft = obtainFragmentTransaction(i);
//
//                getCurrentFragment().onPause();
////                getCurrentFragment().onStop();
//
//                if(fragment.isAdded()){
////                    fragment.onStart();
//                    fragment.onResume();
//                }else{
//                    ft.add(fragmentContentId, fragment);
//                }
//                showTab(i);
//                ft.commit();
//
//                if(null != onRgsExtraCheckedChangedListener){
//                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, i);
//                }
//
//            }
//        }

        switch (checkedId){
            case R.id.tab_rb_home:
                changeTab(fragments.get(Constant.HOME_FRAGMENT),Constant.HOME_FRAGMENT);
                break;
            case R.id.tab_rb_b:
                changeTab(fragments.get(1),1);
                break;
            case R.id.tab_rb_broadcast:
                if(!userLocalStore.getLoginStatus())
                    mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                else
                    changeTab(fragments.get(Constant.BROADCAST_FRAGMENT), Constant.BROADCAST_FRAGMENT);
                break;
            case R.id.tab_rb_nearby:
                changeTab(fragments.get(Constant.NEARBY_FRAGMENT),Constant.NEARBY_FRAGMENT);
                break;
            case R.id.tab_rb_me:
                changeTab(fragments.get(Constant.USER_HOME_PAGE_FRAGMENT),Constant.USER_HOME_PAGE_FRAGMENT);

        }

    }


    private void changeTab(Fragment fragment,int index){
        FragmentTransaction ft = obtainFragmentTransaction(index);
        fragments.get(currentTab).onPause();
        if(fragment.isAdded())
            fragment.onResume();
        else
            ft.add(fragmentContentId,fragment);
        showTab(index);
        ft.commit();
    }

    /**
     * 切换tab
     * @param index
     */
    private void showTab(int index){
//        for(int i = 0; i < fragments.size(); i++){
//            Fragment fragment = fragments.get(i);
//            FragmentTransaction ft = obtainFragmentTransaction(idx);
//
//            if(idx == i){
//                ft.show(fragment);
//            }else{
//                ft.hide(fragment);
//            }
//            ft.commit();
//        }
//        currentTab = idx;

        FragmentTransaction ft = obtainFragmentTransaction(index);
        ft.hide(fragments.get(currentTab));
        ft.show(fragments.get(index));
        ft.commit();
        currentTab = index;
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index){
        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();

        if(index > currentTab){
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }else{
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment(){
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }


    static class OnRgsExtraCheckedChangedListener{
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index){

        }
    }

}
