package com.hzease.tomeet.data;

/**
 * Created by xuq on 2017/9/6.
 */

public class JSData {
    /**
     * image : https://hzease.com/activity9/banner/share2.png
     * message : 我正在参加星球崛起3：终极之战免费观影活动，快来帮TA助力吧！
     * title : 星球崛起3：终极之战免费观影
     * url : https://hzease.com/activity9/planetRise.html
     */

    private String image;
    private String message;
    private String title;
    private String url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "JSData{" +
                "image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
