package com.example.herve.localmedia.utils.pickmedia;

import android.content.Context;

import com.example.herve.localmedia.utils.pickmedia.pick_audio.PickAudio;
import com.example.herve.localmedia.utils.pickmedia.pick_audio.PickAudioCallback;
import com.example.herve.localmedia.utils.pickmedia.pick_picture.PickPicture;
import com.example.herve.localmedia.utils.pickmedia.pick_picture.PickPictureCallback;
import com.example.herve.localmedia.utils.pickmedia.pick_video.PickVideo;
import com.example.herve.localmedia.utils.pickmedia.pick_video.PickVideoCallback;

import java.util.List;

import static com.example.herve.localmedia.utils.pickmedia.PickMediaHelper.PIckMediaType.Audio;
import static com.example.herve.localmedia.utils.pickmedia.PickMediaHelper.PIckMediaType.Picture;
import static com.example.herve.localmedia.utils.pickmedia.PickMediaHelper.PIckMediaType.Video;

/**
 * Created by hupei on 2016/7/14.
 */
public class PickMediaHelper {
    private PickMeaid mPickMeaid;

    private PickMediaHelper(Context context, PickMediaCallBack callback, PIckMediaType mediaType) {
        switch (mediaType) {
            case Picture:
                mPickMeaid = new PickPicture(context.getApplicationContext(), callback);
                break;
            case Video:
                mPickMeaid = new PickVideo(context.getApplicationContext(), callback);
                break;
            case Audio:
                mPickMeaid = new PickAudio(context.getApplicationContext(), callback);
                break;
        }
        mPickMeaid.start();
    }

    /**
     * 开始读取视频
     *
     * @param context
     * @param callback
     * @return
     */
    public static PickMediaHelper readVideo(Context context, PickMediaCallBack callback) {
        callback.onStart();
        return new PickMediaHelper(context, callback, Video);
    }

    /**
     * 开始读取相册
     *
     * @param context
     * @param callback
     * @return
     */
    public static PickMediaHelper readPicture(Context context, PickMediaCallBack callback) {
        callback.onStart();
        return new PickMediaHelper(context, callback, Picture);
    }

    /**
     * 开始读取相册
     *
     * @param context
     * @param callback
     * @return
     */
    public static PickMediaHelper readAudio(Context context, PickMediaCallBack callback) {
        callback.onStart();
        return new PickMediaHelper(context, callback, Audio);
    }

    /**
     * 取出子目录图片路径集合
     *
     * @param position
     * @return
     */
    public <T extends MediaBean> List<T> getChildPathList(int position) {
        if (mPickMeaid == null) return null;

        return mPickMeaid.getChildPathList(position);
    }

    enum PIckMediaType {
        Picture, Audio, Video;
    }
}
