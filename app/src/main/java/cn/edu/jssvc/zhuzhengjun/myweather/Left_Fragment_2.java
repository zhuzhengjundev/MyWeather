package cn.edu.jssvc.zhuzhengjun.myweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Left_Fragment_2 extends Fragment {

    private LinearLayout main_LinearLayout_background;

    private ImageView back_imageView;
    private TextView textView_left_title;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ListView listView;
    private List<Name> nameList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private List<String> stringChengshiNameList = new ArrayList<>();
    private NameArrayAdapter nameArrayAdapter;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private DrawerLayout drawerLayout;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left__layout_2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("jiluShenfen", Context.MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("baocunChengshi",Context.MODE_PRIVATE).edit();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(), MySQLiteOpenHelper.DBNAME, null, 1);
        db = mySQLiteOpenHelper.getWritableDatabase();
        init();
    }


    private void init() {
        main_LinearLayout_background = getActivity().findViewById(R.id.main_LinearLayout_background);

        textView_didian= getActivity().findViewById(R.id.main_content_didian);
        textView_tianqizhangtai = getActivity().findViewById(R.id.main_content_tianqizhangtai);
        textView_wendu = getActivity().findViewById(R.id.main_content_wendu);
        textView_tishi = getActivity().findViewById(R.id.main_content_tishi);

        listView_main = getActivity().findViewById(R.id.main_listView);
        mainListArrayAdapter = new Main_listArrayAdapter(getContext(), R.layout.main_listview_item, main_lists);
        listView_main.setAdapter(mainListArrayAdapter);

        recyclerView = getActivity().findViewById(R.id.main_recyclerView);
        //LinearLayoutManager中定制了可扩展的布局排列接口，子类按照接口中的规范来实现就可以定制出不同排雷方式的布局了
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        main_recyclerAdapter = new Main_recyclerAdapter(main_recyclers_list);
        recyclerView.setAdapter(main_recyclerAdapter);
        //生活指标
        textView_updateTime = getActivity().findViewById(R.id.main_content_update_time);
        textView_main_content_ziwaixian = getActivity().findViewById(R.id.main_content_ziwaixian);
        textView_main_content_ziwaixian_dengji = getActivity().findViewById(R.id.main_content_ziwaixian_dengji);
        textView_main_content_ziwaixian_shuoming = getActivity().findViewById(R.id.main_content_ziwaixian_shuoming);
        textView_main_content_chuanyi = getActivity().findViewById(R.id.main_content_chuanyi);
        textView_main_content_chuanyi_dengji = getActivity().findViewById(R.id.main_content_chuanyi_dengji);
        textView_main_content_chuanyi_shuoming = getActivity().findViewById(R.id.main_content_chuanyi_shuoming);
        textView_main_content_xiche = getActivity().findViewById(R.id.main_content_xiche);
        textView_main_content_xiche_dengji = getActivity().findViewById(R.id.main_content_xiche_dengji);
        textView_main_content_xiche_shuoming = getActivity().findViewById(R.id.main_content_xiche_shuoming);
        textView_main_content_kongqi = getActivity().findViewById(R.id.main_content_kongqi);
        textView_main_content_kongqi_dengji = getActivity().findViewById(R.id.main_content_kongqi_dengji);
        textView_main_content_kongqi_shuoming = getActivity().findViewById(R.id.main_content_kongqi_shuoming);
        //今日空气质量
        textView_main_content_kongqizhiliang = getActivity().findViewById(R.id.main_content_kongqizhiliang);
        //侧边栏
        drawerLayout = getActivity().findViewById(R.id.myDrawerLayout);
        listView = getActivity().findViewById(R.id.left_fragment_2_listView);
        addData();
        nameArrayAdapter = new NameArrayAdapter(getContext(), R.layout.name_item, nameList);
        listView.setAdapter(nameArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                drawerLayout.closeDrawers();
                editor.putString("chengshi", stringList.get(position));
                editor.putString("chengshiName", stringChengshiNameList.get(position));
                editor.apply();
                HttpRequest.get_zuiquan(stringList.get(position), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            textView_updateTime.setText("更新时间："+jsonObject.getString("update_time"));
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
                                main_recycler = new Main_recycler(jsonObject_1_1.getString("day").substring(3),jsonObject_1_1.getString("wea"),jsonObject_1_1.getString("tem"));
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
                                    main_list = new Main_list("今天",resID,jsonObject_2.getString("wea"),jsonObject_2.getString("tem1"),jsonObject_2.getString("tem2"));
                                    main_lists.add(main_list);
                                }else {
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
                    }
                },null);
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
            }
        });

        textView_left_title = getActivity().findViewById(R.id.left_title);
        back_imageView = getActivity().findViewById(R.id.left_back);
        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Left_Fragment_1());
                back_imageView.setVisibility(View.GONE);
                textView_left_title.setText("省份");
            }
        });
    }

    private void addData() {
        Cursor cursor = db.rawQuery("select * from quanguoMax", null);
        Name name;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex("country")).equals(sharedPreferences.getString("shen", ""))) {
                    name = new Name(cursor.getString(cursor.getColumnIndex("chinsesName")));
                    nameList.add(name);
                    stringList.add(cursor.getString(cursor.getColumnIndex("cityCode")));
                    stringChengshiNameList.add(cursor.getString(cursor.getColumnIndex("readmeName")));
                }
            } while (cursor.moveToNext());
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.left_fragment, fragment);
        transaction.commit();
    }
}
