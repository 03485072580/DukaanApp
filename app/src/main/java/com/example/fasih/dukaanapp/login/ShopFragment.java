package com.example.fasih.dukaanapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.activities.HomePageActivity;
import com.example.fasih.dukaanapp.register.RegisterActivity;

/**
 * Created by Fasih on 11/07/18.
 */

public class ShopFragment extends Fragment {

    private TextView signUp;
    private Button signIn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        setupFragmentWidgets(view);
        return view;
    }

    private void setupFragmentWidgets(View view) {
        signUp = view.findViewById(R.id.textView3);
        signIn = view.findViewById(R.id.button2);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(getString(R.string.shopFragment), getString(R.string.shopFragment));
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                startActivity(intent);
            }
        });
    }
}
