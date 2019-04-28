package uk.ac.tees.cupcake.dietplan;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Diet Class
 * Parcelable object so can be passed through PutExtraParcelable and read.
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class Diet implements Parcelable {

    private String dietName;
    private String dietDescription;

    private String day;
    private String starter;
    private String starterDisc;
    private String starterTime;
    private String lunch;
    private String lunchDesc;
    private String lunchTime;
    private String dinner;
    private String dinnerDesc;
    private String dinnerTime;


    Diet(String day, String starter, String starterDisc, String starterTime, String lunch, String lunchDesc, String lunchTime, String dinner, String dinnerDesc, String dinnerTime) {
        this.day = day;
        this.starter = starter;
        this.starterDisc = starterDisc;
        this.starterTime = starterTime;
        this.lunch = lunch;
        this.lunchDesc = lunchDesc;
        this.lunchTime = lunchTime;
        this.dinner = dinner;
        this.dinnerDesc = dinnerDesc;
        this.dinnerTime = dinnerTime;
    }

    Diet(String dietName, String dietDescription) {
        this.dietName = dietName;
        this.dietDescription = dietDescription;
    }

    /**
     * This is what the object is stored in as a Parcel.
     */
    private Diet(Parcel in) {
        this.day = in.readString();
        this.starter = in.readString();
        this.starterDisc = in.readString();
        this.starterTime = in.readString();
        this.lunch = in.readString();
        this.lunchDesc = in.readString();
        this.lunchTime = in.readString();
        this.dinner = in.readString();
        this.dinnerDesc = in.readString();
        this.dinnerTime = in.readString();
    }


    /**
     * Converts it from a Parcel to a Object.
     */
    public static final Creator<Diet> CREATOR = new Creator<Diet>() {
        public Diet createFromParcel(Parcel in) {
            return new Diet(in);
        }

        public Diet[] newArray(int size) {
            return new Diet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writing to Parcel
     *
     * @param dest - Passes through every variable in the object to be stored.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getDay());
        dest.writeString(getStarter());
        dest.writeString(getStarterDisc());
        dest.writeString(getStarterTime());
        dest.writeString(getLunch());
        dest.writeString(getLunchDesc());
        dest.writeString(getLunchTime());
        dest.writeString(getDinner());
        dest.writeString(getDinnerDesc());
        dest.writeString(getDinnerTime());


    }


    public String getDietName() {
        return dietName;
    }

    public String getDietDescription() {
        return dietDescription;
    }

    String getStarterTime() {
        return starterTime;
    }

    String getLunchTime() {
        return lunchTime;
    }

    String getDinnerTime() {
        return dinnerTime;
    }

    public String getDay() {
        return day;
    }

    String getStarter() {
        return starter;
    }

    String getStarterDisc() {
        return starterDisc;
    }

    String getLunch() {
        return lunch;
    }

    String getLunchDesc() {
        return lunchDesc;
    }

    String getDinner() {
        return dinner;
    }

    String getDinnerDesc() {
        return dinnerDesc;
    }

    /**
     * @return - Diet as toString to be read in the log messages to ensure its passing the correct data.
     */
    @Override
    public String toString() {
        return "Diet{" +
                "day='" + day + '\'' +
                ", starter='" + starter + '\'' +
                ", starterDisc='" + starterDisc + '\'' +
                ", starterTime='" + starterTime + '\'' +
                ", lunch='" + lunch + '\'' +
                ", lunchDesc='" + lunchDesc + '\'' +
                ", lunchTime='" + lunchTime + '\'' +
                ", dinner='" + dinner + '\'' +
                ", dinnerDesc='" + dinnerDesc + '\'' +
                ", dinnerTime='" + dinnerTime + '\'' +
                '}';
    }
}
