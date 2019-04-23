package com.example.fasih.dukaanapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Orders;

import java.util.ArrayList;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.MyViewHolder> {

    private OnRecyclerImageSelectedListener imageSelected;
    private Context mContext;
    private ArrayList<Orders> userViewOrdersList;

    public OrderProductsAdapter(Context context
            , ArrayList<Orders> userViewOrdersList) {

        this.mContext = context;
        this.userViewOrdersList = userViewOrdersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_order_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.orderID.setText("ORDER ID# " + userViewOrdersList.get(position).getOrder_id());
        holder.productTitle.setText(userViewOrdersList.get(position).getProduct_name());
        holder.productPrice.setText(userViewOrdersList.get(position).getOrder_price());
        holder.timeStamp.setText(parseDateFromTimeStamp(userViewOrdersList.get(position).getTimeStamp()));
    }

    private String parseDateFromTimeStamp(String timeStamp) {
        return timeStamp.split(" ")[0];
    }

    @Override
    public int getItemCount() {
        return userViewOrdersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView orderID, productTitle, productPrice, timeStamp;
        private Button trackOrder;

        public MyViewHolder(final View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.orderID);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            trackOrder = itemView.findViewById(R.id.trackOrder);

            trackOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageSelected.onClickGridImage(getAdapterPosition()
                            , trackOrder
                            , "");
                }
            });
        }
    }

    public void setupOnItemClickListener(OnRecyclerImageSelectedListener onRecyclerImageSelectedListener) {

        this.imageSelected = onRecyclerImageSelectedListener;

    }
}
