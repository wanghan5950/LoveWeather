package com.example.wanghanpc.loveweather.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.gson.newsGson.DataItem;
import com.example.wanghanpc.loveweather.tools.Tools;

import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {

    private Context context;
    private List<DataItem> dataItemList;
    private OnItemClickListener setOnItemClickListener;

    public NewsItemAdapter(Context context, List<DataItem> dataItemList) {
        this.dataItemList = dataItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_page_recycler_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.imageView1.setImageDrawable(null);
        holder.imageView2.setImageDrawable(null);
        holder.imageView3.setImageDrawable(null);
        RequestManager manager = Glide.with(context);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        DataItem item = dataItemList.get(position);
        String title = item.getTitle();
        String author = item.getAuthorName();
        String time = Tools.getWeek(item.getDate().substring(0,10)) + "  " + item.getDate().substring(11);
        holder.titleText.setText(title);
        holder.authorName.setText(author);
        holder.timeText.setText(time);
        if (item.getPictureUrl1() != null){
            String url1 = item.getPictureUrl1();
            manager.load(url1).apply(options).into(holder.imageView1);
        }
        if (item.getPictureUrl2() != null){
            String url2 = item.getPictureUrl2();
            manager.load(url2).apply(options).into(holder.imageView2);
        }
        if (item.getPictureUrl3() != null){
            String url3 = item.getPictureUrl3();
            manager.load(url3).apply(options).into(holder.imageView3);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnItemClickListener.onItemClickListener(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.setOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleText;
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView imageView3;
        private TextView timeText;
        private TextView authorName;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.news_item_title);
            imageView1 = (ImageView) itemView.findViewById(R.id.news_item_image1);
            imageView2 = (ImageView) itemView.findViewById(R.id.news_item_image2);
            imageView3 = (ImageView) itemView.findViewById(R.id.news_item_image3);
            timeText = (TextView) itemView.findViewById(R.id.news_item_time);
            authorName = (TextView) itemView.findViewById(R.id.news_item_author);
        }
    }
}
