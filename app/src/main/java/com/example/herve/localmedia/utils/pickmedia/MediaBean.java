package com.example.herve.localmedia.utils.pickmedia;

/**
 * Created by Herve on 2016/7/20.
 */
public class MediaBean {
    //文件名
    private String fileName;
    //路径名
    private String originalFilePath;
    //时长
    private String mediaLength;
    //创建时间
    private String mediaTakeTime;
    //修改时间
    private String mediaEditTime;
    //地址
    private String origId;
    //

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFilePath() {
        return originalFilePath;
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }

    public String getOrigId() {
        return origId;
    }

    public void setOrigId(String origId) {
        this.origId = origId;
    }

    public String getMediaLength() {
        return mediaLength;
    }

    public void setMediaLength(String mediaLength) {
        this.mediaLength = mediaLength;
    }

    public String getMediaTakeTime() {
        return mediaTakeTime;
    }

    public void setMediaTakeTime(String mediaTakeTime) {
        this.mediaTakeTime = mediaTakeTime;
    }

    public String getMediaEditTime() {
        return mediaEditTime;
    }

    public void setMediaEditTime(String mediaEditTime) {
        this.mediaEditTime = mediaEditTime;
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "fileName='" + fileName + '\'' +
                ", originalFilePath='" + originalFilePath + '\'' +
                ", mediaLength='" + mediaLength + '\'' +
                ", mediaTakeTime='" + mediaTakeTime + '\'' +
                ", mediaEditTime='" + mediaEditTime + '\'' +
                ", origId='" + origId + '\'' +
                '}';
    }
}

//// 某些手机使用上面的方法无法获取视频时长，则使用mediaPlayer来获取
//if (mediaLengthTmp <= 0) {
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        mediaPlayer.setDataSource(filmElementBean.originalFilePath);
//        mediaPlayer.prepare();
//        mediaLengthTmp = mediaPlayer.getDuration();
//
//        try {
//        mediaPlayer.release();
//        } catch (Exception e) {
//        e.printStackTrace();
//        }
//        }