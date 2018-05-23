package com.example.liaorongpu_1601r_0419;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bawei.jane.mxlistview.view.XListView;
import com.example.liaorongpu_1601r_0419.base.MyBase;
import com.example.liaorongpu_1601r_0419.gson.MyGson;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<MyGson.DataBean> datas;

    private XListView xListView;
    private int pageIndex = 1;
    private int clickType = 1;

    private String myUrl = "http://www.yulin520.com/a2a/impressApi/news/mergeList?pageSize=10&page=1";
    private MyBase base;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonDatas = (String) msg.obj;
            Gson gson = new Gson();
            MyGson myGson = gson.fromJson(jsonDatas, MyGson.class);
            List<MyGson.DataBean> data = myGson.getData();//得到集合数据
            if(clickType == 1){
                datas.clear();
            }
            datas.addAll(data);
            //设置适配器
            setXListSPQ();
            if(clickType == 1){
                xListView.stopRefresh();
            }else {
                xListView.stopLoadMore();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取资源id
        xListView = findViewById(R.id.xvl);
        datas = new ArrayList<>();

        //初始化
        initXlist();
        //调用获取数据的方法
        requestDataFunction();

    }

    private void initXlist() {//设置下拉属性上拉加载跟多
        xListView.setPullRefreshEnable(true);//C
        xListView.setPullLoadEnable(true);//C
        xListView.setXListViewListener(new XListView.IXListViewListener() {//C
            @Override
            public void onRefresh() {
                clickType = 1;
                myUrl = "http://www.yulin520.com/a2a/impressApi/news/mergeList?pageSize=10&page=1";
                requestDataFunction();
            }

            @Override
            public void onLoadMore() {
                clickType = 2;
                pageIndex++;
                myUrl = "http://www.yulin520.com/a2a/impressApi/news/mergeList?pageSize=10&page="+pageIndex;
                requestDataFunction();
            }
        });
    }

    //请求数据的方法
    private void requestDataFunction() {
       /* new MyTask().execute(myUrl);*/
        //1.得到OKHTTPClinet对象
        OkHttpClient client = new OkHttpClient.Builder().build();
        //2.创建一个请求对象
        Request request = new Request.Builder().url(myUrl).build();
        //3.创建Call对象，调用enqueue方法
        Call call = client.newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                String jsonData = response.body().string();
                Message msg = Message.obtain();
                msg.obj = jsonData;
                handler.sendMessage(msg);

            }
        });

    }
    class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                if(urlConnection.getResponseCode() == 200){
                    InputStream inputStream = urlConnection.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int len;
                    byte[] b= new byte[1024];
                    while ((len = inputStream.read(b))!=-1){
                        byteArrayOutputStream.write(b,0,len);
                    }
                    return byteArrayOutputStream.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            MyGson myGson = gson.fromJson(s, MyGson.class);
            List<MyGson.DataBean> data = myGson.getData();//得到集合数据
            if(clickType == 1){
                datas.clear();
            }
            datas.addAll(data);
            //设置适配器
            setXListSPQ();
            if(clickType == 1){
                xListView.stopRefresh();
            }else {
                xListView.stopLoadMore();
            }
        }
    }

    //设置适配器
    private void setXListSPQ() {
        if(base == null){
            base = new MyBase(MainActivity.this,datas);
            xListView.setAdapter(base);
        }else {
            base.notifyDataSetChanged();
        }

    }
}
