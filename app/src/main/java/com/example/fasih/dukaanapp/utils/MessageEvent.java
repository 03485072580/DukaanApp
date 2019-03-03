package com.example.fasih.dukaanapp.utils;

import android.net.Uri;

/**
 * Created by Fasih on 03/03/19.
 */

public class MessageEvent {

    private Uri capturedImageUri;

    public Uri getCapturedImageUri() {
        return capturedImageUri;
    }

    public void setCapturedImageUri(Uri capturedImageUri) {
        this.capturedImageUri = capturedImageUri;
    }
}
