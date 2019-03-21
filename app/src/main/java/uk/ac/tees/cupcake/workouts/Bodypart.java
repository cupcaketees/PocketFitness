package uk.ac.tees.cupcake.workouts;

public class Bodypart {

    private String name;
    private String desc;
    private int image;

    /**
     * All the parameters a video will require.
     *
     * @param name  - Workout Name
     * @param desc  - Workout Description
     * @param image - Workout Image
     */
    public Bodypart(String name, String desc, int image)
    {
        this.name = name;
        this.desc = desc;
        this.image = image;
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

}


