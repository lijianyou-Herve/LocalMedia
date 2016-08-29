package com.example.herve.localmedia.utils.pickmedia;

import android.os.Handler;
import android.os.Message;

import java.util.List;

/**
 * Created by hupei on 2016/7/14.
 */
public class PickMediaHandler extends Handler {
    public final static int SCAN_OK = 1;
    public final static int SCAN_ERROR = 2;

    private PickMediaCallBack mCallback;

    public PickMediaHandler(PickMediaCallBack callback) {
        this.mCallback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SCAN_OK:
                List<PickMediaTotal> list = (List<PickMediaTotal>) msg.obj;
                mCallback.onSuccess(list);
                break;
            case SCAN_ERROR:
                mCallback.onError();
                break;
        }
    }
}