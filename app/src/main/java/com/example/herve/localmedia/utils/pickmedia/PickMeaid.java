package com.example.herve.localmedia.utils.pickmedia;

import java.util.List;

/**
 * Created by Herve on 2016/7/20.
 */
public interface PickMeaid<T extends MediaBean> {
    void start();

   List<T> getChildPathList(int position);
}
