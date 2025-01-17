package com.onesoft.truyenqq;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.kobakei.ratethisapp.RateThisApp;
import com.onesoft.truyenqq.fragment.FragmentCate;
import com.onesoft.truyenqq.fragment.FragmentFav;
import com.onesoft.truyenqq.fragment.FragmentNew;
import com.onesoft.truyenqq.fragment.FragmentUser;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_SURATION = 500; //0.5 seconds
    Fragment fragment = null;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rateMe();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new DotProgressBarBuilder(getBaseContext())
                        .setAnimationTime(500)
                        .build();

                loadFragment(new FragmentNew());
            }
        }, SPLASH_SURATION);

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new:
                    fragment = new FragmentNew();
                    break;

                case R.id.navigation_categories:
                    fragment = new FragmentCate();
                    break;

                case R.id.navigation_favorite:
                    fragment = new FragmentFav();
                    break;

                case R.id.navigation_user:
//                    dao = new FavDAO(getBaseContext());
//                    list = dao.viewAll();
//
//                    if (list.size() == 0){
//                        fragment = new FragmentNoFav();
//                    } else {
//                        fragment = new FragmentFavorite();
//                    }
                    fragment = new FragmentUser();

                    break;

            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (fragment != null) {
            getSupportFragmentManager().popBackStack();
            transaction.replace(R.id.fragment, fragment);
            transaction.addToBackStack(null);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            transaction.commit();
            return true;
        }
        return false;
    }

    private void rateMe(){
        //config RateThisApp
        RateThisApp.Config config = new RateThisApp.Config(1, 1);
        RateThisApp.init(config);

        config = new RateThisApp.Config();
        config.setTitle(R.string.rate_1);
        config.setMessage(R.string.rate_2);
        config.setYesButtonText(R.string.rate_yes);
        config.setNoButtonText(R.string.rate_no);
        config.setCancelButtonText(R.string.rate_later);
        RateThisApp.init(config);

        //Create & check if needed show
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
    }


    @Override
    protected void onStart() {
        rateMe();
        super.onStart();
    }

    @Override
    protected void onResume() {
        rateMe();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        moveTaskToBack(true);
        finish();
        super.onDestroy();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.raw.no_internet)
                .setTitle(R.string.app_name)
                .setMessage(R.string.exit_app)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
