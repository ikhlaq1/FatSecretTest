package broken.shotgun.fatsecret.activities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by white on 10/19/2017.
 */

public class CurrentData implements Parcelable{
    private String food_name;
    private String food_description;

    public CurrentData(){

    }



    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(food_name);
        parcel.writeString(food_description);
    }

    private CurrentData(Parcel in){
        food_name=in.readString();
        food_description=in.readString();
    }

    public static final Creator<CurrentData> CREATOR = new Creator<CurrentData>() {
        @Override
        public CurrentData createFromParcel(Parcel in) {
            return new CurrentData(in);
        }

        @Override
        public CurrentData[] newArray(int size) {
            return new CurrentData[size];
        }
    };
}
