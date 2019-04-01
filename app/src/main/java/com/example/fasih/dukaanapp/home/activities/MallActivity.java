package com.example.fasih.dukaanapp.home.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.ShopsListFragment;
import com.example.fasih.dukaanapp.home.interfaces.KeepHandleFragments;

public class MallActivity extends AppCompatActivity implements KeepHandleFragments {


    private String mallUniqueID;
    private String previousFragmentOrActivity, currentAttachedFragment;
    private Fragment shopsListFragment;

    @Override
    public void onCustomAttachFragment(String previousFragment
            , String currentAttachedFragment) {
        previousFragmentOrActivity = previousFragment;
        MallActivity.this.currentAttachedFragment = currentAttachedFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        setupActivityWidgets();
        setupIntentExtra(getIntent());
        if (savedInstanceState == null)
            setupPendingTransitions();
        else {
            shopsListFragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.shopsListFragment));
            mallUniqueID = savedInstanceState.getString(getString(R.string.db_field_user_id));
        }

    }

    private void setupPendingTransitions() {
        ShopsListFragment fragment = new ShopsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, getString(R.string.shopsListFragment))
                .commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.db_field_user_id), mallUniqueID);
    }

    private void setupActivityWidgets() {
    }

    public void setupIntentExtra(Intent intent) {

        if (intent != null) {
            if (intent.hasExtra(getString(R.string.db_field_user_id))) {

                mallUniqueID = intent.getStringExtra(getString(R.string.db_field_user_id));
                //Now you can use this ID for different Mall Shops Registration
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mall_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.getMallId) {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("mallUniqueID", mallUniqueID));
                Log.d("TAG1234", "onOptionsItemSelected: " + mallUniqueID);

                Toast.makeText(this, R.string.clipboardToastMsg, Toast.LENGTH_LONG).show();
            } catch (NullPointerException exc) {
                exc.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (!TextUtils.isEmpty(currentAttachedFragment) && !TextUtils.isEmpty(previousFragmentOrActivity)) {
            if (currentAttachedFragment.equals(getString(R.string.searchUsernameFragment))
                    && previousFragmentOrActivity.equals(getString(R.string.shopsListFragment))) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new ShopsListFragment(), getString(R.string.shopsListFragment))
                        .commitAllowingStateLoss();

            }
        }
        if (!TextUtils.isEmpty(currentAttachedFragment) && !TextUtils.isEmpty(previousFragmentOrActivity)) {
            Log.d("TAG1234", "onBackPressed: ");
            if (currentAttachedFragment.equals(getString(R.string.shopsListFragment))
                    && previousFragmentOrActivity.equals(getString(R.string.activity_mall))) {

                Log.d("TAG1234", "onBackPressed: Here it comes");
                super.onBackPressed();

            }
        }

    }

}
