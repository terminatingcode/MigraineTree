package com.terminatingcode.android.migrainetree.model.amazonaws.downloader;

public interface ResponseHandler {
    void onSuccess(long downloadId);
    void onError(String errorMessage);
}
