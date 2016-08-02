package com.terminatingcode.android.migrainetree.amazonaws.downloader;

public interface ResponseHandler {
    void onSuccess(long downloadId);
    void onError(String errorMessage);
}
