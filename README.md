# auto-loop-viewpager
An Android auto scroll ViewPager which can inifinite loop. And contains a animated indicator.

![image](https://raw.githubusercontent.com/wflfei/auto-loop-viewpager/master/auto-scroll-pager.gif)

## Useage
Custom your adapter extends RecycleAdapter<VH extends RecycleAdapter.ViewHolder>:

```java
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
```
And then:
```
mAutoLoopPager.setAdapter(new MyAdapter());
```
Also you can check the RecycleAdapter.java to see how does RecycleAdapter manage and recycle Views for the ViewPager;

## Configration
- Set whether this LoopViewPager need to scroll automaticaly

```
mAutoLoopPager.setAutoPlay(boolean);
```

- Set the automaticaly scroll duration: millisecond

```
mAutoLoopPager.setAutoDuration(int);
```

- Set whether the Indicator animated when ViewPager scrolled

```
mAutoLoopPager.setIndicatorAnimed(boolean);
```

- Set the aspect ratio for this View, This is useful if just set the ```layout_width``` to ```match_parent``` and leave the ```layout_height``` with ```wrap_content```. In this case, the height will be calculated by the aspect ratio set here:

```
mAutoLoopPager.setAspectRatio(float);
```

---
Thanks to [gank.io](http://gank.io/). Where these images in this project are from.