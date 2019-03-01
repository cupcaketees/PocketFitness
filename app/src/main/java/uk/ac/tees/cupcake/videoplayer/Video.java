package uk.ac.tees.cupcake.videoplayer;

/**
 * Video Class
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class Video {

    private String name;
    private String desc;
    private int image;
    private String url;
    private String extra;
    private String time;

    /**
     * All the parameters a video will require.
     *
     * @param name  - Video Name
     * @param desc  - Video Description
     * @param image - Video Image
     * @param url   - Firebase URL
     * @param extra - Intent putExtra
     * @param time  - Duration of video (possibly retrievable from the video itself).
     */
    public Video(String name, String desc, int image, String url, String extra, String time) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.url = url;
        this.extra = extra;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
