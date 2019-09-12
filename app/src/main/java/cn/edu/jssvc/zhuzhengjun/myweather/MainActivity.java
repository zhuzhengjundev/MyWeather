package cn.edu.jssvc.zhuzhengjun.myweather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout myDrawerLayout;

    private LinearLayout main_LinearLayout_background;

    private ImageView imageView_menu;

    private Context context;

    private ProgressDialog progressDialog;

    private SwipeRefreshLayout swipeRefreshLayout;     //下拉刷新

    private TextView textView_updateTime,textView_didian,textView_tianqizhangtai,textView_wendu,textView_tishi;
    private ListView listView_main;
    private Main_list main_list;
    private List<Main_list> main_lists = new ArrayList<>();
    private Main_listArrayAdapter mainListArrayAdapter;

    private RecyclerView recyclerView;
    private Main_recycler main_recycler;
    private List<Main_recycler> main_recyclers_list = new ArrayList<>();
    private Main_recyclerAdapter main_recyclerAdapter;

    private TextView textView_main_content_ziwaixian,textView_main_content_ziwaixian_dengji,textView_main_content_ziwaixian_shuoming;   //紫外线指数
    private TextView textView_main_content_chuanyi,textView_main_content_chuanyi_dengji,textView_main_content_chuanyi_shuoming;          //穿衣指数
    private TextView textView_main_content_xiche,textView_main_content_xiche_dengji,textView_main_content_xiche_shuoming;                 //洗车指数
    private TextView textView_main_content_kongqi,textView_main_content_kongqi_dengji,textView_main_content_kongqi_shuoming;              //空气质量指数

    private TextView textView_main_content_kongqizhiliang;    //今天空气质量

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                progressDialog.dismiss();
                myDrawerLayout.openDrawer(Gravity.START);
                replaceFragment(new Left_Fragment_1());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu_image:
                myDrawerLayout.openDrawer(Gravity.START);
                replaceFragment(new Left_Fragment_1());
                break;
            default:
                break;
        }
    }

    private void init() {
        swipeRefreshLayout = findViewById(R.id.main_SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        main_LinearLayout_background = findViewById(R.id.main_LinearLayout_background);
        //随机背景
        Random random = new Random();
        int j = random.nextInt(20);
        Log.d("随机数", j + "");
        HttpRequest.bitmap(ImageLinks.imageUrls[j-1], new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                main_LinearLayout_background.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        },null);

        textView_didian= findViewById(R.id.main_content_didian);
        textView_tianqizhangtai = findViewById(R.id.main_content_tianqizhangtai);
        textView_wendu = findViewById(R.id.main_content_wendu);
        textView_tishi = findViewById(R.id.main_content_tishi);

        listView_main = findViewById(R.id.main_listView);
        mainListArrayAdapter = new Main_listArrayAdapter(MainActivity.this, R.layout.main_listview_item, main_lists);
        listView_main.setAdapter(mainListArrayAdapter);

        recyclerView = findViewById(R.id.main_recyclerView);
        //LinearLayoutManager中定制了可扩展的布局排列接口，子类按照接口中的规范来实现就可以定制出不同排雷方式的布局了
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        main_recyclerAdapter = new Main_recyclerAdapter(main_recyclers_list);
        recyclerView.setAdapter(main_recyclerAdapter);
        //生活指标
        textView_updateTime = findViewById(R.id.main_content_update_time);
        textView_main_content_ziwaixian = findViewById(R.id.main_content_ziwaixian);
        textView_main_content_ziwaixian_dengji = findViewById(R.id.main_content_ziwaixian_dengji);
        textView_main_content_ziwaixian_shuoming = findViewById(R.id.main_content_ziwaixian_shuoming);
        textView_main_content_chuanyi = findViewById(R.id.main_content_chuanyi);
        textView_main_content_chuanyi_dengji = findViewById(R.id.main_content_chuanyi_dengji);
        textView_main_content_chuanyi_shuoming = findViewById(R.id.main_content_chuanyi_shuoming);
        textView_main_content_xiche = findViewById(R.id.main_content_xiche);
        textView_main_content_xiche_dengji = findViewById(R.id.main_content_xiche_dengji);
        textView_main_content_xiche_shuoming = findViewById(R.id.main_content_xiche_shuoming);
        textView_main_content_kongqi = findViewById(R.id.main_content_kongqi);
        textView_main_content_kongqi_dengji = findViewById(R.id.main_content_kongqi_dengji);
        textView_main_content_kongqi_shuoming = findViewById(R.id.main_content_kongqi_shuoming);
        //今日空气质量
        textView_main_content_kongqizhiliang = findViewById(R.id.main_content_kongqizhiliang);
        //侧边栏
        myDrawerLayout = findViewById(R.id.myDrawerLayout);
        myDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        imageView_menu = findViewById(R.id.main_menu_image);
        imageView_menu.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("isFirst", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("is", false)) {
            SharedPreferences.Editor editor = getSharedPreferences("isFirst", Context.MODE_PRIVATE).edit();
            editor.putBoolean("is",true);
            editor.apply();
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("提示");
            progressDialog.setMessage("正在获取全国各地方名称...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Function function = new Function();
                    function.openXls(context,handler);
                    function.openXls2(context);
                }
            }).start();
        }
        getData();
    }

    private void getData() {
        SharedPreferences sharedPreferences2 = getSharedPreferences("baocunChengshi", Context.MODE_PRIVATE);
        HttpRequest.get_zuiquan(sharedPreferences2.getString("chengshi", ""), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("打印", jsonObject.toString());
                try {
                    textView_updateTime.setText("更新时间：" + jsonObject.getString("update_time"));
                    textView_didian.setText(jsonObject.getString("city"));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jsonObject_1 = jsonArray.getJSONObject(0);
                    textView_tianqizhangtai.setText(jsonObject_1.getString("wea"));
                    textView_wendu.setText(jsonObject_1.getString("tem"));
                    //今日空气质量
                    textView_main_content_kongqizhiliang.setText("空气质量：" + jsonObject_1.getString("air_level"));
                    //RecyclerView数据数组
                    JSONArray jsonArray_1 = jsonObject_1.getJSONArray("hours");
                    for (int i = 0; i < jsonArray_1.length(); i++) {
                        JSONObject jsonObject_1_1 = jsonArray_1.getJSONObject(i);
                        main_recycler = new Main_recycler(jsonObject_1_1.getString("day").substring(3), jsonObject_1_1.getString("wea"), jsonObject_1_1.getString("tem"));
                        main_recyclers_list.add(main_recycler);
                    }
                    main_recyclerAdapter.notifyDataSetChanged();
                    //今日温度说明
                    textView_tishi.setText("        " + jsonObject_1.getString("air_tips"));
                    //ListView数据数组
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject_2 = jsonArray.getJSONObject(j);
                        int resID = getResources().getIdentifier(jsonObject_2.getString("wea_img"), "drawable", "cn.edu.jssvc.zhuzhengjun.myweather");
                        if (j == 0) {
                            main_list = new Main_list("今天", resID, jsonObject_2.getString("wea"), jsonObject_2.getString("tem1"), jsonObject_2.getString("tem2"));
                            main_lists.add(main_list);
                        } else {
                            main_list = new Main_list(jsonObject_2.getString("week"), resID, jsonObject_2.getString("wea"), jsonObject_2.getString("tem1"), jsonObject_2.getString("tem2"));
                            main_lists.add(main_list);
                        }
                    }
                    mainListArrayAdapter.notifyDataSetChanged();
                    //生活指数数组
                    JSONArray jsonArray_2 = jsonObject_1.getJSONArray("index");
                    //紫外线指数
                    JSONObject jsonObject_1_2_0 = jsonArray_2.getJSONObject(0);
                    textView_main_content_ziwaixian.setText(jsonObject_1_2_0.getString("title"));
                    textView_main_content_ziwaixian_dengji.setText(jsonObject_1_2_0.getString("level"));
                    textView_main_content_ziwaixian_shuoming.setText(jsonObject_1_2_0.getString("desc"));
                    //穿衣指数
                    JSONObject jsonObject_1_2_3 = jsonArray_2.getJSONObject(3);
                    textView_main_content_chuanyi.setText(jsonObject_1_2_3.getString("title"));
                    textView_main_content_chuanyi_dengji.setText(jsonObject_1_2_3.getString("level"));
                    textView_main_content_chuanyi_shuoming.setText(jsonObject_1_2_3.getString("desc"));
                    //洗车指数
                    JSONObject jsonObject_1_2_4 = jsonArray_2.getJSONObject(4);
                    textView_main_content_xiche.setText(jsonObject_1_2_4.getString("title"));
                    textView_main_content_xiche_dengji.setText(jsonObject_1_2_4.getString("level"));
                    textView_main_content_xiche_shuoming.setText(jsonObject_1_2_4.getString("desc"));
                    //空气质量指数
                    JSONObject jsonObject_1_2_5 = jsonArray_2.getJSONObject(5);
                    textView_main_content_kongqi.setText(jsonObject_1_2_5.getString("title"));
                    textView_main_content_kongqi_dengji.setText(jsonObject_1_2_5.getString("level"));
                    textView_main_content_kongqi_shuoming.setText(jsonObject_1_2_5.getString("desc"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MyApplication.mContext.getApplicationContext(),"数据更新了",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("错误", volleyError.toString());
                Toast.makeText(MyApplication.mContext.getApplicationContext(),"错误" + volleyError,Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.left_fragment, fragment);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("您确定退出？");

            //设置确定按钮
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //设置取消按钮
            builder.setPositiveButton("容我再想想",null);
            //显示提示框
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
