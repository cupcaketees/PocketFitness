package uk.ac.tees.cupcake.utils;

import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.home.health.Pace;
import uk.ac.tees.cupcake.home.health.Sex;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HealthUtility {
    
    public static int calculateCaloriesBurnedMovement(float weight, float height, Sex sex, int age, float distance, long time) {
        long seconds = time / 1000;
        float averagePace = (distance / 100) / seconds;
        float bmr = calculateBmr(weight, height, sex, age);
        Pace pace = Pace.getBracket(averagePace);
        
        return Math.round(bmr * (pace.getMetabolicEquivalent() / 24) * seconds / 60 / 60);
    }
    
    public static int calculateCaloriesBurnedMovement(float bmr, float distance, long time) {
        long seconds = time / 1000;
        float averagePace = (distance / 100) / seconds;
        Pace pace = Pace.getBracket(averagePace);
        
        return Math.round(bmr * (pace.getMetabolicEquivalent() / 24) * seconds / 60 / 60);
    }
    
    public static float calculateBmr(float weight, float height, Sex sex, int age) {
        if (sex == Sex.MALE) {
            return (float) (66.47 + (13.7 * weight) + (5 * height) - (6.8 * age));
        } else {
            return (float) (655.1 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
        }
    }
    
    public static float calculateBmr(UserProfile profile) {
        return calculateBmr(profile.getWeight(), profile.getHeight(), profile.getSex(), 23);
    }
    
}