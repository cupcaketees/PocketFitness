package uk.ac.tees.cupcake.dietplan;

import android.os.Parcel;
import android.os.Parcelable;

public class Diet implements Parcelable {

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

    public Diet(String day, String starter, String starterDisc, String starterTime, String lunch, String lunchDesc, String lunchTime, String dinner, String dinnerDesc, String dinnerTime) {
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

    public Diet(Parcel in) {
        day = in.readString();
        starter = in.readString();
        this.starterDisc = in.readString();
        this.starterTime = in.readString();
        this.lunch = in.readString();
        this.lunchDesc = in.readString();
        this.lunchTime = in.readString();
        this.dinner = in.readString();
        this.dinnerDesc = in.readString();
        this.dinnerTime = in.readString();
    }

    public String getStarterTime() {
        return starterTime;
    }

    public String getLunchTime() {
        return lunchTime;
    }

    public String getDinnerTime() {
        return dinnerTime;
    }

    public String getDay() {
        return day;
    }

    public String getStarter() {
        return starter;
    }

    public String getStarterDisc() {
        return starterDisc;
    }

    public String getLunch() {
        return lunch;
    }

    public String getLunchDesc() {
        return lunchDesc;
    }

    public String getDinner() {
        return dinner;
    }

    public String getDinnerDesc() {
        return dinnerDesc;
    }

    public static final Creator<Diet> CREATOR = new Creator<Diet>(){
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
}
