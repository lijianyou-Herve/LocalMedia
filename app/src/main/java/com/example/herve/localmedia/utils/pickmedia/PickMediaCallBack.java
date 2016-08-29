package com.example.herve.localmedia.utils.pickmedia;

import java.util.List;

/**
 * Created by Herve on 2016/7/20.
 */
public interface PickMediaCallBack {
    void onStart();

    void onSuccess(List<PickMediaTotal> list);

    void onError();
}
