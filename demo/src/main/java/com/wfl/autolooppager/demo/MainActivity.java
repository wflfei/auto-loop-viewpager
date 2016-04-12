package com.wfl.autolooppager.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wfl.autolooppager.AutoLoopPager;
import com.wfl.autolooppager.RecycleAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AutoLoopPager autoLoopPager;
    private List<String> banners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        autoLoopPager = (AutoLoopPager) findViewById(R.id.autoPager);
        autoLoopPager.setAspectRatio(16f / 9f);
        formData();
        MyAdapter adapter = new MyAdapter();
        adapter.datas = banners;
        autoLoopPager.setAdapter(adapter);

    }

    private void formData() {
        banners = new ArrayList<>();
        banners.add("http://ww1.sinaimg.cn/large/7a8aed7bjw1f20ruz456sj20go0p0wi3.jpg");
        banners.add("http://ww1.sinaimg.cn/large/610dc034gw1f20vetaa9pj20ka0dh75n.jpg");
        banners.add("http://ww4.sinaimg.cn/large/610dc034gw1f20vefirbij20ka0dhq44.jpg");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_autopaly) {
            autoLoopPager.setAutoPlay(true);
            return true;
        } else if (id == R.id.action_not_auto) {
            autoLoopPager.setAutoPlay(false);
            return true;
        } else if (id == R.id.action_anim) {
            autoLoopPager.setIndicatorAnimed(true);
            return true;
        } else if (id == R.id.action_no_anim) {
            autoLoopPager.setIndicatorAnimed(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyAdapter extends RecycleAdapter<MyAdapter.ViewHolder> {
        public List<String> datas;

        @Override
        protected int getCount() {
            return null == datas ? 0 : datas.size();
        }

        @Override
        protected ViewHolder onCreateViewHolder(ViewGroup container) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(container.getContext());
            ViewHolder holder = new ViewHolder(simpleDraweeView);
            return holder;
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, int position) {
            holder.simpleDraweeView.setImageURI(Uri.parse(datas.get(position)));
        }

        @Override
        protected void onRecycleViewHolder(ViewHolder holder) {

        }

        public class ViewHolder extends RecycleAdapter.ViewHolder {
            private SimpleDraweeView simpleDraweeView;

            protected ViewHolder(@NonNull View view) {
                super(view);
                simpleDraweeView = ((SimpleDraweeView) view);
            }
        }
    }
}
