package cn.edu.jssvc.zhuzhengjun.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Main_listArrayAdapter extends ArrayAdapter<Main_list> {

    private int resourceId;

    public Main_listArrayAdapter(Context context, int resource, List<Main_list> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        Main_list main_list = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView textView_1 = view.findViewById(R.id.main_listView_xingqi);
        ImageView imageView = view.findViewById(R.id.main_listView_logo);
        TextView textView_3 = view.findViewById(R.id.main_listView_tiangqiZhuangtai);
        TextView textView_4 = view.findViewById(R.id.main_listView_max);
        TextView textView_5 = view.findViewById(R.id.main_listView_min);

        textView_1.setText(main_list.getXingqi());
        imageView.setImageResource(main_list.getIamge());
        textView_3.setText(main_list.getZhuangtai());
        textView_4.setText(main_list.getMax());
        textView_5.setText(main_list.getMin());

        return view;
    }
}
