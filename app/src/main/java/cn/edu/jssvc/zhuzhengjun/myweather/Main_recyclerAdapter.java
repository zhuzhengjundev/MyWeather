package cn.edu.jssvc.zhuzhengjun.myweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Main_recyclerAdapter extends RecyclerView.Adapter<Main_recyclerAdapter.ViewHolder> {

    private List<Main_recycler> mRecyclers;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_recyclerview_item, viewGroup,false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Main_recycler main_recycler = mRecyclers.get(i);
        viewHolder.textView_time.setText(main_recycler.getTime());
        viewHolder.textView_zhuangtai.setText(main_recycler.getZhuangtai());
        viewHolder.textView_wendu.setText(main_recycler.getWendu());
    }

    @Override
    public int getItemCount() {
        return mRecyclers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_time;
        TextView textView_zhuangtai;
        TextView textView_wendu;

        public ViewHolder( View itemView) {
            super(itemView);
            textView_time = itemView.findViewById(R.id.main_recyclerView_time);
            textView_zhuangtai = itemView.findViewById(R.id.main_recyclerView_zhuangtai);
            textView_wendu = itemView.findViewById(R.id.main_recyclerView_wendu);
        }
    }

    public Main_recyclerAdapter(List<Main_recycler> recyclers) {
        mRecyclers = recyclers;
    }

}
