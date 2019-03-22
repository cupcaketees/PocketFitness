package uk.ac.tees.cupcake.workouts;
/**
 * Workout Class
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class Workout {

    private String name;
    private String desc;
    private int image;
    private String url;
    private String difficulty;

    /**
     * All the parameters a video will require.
     *
     * @param name  - Workout Name
     * @param desc  - Workout Description
     * @param image - Workout Image
     * @param url   - Firebase URL
     * @param difficulty - Workout Difficulty Rating
     */

    public Workout(String name, String desc, int image, String url, String difficulty) {

        this.name = name;
        this.desc = desc;
        this.image = image;
        this.url = url;
        this.difficulty = difficulty;

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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
