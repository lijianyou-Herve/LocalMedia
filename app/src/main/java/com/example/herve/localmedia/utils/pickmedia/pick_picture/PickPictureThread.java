package com.example.herve.localmedia.utils.pickmedia.pick_picture;

/**
 * Created           :Herve on 2016/8/29.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/8/29
 * @ projectName     :LocalMedia
 * @ version
 */
public abstract class PickPictureThread extends Thread implements Runnable {
    public abstract void pickPictureThreadRun();
    @Override
    public void run() {
        pickPictureThreadRun();
    }
}
