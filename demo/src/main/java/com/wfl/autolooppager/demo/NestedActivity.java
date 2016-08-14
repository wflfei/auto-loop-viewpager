package com.wfl.autolooppager.demo;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wfl.autolooppager.AutoLoopPager;
import com.wfl.autolooppager.RecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sn on 2016/8/15.
 */
public class NestedActivity extends AppCompatActivity {

    private AutoLoopPager autoLoopPager;
    private RecyclerView mRecyclerView;
    private List<String> banners;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        autoLoopPager = (AutoLoopPager) findViewById(R.id.autoPager);
        autoLoopPager.setAspectRatio(16f / 9f);
        formData();
        MyAdapter adapter = new MyAdapter();
        adapter.datas = banners;
        autoLoopPager.setAdapter(adapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new RecyclerAdapter());
    }

    private void formData() {
        banners = new ArrayList<>();
        banners.add("http://ww1.sinaimg.cn/large/7a8aed7bjw1f20ruz456sj20go0p0wi3.jpg");
        banners.add("http://ww1.sinaimg.cn/large/610dc034gw1f20vetaa9pj20ka0dh75n.jpg");
        banners.add("http://ww4.sinaimg.cn/large/610dc034gw1f20vefirbij20ka0dhq44.jpg");
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

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemTv.setText("Item " + position);
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView itemTv;

            public ViewHolder(View itemView) {
                super(itemView);
                itemTv = (TextView) itemView;
            }
        }
    }


}
