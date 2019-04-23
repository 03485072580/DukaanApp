package com.example.fasih.dukaanapp.order.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Products;

public class CheckoutFragment extends Fragment implements View.OnClickListener {

    private RadioButton payByHand, payByCashOnDelivery, payByPaypal, payByStripe;
    private TextView availableStock, productName, productPrice;

    private Products product;

    private Boolean isPaymentMethodCorrect = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        setupFragmentWidgets(view);
        setupFragmentWidgetsInitialization(getBundleData(getArguments()));
        return view;
    }

    private void setupFragmentWidgets(View view) {

        availableStock = view.findViewById(R.id.availableStock);
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        payByHand = view.findViewById(R.id.payByHand);
        payByCashOnDelivery = view.findViewById(R.id.payByCashOnDelivery);
        payByPaypal = view.findViewById(R.id.payByPaypal);
        payByStripe = view.findViewById(R.id.payByStripe);

        payByHand.setOnClickListener(this);
        payByCashOnDelivery.setOnClickListener(this);
        payByPaypal.setOnClickListener(this);
        payByStripe.setOnClickListener(this);
    }

    private void setupFragmentWidgetsInitialization(Products product) {

        this.product = product;

        availableStock.setText("Available Stock : "+product.getProduct_stock());
        productName.setText("Title : "+product.getProduct_name());
        productPrice.setText("Price: "+product.getProduct_price());

    }

    private Products getBundleData(Bundle arguments) {
        if (arguments != null) {
            return (Products) arguments.get(getString(R.string.currentSelectedProduct));
        }
        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.payByHand:
                isPaymentMethodCorrect = true;
                break;
            case R.id.payByCashOnDelivery:
                isPaymentMethodCorrect = false;
                break;
            case R.id.payByPaypal:
                isPaymentMethodCorrect = false;
                break;
            case R.id.payByStripe:
                isPaymentMethodCorrect = false;
                break;
        }
    }

    public Boolean getPaymentMethodCorrect() {
        return isPaymentMethodCorrect;
    }

    public Products getCurrentProduct() {
        return product;
    }
}
