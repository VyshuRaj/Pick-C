package com.prts.pickcustomer.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.prts.pickcustomer.dummy.DummyPage;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.about.AboutActivity;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.emergency.EmergencyActivity;
import com.prts.pickcustomer.help.WebViewActivity;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.history.BookingHistoryActivity;
import com.prts.pickcustomer.login.LoginActivity;
import com.prts.pickcustomer.profile.ProfileActivity;
import com.prts.pickcustomer.rate_card.RateCardView;
import com.prts.pickcustomer.referral.ReferralActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.prts.pickcustomer.helpers.Constants.HELP_URL;
import static com.prts.pickcustomer.helpers.Constants.PRIVACY_POLICY;
import static com.prts.pickcustomer.helpers.Constants.TERMS_AND_CONDITIONS;

public class BaseActivity extends AppCompatActivity implements BaseView {

    public static final String NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static BroadcastReceiver broadcastReceiver;
    private static final String TAG = "NavDrawerAct";

    int mPosition = -1;
    String mTitle = "";
    Toolbar toolbar;
    TextView toolbartitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawer, mDrawerListLayout;

    int[] mMenuDrawables = new int[]{
            R.drawable.truck_menu,
            R.drawable.ic_nav_my_rides,
            R.drawable.nav_rate_card,
            R.drawable.ic_nav_emergency_contact,
            R.drawable.ic_nav_support,
            R.drawable.tcpng,
            R.drawable.priavcy_policy,
            R.drawable.about,
            R.drawable.referal,
            R.drawable.logout
    };

    BaseViewPresenter mBaseViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (!NetworkHelper.hasNetworkConnection(BaseActivity.this)) {
                    ToastHelper.noInternet(BaseActivity.this);
                }
            }
        };

        mBaseViewPresenter = new BaseViewPresenterImpl(BaseActivity.this, this);
    }

    @Override
    public void setContentView(int layoutResID) {

        try {
            LinearLayout fullLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.nav_activity_main, null);
            FrameLayout frameLayout = (FrameLayout) fullLayout.findViewById(R.id.content_frame);
            getLayoutInflater().inflate(layoutResID, frameLayout, true);
            super.setContentView(fullLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected boolean isNavDrawerOpened = false;

    protected void navDrawerOnCreate() {

        String[] mMenus = new String[]{
                "Book your truck",
                "Booking history",
                "Rate card",
                "Emergency contacts",
                "Help",
                "Terms & Conditions",
                "Privacy & Policy",
                "About",
                "Referral",
                "Log out"};
        mTitle = (String) getTitle();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (LinearLayout) findViewById(R.id.main_parent_view);
        mDrawerListLayout = (LinearLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        toolbartitle = (TextView) findViewById(R.id.titletool);
        List<HashMap<String, String>> mList = new ArrayList<>();

        String COUNTRY = "country";
        String FLAG = "flag";
        for (int i = 0; i < mMenus.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put(COUNTRY, mMenus[i]);
            hm.put(FLAG, Integer.toString(mMenuDrawables[i]));
            mList.add(hm);
        }
        String COUNT = "count";
        String[] from = {FLAG, COUNTRY, COUNT};
        int[] to = {R.id.flag, R.id.country, R.id.count};
        SimpleAdapter mAdapter = new SimpleAdapter(this, mList, R.layout.nav_drawer_layout, from, to);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedCountry();
                supportInvalidateOptionsMenu();
                isNavDrawerOpened = false;
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle("More Apps From Us");
                supportInvalidateOptionsMenu();
                initialize();
                isNavDrawerOpened = true;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int position,
                                    long arg3) {
                try {
                    showFragment(position);
                    arg1.setBackgroundResource(R.color.appThemeYellow);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            arg1.setBackgroundResource(android.R.color.transparent);
                        }
                    }, 500);
                    mDrawerLayout.closeDrawer(mDrawerListLayout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDrawerList.setAdapter(mAdapter);
        initialize();
    }

    private void initialize() {
        try {
            TextView userMobileNOTV = findViewById(R.id.user_mobileNo_TV);
            TextView userNameTV = findViewById(R.id.user_name_TV);
            userMobileNOTV.setText(CredentialManager.getMobileNO(BaseActivity.this));
            userNameTV.setText(CredentialManager.getName(BaseActivity.this));

            LinearLayout userProfileLL = findViewById(R.id.userProfileLL);
            userProfileLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, ProfileActivity.class));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            // If the nav drawer is open, hide action items related to the content view
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListLayout);
            int menuSize = menu.size();
            for (int i = 0; i < menuSize; i++) {
                MenuItem menuItem = menu.getItem(i);
                menuItem.setVisible(!drawerOpen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void showFragment(int position) {
        try {
            getSupportActionBar().setTitle(R.string.app_name);

            switch (position) {
                case 0:
                    startActivity(new Intent(BaseActivity.this, DummyPage.class));
                    break;
                case 1:
                    startActivity(new Intent(BaseActivity.this, BookingHistoryActivity.class));

                    return;
                case 2:

                    startActivity(new Intent(BaseActivity.this, RateCardView.class));

                    return;
                case 3:
                    startActivity(new Intent(getApplicationContext(), EmergencyActivity.class));
                    break;
                case 4:
                    Intent intent = new Intent(BaseActivity.this, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL, HELP_URL);
                    intent.putExtra(WebViewActivity.TITLE, "Help");
                    startActivity(intent);
                    break;

                case 5:
                    Intent tcIntent = new Intent(BaseActivity.this, WebViewActivity.class);
                    tcIntent.putExtra(WebViewActivity.URL, TERMS_AND_CONDITIONS);
                    tcIntent.putExtra(WebViewActivity.TITLE, "Terms & Conditions");
                    startActivity(tcIntent);
                    break;
                case 6:
                    Intent ppintent = new Intent(BaseActivity.this, WebViewActivity.class);
                    ppintent.putExtra(WebViewActivity.URL, PRIVACY_POLICY);
                    ppintent.putExtra(WebViewActivity.TITLE, "Privacy & Policy");
                    startActivity(ppintent);
                    break;
                case 7:
                    startActivity(new Intent(BaseActivity.this, AboutActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(BaseActivity.this, ReferralActivity.class));
                    break;
                case 9:
                    showConfirmDialog();
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showConfirmDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this, R.style.AppDialogTheme1);
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.logout_confrimation, null, false);
        alertDialog.setView(view);
        PickCCustomTextVIew yes = view.findViewById(R.id.yesBtn);
        PickCCustomTextVIew no = view.findViewById(R.id.noBtn);

        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                try {
                    mBaseViewPresenter.doLogoutCall();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void highlightSelectedCountry() {
        try {
            int selectedItem = mDrawerList.getCheckedItemPosition();
            if (selectedItem > 4)
                mDrawerList.setItemChecked(mPosition, true);
            else
                mPosition = selectedItem;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (isNavDrawerOpened) {
                mDrawerLayout.closeDrawer(mDrawerListLayout);
            } /*else {
                super.onBackPressed();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(broadcastReceiver, new IntentFilter(NETWORK_CHANGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void navigateToLoginPage() {
        try {
            ToastHelper.showToastLenShort(BaseActivity.this, getString(R.string.logged_out_successfully));
            startActivity(new Intent(BaseActivity.this, LoginActivity.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(BaseActivity.this);
    }

    @Override
    public void tryAagin() {
        ToastHelper.showToastLenShort(BaseActivity.this, getString(R.string.try_again));
    }

    @Override
    public void notAbleToLoggedOut() {
        ToastHelper.showToastLenShort(BaseActivity.this, getString(R.string.try_again));
    }

    @Override
    public void showProgressDialog(String s) {

        ProgressDialogHelper.showProgressDialog(BaseActivity.this, s);
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }
}



