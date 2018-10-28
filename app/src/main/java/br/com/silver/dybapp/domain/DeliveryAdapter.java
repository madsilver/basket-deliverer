package br.com.silver.dybapp.domain;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.silver.dybapp.R;

public class DeliveryAdapter extends BaseAdapter {

    Context ctx;
    List<Delivery> list;

    public DeliveryAdapter(Context ctx, List<Delivery> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Delivery delivery = list.get(position);
        View line = LayoutInflater.from(ctx).inflate(R.layout.item_delivery, null);
        TextView tvCode = line.findViewById(R.id.tvCodeDelivery);
        TextView tvDate = line.findViewById(R.id.tvDateDelivery);
        ImageView imgStatus = line.findViewById(R.id.img_delivery_status);
        ImageView imgSync = line.findViewById(R.id.img_sync);

        if(delivery.getStatus() == Delivery.OCCURRENCE ) {
            ImageViewCompat.setImageTintList(imgStatus, ContextCompat.getColorStateList(line.getContext(), R.color.colorAccent));
        }

        if(delivery.getSync() == Delivery.NOT_SYNCED) {
            ImageViewCompat.setImageTintList(imgSync, ContextCompat.getColorStateList(line.getContext(), R.color.colorAccent));
        }

        tvCode.setText(delivery.getCode());
        tvDate.setText(delivery.getDate());

        return line;
    }

}
