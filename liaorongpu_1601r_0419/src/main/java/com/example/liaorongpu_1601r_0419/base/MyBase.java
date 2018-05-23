package com.example.liaorongpu_1601r_0419.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaorongpu_1601r_0419.R;
import com.example.liaorongpu_1601r_0419.gson.MyGson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by John on 2018/4/19 0019.
 */

public class MyBase extends BaseAdapter {
    private Context context;
    private List<MyGson.DataBean> list;
    private static int TXT_VIEW = 0;
    private static int IMG_VIEW = 1;
    private final DisplayImageOptions options;

    public MyBase(Context context, List<MyGson.DataBean> list) {
        this.context = context;
        this.list = list;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)//使用内存缓存
                .cacheOnDisk(true)//使用磁盘缓存
                .showImageOnLoading(R.mipmap.ic_launcher)//设置正在下载的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//url为空或请求的资源不存在时
                .showImageOnFail(R.mipmap.ic_launcher)//下载失败时显示的图片
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片色彩模式
                .imageScaleType(ImageScaleType.EXACTLY)//设置图片的缩放模式
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int t = getItemViewType(i);
        if(t == IMG_VIEW){
            ViewHolder holder;
            if(view == null){
                view = View.inflate(context, R.layout.activity_mybase,null);
                holder = new ViewHolder();
                holder.img_tu = view.findViewById(R.id.img_tu);
                holder.txt_title = view.findViewById(R.id.txt_title);
                holder.txt_time = view.findViewById(R.id.txt_time);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            holder.txt_title.setText(list.get(i).getTitle());
            holder.txt_time.setText(list.get(i).getTopTime()+"");
            //加载图片
            ImageLoader.getInstance().displayImage(list.get(i).getUserImg(),holder.img_tu,options);
            return view;
        }else {

            ViewHolder2 holder2;
            if(view == null){
                view = View.inflate(context, R.layout.activity_mybase2,null);
                holder2 = new ViewHolder2();
                holder2.txt_title2 = view.findViewById(R.id.txt_title2);
                holder2.txt_time2 = view.findViewById(R.id.txt_time2);
                view.setTag(holder2);
            }else {
                holder2 = (ViewHolder2) view.getTag();
            }
            holder2.txt_title2.setText(list.get(i).getTitle());
            holder2.txt_time2.setText(list.get(i).getTopTime()+"");
            return view;
        }


    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2==0?IMG_VIEW:TXT_VIEW;
    }

    class ViewHolder2{
        TextView txt_title2,txt_time2;
    }
    class ViewHolder{
        TextView txt_title,txt_time;
        ImageView img_tu;
    }
}
