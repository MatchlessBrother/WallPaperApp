package company.petrifaction.boss.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class StyleData implements Parcelable
{
    private String id;
    private String count;
    @SerializedName("name")
    private String description;
    @SerializedName("cover_image_thumb_url")
    private String downloadUrl;
    private int downloadImgHeight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getDownloadImgHeight() {
        return downloadImgHeight;
    }

    public void setDownloadImgHeight(int downloadImgHeight) {
        this.downloadImgHeight = downloadImgHeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.count);
        dest.writeString(this.description);
        dest.writeString(this.downloadUrl);
        dest.writeInt(this.downloadImgHeight);
    }

    public StyleData() {
    }

    protected StyleData(Parcel in) {
        this.id = in.readString();
        this.count = in.readString();
        this.description = in.readString();
        this.downloadUrl = in.readString();
        this.downloadImgHeight = in.readInt();
    }

    public static final Creator<StyleData> CREATOR = new Creator<StyleData>() {
        @Override
        public StyleData createFromParcel(Parcel source) {
            return new StyleData(source);
        }

        @Override
        public StyleData[] newArray(int size) {
            return new StyleData[size];
        }
    };
}