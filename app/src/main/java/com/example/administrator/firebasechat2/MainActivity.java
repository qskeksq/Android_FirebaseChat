package com.example.administrator.firebasechat2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.firebasechat2.adapter.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager mainPager;
    private MainPagerAdapter pagerAdapter;
    private FloatingActionButton fab;
    private FrameLayout addBackground;
    private int CUR_PAGE = 0;
    private FloatingActionButton fabChatNormal;
    private FloatingActionButton fabSecretChat;
    private FloatingActionButton fabOpenChat;
    private TextView txtChatNormal;
    private TextView txtChatOpen;
    private TextView txtChatSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
        setToolbar();
        setTabLayout();
        setMainPager();
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mainPager = (ViewPager) findViewById(R.id.mainPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        addBackground = (FrameLayout) findViewById(R.id.addBackground);
        fabChatNormal = (FloatingActionButton) findViewById(R.id.fab_chatNormal);
        fabSecretChat = (FloatingActionButton) findViewById(R.id.fab_secretChat);
        fabOpenChat = (FloatingActionButton) findViewById(R.id.fab_openChat);
        txtChatNormal = (TextView) findViewById(R.id.txtChatNormal);
        txtChatOpen = (TextView) findViewById(R.id.txtChatOpen);
        txtChatSecret = (TextView) findViewById(R.id.txtChatSecret);
    }

    private void setListener() {

        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.add_user);
                        CUR_PAGE = 0;
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.chat_more3);
                        CUR_PAGE = 1;
                        break;
                    case 2:
                        fab.setVisibility(ViewPager.GONE);
                        break;
                    case 3:
                        fab.setVisibility(ViewPager.GONE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (CUR_PAGE) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        setAddBackgroundVisibility();
                        setAddChatVisibility();
                        break;
                }
            }
        });

        fabChatNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeRoomActivity.class);
                startActivity(intent);
            }
        });

        addBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setAddBackgroundVisibility();
            }
        });


    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_selected_tab));
        getSupportActionBar().setTitle(getResources().getString(R.string.tab_friend_list));
    }

    private void setTabLayout() {

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.user_selected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.chat_unselected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.clock_unselected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.more_unselected));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.user_selected);
                        toolbar.setTitle(getString(R.string.tab_friend_list));
                        break;
                    case 1:
                        tab.setIcon(R.drawable.chat_selected);
                        toolbar.setTitle(getString(R.string.tab_room_list));
                        break;
                    case 2:
                        tab.setIcon(R.drawable.clock_selected);
                        toolbar.setTitle(getString(R.string.tab_time_line));
                        break;
                    case 3:
                        tab.setIcon(R.drawable.more_selected);
                        toolbar.setTitle(getString(R.string.tab_profile));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.user_unselected);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.chat_unselected);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.clock_unselected);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.more_unselected);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                break;
            case R.id.menu_more:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMainPager() {
        pagerAdapter = new MainPagerAdapter(this);
        mainPager.setAdapter(pagerAdapter);
        mainPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mainPager));
    }

    private void setAddBackgroundVisibility() {
        if (addBackground.getVisibility() == View.VISIBLE) {
            addBackground.setVisibility(View.GONE);
        } else {
            addBackground.setVisibility(View.VISIBLE);
        }
    }

    private void setAddChatVisibility() {
        if (fabChatNormal.getVisibility() == View.VISIBLE) {
            fabChatNormal.setVisibility(View.GONE);
            fabOpenChat.setVisibility(View.GONE);
            fabSecretChat.setVisibility(View.GONE);

            txtChatNormal.setVisibility(View.GONE);
            txtChatOpen.setVisibility(View.GONE);
            txtChatSecret.setVisibility(View.GONE);
        } else {
            fabChatNormal.setVisibility(View.VISIBLE);
            fabOpenChat.setVisibility(View.VISIBLE);
            fabSecretChat.setVisibility(View.VISIBLE);

            txtChatNormal.setVisibility(View.VISIBLE);
            txtChatOpen.setVisibility(View.VISIBLE);
            txtChatSecret.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (addBackground.getVisibility() == View.VISIBLE) {
            addBackground.setVisibility(View.GONE);
        }
        if (fabChatNormal.getVisibility() == View.VISIBLE) {
            fabChatNormal.setVisibility(View.GONE);
            fabOpenChat.setVisibility(View.GONE);
            fabSecretChat.setVisibility(View.GONE);

            txtChatNormal.setVisibility(View.GONE);
            txtChatOpen.setVisibility(View.GONE);
            txtChatSecret.setVisibility(View.GONE);
        }
    }

}
