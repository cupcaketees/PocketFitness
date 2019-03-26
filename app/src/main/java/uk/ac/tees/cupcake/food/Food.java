package uk.ac.tees.cupcake.food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by /**
 * {@author Bradley Hunter < s6263464@live.tees.ac.uk}
 * {@author Sam Hammersley < q5315908@live.tees.ac.uk}
 */

public final class Food {

    private final String label;
    private final String category;
    private final Map<String, Double> nutritionalValues;

    public Food(String label, Map<String, Double> nutritionalValues, String category){
        this.label = label;
        this.category = category;
        this.nutritionalValues = nutritionalValues;
    }

    public static List<Food> fromJSONArray(JSONArray array){
        List<Food> foods = new LinkedList<>();

        try{
            for (int index = 0; index < array.length(); index++){

                JSONObject foodObject = array.getJSONObject(index).getJSONObject("food");

                String label = foodObject.getString("label");
                JSONObject nutrients = foodObject.getJSONObject("nutrients");
                String category = foodObject.getString("category");

                Map<String,Double> nutritionalValues = new HashMap<>();

                Iterator<String> it = nutrients.keys();
                while(it.hasNext()){
                    String key = it.next();
                    double value = nutrients.getDouble(key);
                    nutritionalValues.put(key,value);
                }

                foods.add(new Food(label,nutritionalValues, category));
            }

            return foods;
        }catch(JSONException e){
            throw new RuntimeException("Failed to parse food response ", e);
        }
    }

    public static List<Food> fromJSONObject(String jsonString){
        try {
            JSONObject object = new JSONObject(jsonString);

            List<Food> food = fromJSONArray(object.getJSONArray("parsed"));
            food.addAll(fromJSONArray(object.getJSONArray("hints")));

            return food;
        } catch(JSONException e) {
            throw new RuntimeException("Failed to parse food response", e);
        }
    }

    private static final Map<String, String> NUTRITIONAL_LABELS = new HashMap<>();

    static {
        NUTRITIONAL_LABELS.put("procnt", "protein");
        NUTRITIONAL_LABELS.put("fat", "fat");
        NUTRITIONAL_LABELS.put("fibtg", "fibre");
        NUTRITIONAL_LABELS.put("enerc_kcal", "calories");
        NUTRITIONAL_LABELS.put("chocdf", "carbohydrates");
    }

    public static String getReadableLabel(String tag) {
        return NUTRITIONAL_LABELS.get(tag.toLowerCase());
    }

    @Override
    public String toString() {
        return label + "" + nutritionalValues;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() { return category; }

    public Map<String, Double> getNutritionalValues() { return nutritionalValues; }
}
