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

import com.example.administrator.firebasechat2.Util.Const;
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


    /**
     * 뷰 초기화
     */
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

    /**
     * 리스너
     */
    private void setListener() {

        // 페이저 설정 - 친구목록, 채팅방목록, 타임라인, 프로필
        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    // 화면이 바뀌면서
                    // 1. FAB 버튼 visibility
                    // 2. FAB 버튼 이미지
                    // 3. 현재 페이지 멤버변수에 설정
                    case 0:
                        setFabInvisible();
                        fab.setImageResource(R.drawable.add_user);
                        CUR_PAGE = 0;
                        break;
                    case 1:
                        setFabInvisible();
                        fab.setImageResource(R.drawable.chat_more3);
                        CUR_PAGE = 1;
                        break;
                    case 2:
                        setFabInVisible();
                        break;
                    case 3:
                        setFabInVisible();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (CUR_PAGE) {
                    // 친구 추가 FAB
                    case Const.PAGE_FRIEND_LIST:
                        Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                        startActivity(intent);
                        break;
                    // 채팅방 추가 FAB
                    case Const.PAGE_CHAT_ROOM_LIST:
                        setAddBackgroundVisibility();
                        setAddChatVisibility();
                        break;
                }
            }
        });

        // 일반 채팅
        fabChatNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeRoomActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 툴바 설정
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_selected_tab));
        getSupportActionBar().setTitle(getResources().getString(R.string.tab_friend_list));
    }

    /**
     * 탭 레이아웃 설정
     */
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

    /**
     * 메인 화면 페이저 설정
     * 페이저 어댑터를 생성하면서 모든 초기 데이터 세팅
     */
    private void setMainPager() {
        pagerAdapter = new MainPagerAdapter(this);
        mainPager.setAdapter(pagerAdapter);
        mainPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mainPager));
    }

    /**
     * 메뉴 인플레이션
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 메뉴 선택 리스너
     */
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

    /**
     * 채팅방 추가 FAB 클릭시 background 관리
     */
    private void setAddBackgroundVisibility() {
        if (addBackground.getVisibility() == View.VISIBLE) {
            addBackground.setVisibility(View.GONE);
        } else {
            addBackground.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 채팅방 추가 FAB 클릭시 뷰 visiblity 관리
     */
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

    /**
     * 다른 화면으로 넘어갈 시 FAB, background 설정
     */
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

    /**
     * FAB invisible
     */
    private void setFabInvisible() {
        fab.setVisibility(View.VISIBLE);
    }

    /**
     * FAB visible
     */
    private void setFabInVisible() {
        fab.setVisibility(ViewPager.GONE);
    }

}
