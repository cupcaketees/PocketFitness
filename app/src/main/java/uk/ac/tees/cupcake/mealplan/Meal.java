package uk.ac.tees.cupcake.mealplan;

public class Meal {

    private String name;
    private Double calories;
    private String id;

    public Meal(){

    }

    public Meal(String name, Double calories){
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }
}
