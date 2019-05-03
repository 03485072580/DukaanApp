package com.example.fasih.dukaanapp.networking.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.order.interfaces.DialogClickNotify;

public class AlertPaymentDialogFragment extends DialogFragment {

    private RelativeLayout confirm, cancel;
    private TextView productTitle, productPrice;
    private DialogClickNotify notify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_alert_payment_dialog, container, false);
        setupFragmentWidgets(view);
        setupWidgetsInitialization(getProduct(getArguments()));
        return view;
    }

    private void setupWidgetsInitialization(Products product) {

        if(product!=null){
            productTitle.setText(product.getProduct_name());
            productPrice.setText(product.getProduct_price());
        }
    }

    private Products getProduct(Bundle bundle) {
        if(bundle!=null){
            Products product =(Products) bundle.get(getString(R.string.uploadProduct));
            return product;
        }
        return null;
    }

    private void setupFragmentWidgets(View view) {
        confirm = view.findViewById(R.id.confirm);
        cancel = view.findViewById(R.id.cancel);
        productTitle = view.findViewById(R.id.productTitle);
        productPrice = view.findViewById(R.id.productPrice);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notify activity about click event
                notify.setupOnClickListener(view);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notify activity about click event
                notify.setupOnClickListener(view);
            }
        });
    }

    public void setupOnButtonClickListener(DialogClickNotify notify){
        this.notify = notify;

    }
}
