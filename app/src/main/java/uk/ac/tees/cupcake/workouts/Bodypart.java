package uk.ac.tees.cupcake.workouts;

import android.content.Context;

public class Bodypart {

    private String name;
    private String desc;
    private int image;
    private Class<?> className;

    /**
     * All the parameters a video will require.
     *  @param name  - Workout Name
     * @param desc  - Workout Description
     * @param image - Workout Image
     * @param className
     */
    public Bodypart(String name, String desc, int image, Class<?> className)
    {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.className = className;

    }

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
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


