package com.example.whackamole.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.whackamole.BuildConfig;
import com.example.whackamole.R;
import com.example.whackamole.base.BackHandledInterface;
import com.example.whackamole.base.BaseFragment;
import com.example.whackamole.data.AppData;
import com.example.whackamole.fragment.GameFragment;
import com.example.whackamole.fragment.RankFragment;
import com.example.whackamole.fragment.SettingFragment;
import com.example.whackamole.fragment.StartFragment;

/**
 * Created by magical.zhang on 2018/5/21.
 * Description : 兴趣标签、（游戏主体、打招呼）
 */
public class MainActivity extends AppCompatActivity implements BackHandledInterface {

    private StartFragment mStartFragment;
    private GameFragment mGameFragment;
    private RankFragment mRankFragment;
    private SettingFragment mSettingFragment;
    //    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private Fragment[] mFragmentArr;
    private BaseFragment mBackHandleFragment;

    private long canQuitActivity;    // 实现两次返回键退出应用的功能

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_main);
        initPage();
        changePage(0);
//----------------
//        final LinearLayout test = findViewById(R.id.testLayout);
//        test.setVisibility(View.VISIBLE);
//        final Drawable[] d = PhraseUtils.getPhraseArr(this);
//
//        CountDownTimer t = new CountDownTimer(4000, 100) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                ImageView v = new ImageView(getBaseContext());
//                v.setImageDrawable(d[PhraseUtils.getNextIndex()]);
//                test.addView( v );
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        };
//        t.start();
//-----------------
    }
    @Override
    public void onStart(){
        super.onStart();
        // 检查当前是否登录了账号
        if(AppData.getCurrentAccount() == null){
            if (BuildConfig.DEBUG) Log.d("MainActivity", "检测当前账户为null, 正在启动LoginActivity");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void initPage() {
        mFragmentArr = new Fragment[4];
        mStartFragment = new StartFragment();
        mGameFragment = new GameFragment();
        mRankFragment = new RankFragment();
        mSettingFragment = new SettingFragment();

        mFragmentArr[0] = mStartFragment;
        mFragmentArr[1] = mGameFragment;
        mFragmentArr[2] = mRankFragment;
        mFragmentArr[3] = mSettingFragment;
    }

    public void changePage(int index) {
        try {
            Fragment fragment = mFragmentArr[index];
//            if(index == 1){
//                fragment = new GameFragment();
//            }
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

    @Override   // 返回键操作
    public void setCurrentTopFragment(BaseFragment topFragment) {
        mBackHandleFragment = topFragment;
    }

    @Override   // 返回键操作
    public void onBackPressed() {
        if (mBackHandleFragment.needHandleBackPress()) {
            canQuitActivity = System.currentTimeMillis();
            changePage(0);
        }
        if (mBackHandleFragment == mStartFragment || mBackHandleFragment == mGameFragment) {
            if (System.currentTimeMillis() - canQuitActivity < 300) {
                this.finish();
            } else {
                Toast.makeText(this, "双击返回键退出App", Toast.LENGTH_SHORT).show();
                canQuitActivity = System.currentTimeMillis();
            }
        }
    }
}
