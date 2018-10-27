package br.com.silver.dybapp.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        TextView tvStatus = line.findViewById(R.id.tvStatusDelivery);

        String status = delivery.getSync() == 0 ? "Entregue" : "Ocorrência";

        tvCode.setText(delivery.getCode());
        tvDate.setText(delivery.getDate());
        tvStatus.setText(status);

        return line;
    }

}
