package cn.edu.jssvc.zhuzhengjun.myweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Left_Fragment_1 extends Fragment {

    private ListView left_listView;
    private List<Name> nameList = new ArrayList<>();
    private NameArrayAdapter nameArrayAdapter;

    private ImageView back_imageView;
    private TextView textView_left_title;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;

    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left__layout_1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(), MySQLiteOpenHelper.DBNAME, null, 1);
        db = mySQLiteOpenHelper.getWritableDatabase();

        back_imageView = getActivity().findViewById(R.id.left_back);
        back_imageView.setVisibility(View.GONE);
        textView_left_title = getActivity().findViewById(R.id.left_title);
        textView_left_title.setText("省份");

        left_listView = getActivity().findViewById(R.id.left_fragment_1_listView);
        getData();
        nameArrayAdapter = new NameArrayAdapter(getContext(),R.layout.name_item,nameList);
        left_listView.setAdapter(nameArrayAdapter);
        left_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor = getActivity().getSharedPreferences("jiluShenfen", Context.MODE_PRIVATE).edit();
                editor.putString("shen", nameList.get(position).getName());
                editor.apply();
                back_imageView.setVisibility(View.VISIBLE);
                textView_left_title.setText("城市");
                replaceFragment(new Left_Fragment_2());
            }
        });

    }

    private void getData() {
        Name name;
        Cursor cursor = db.rawQuery("select * from quanguo", null);
        if (cursor.moveToFirst()) {
            do {
                name = new Name(cursor.getString(cursor.getColumnIndex("country")));
                nameList.add(name);
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
