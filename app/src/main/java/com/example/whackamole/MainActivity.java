package com.example.whackamole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.example.whackamole.base.BackHandledInterface;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.fragment.GameFragment;
import com.example.whackamole.fragment.RankFragment;
import com.example.whackamole.fragment.SettingFragment;
import com.example.whackamole.fragment.StartFragment;

import java.util.ArrayList;

/**
 * Created by magical.zhang on 2018/5/21.
 * Description : 兴趣标签、（游戏主体、打招呼）
 */
public class MainActivity extends AppCompatActivity implements BackHandledInterface {

    private StartFragment mStartFragment;
    private GameFragment mGameFragment;
    private RankFragment mRankFragment;
    private SettingFragment mSettingFragment;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private BaseFragment mBackHandleFragment;

    private long canQuitActivity;    // 实现两次返回键退出应用的功能
    private int score;
    public int getScore() {
        return score;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_main);

        initPage();
        changePage(0);
    }

    private void initPage() {
        mFragmentList.clear();
        mStartFragment = new StartFragment();
        mGameFragment = new GameFragment();
//        mGameFragment = null;
        mRankFragment = new RankFragment();
        mSettingFragment = new SettingFragment();

        mFragmentList.add(mStartFragment);  // 0
        mFragmentList.add(mGameFragment);   // 1
        mFragmentList.add(mRankFragment);   // 2
        mFragmentList.add(mSettingFragment);// 3
    }

    public void changePage(int index) {
        try {
            Fragment fragment = mFragmentList.get(index);
            if (null != fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.id_game_root, fragment);
                fragmentTransaction.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setCurrentTopFragment(BaseFragment topFragment) {
        mBackHandleFragment = topFragment;
    }

    @Override
    public void onBackPressed() {
        if(mBackHandleFragment.needHandleBackPress()){
            canQuitActivity = System.currentTimeMillis();
            changePage(0);
        }
        if(mBackHandleFragment == mStartFragment || mBackHandleFragment == mGameFragment){
            if(System.currentTimeMillis() - canQuitActivity < 300){
                this.finish();
            }else {
                Toast.makeText(this, "双击返回键退出App", Toast.LENGTH_SHORT).show();
                canQuitActivity = System.currentTimeMillis();
            }
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //游戏页屏蔽返回键
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
