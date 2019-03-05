package company.petrifaction.boss.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class PhotoData implements Parcelable
{
    private String id;
    @SerializedName("w")
    private String width;
    private int position;
    @SerializedName("h")
    private String height;
    @SerializedName("name")
    private String description;
    private boolean isFavorite;
    @SerializedName("img_url")
    private String downloadUrl;
    private int downloadImgHeight;
    @SerializedName("img_thumb_url")
    private String downloadThumbUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public String getDownloadThumbUrl() {
        return downloadThumbUrl;
    }

    public void setDownloadThumbUrl(String downloadThumbUrl) {
        this.downloadThumbUrl = downloadThumbUrl;
    }

    public int getDownloadImgHeight() {
        return downloadImgHeight;
    }

    public void setDownloadImgHeight(int downloadImgHeight) {
        this.downloadImgHeight = downloadImgHeight;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.width);
        dest.writeInt(this.position);
        dest.writeString(this.height);
        dest.writeString(this.description);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.downloadUrl);
        dest.writeInt(this.downloadImgHeight);
        dest.writeString(this.downloadThumbUrl);
    }

    public PhotoData() {
    }

    protected PhotoData(Parcel in) {
        this.id = in.readString();
        this.width = in.readString();
        this.position = in.readInt();
        this.height = in.readString();
        this.description = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.downloadUrl = in.readString();
        this.downloadImgHeight = in.readInt();
        this.downloadThumbUrl = in.readString();
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>() {
        @Override
        public PhotoData createFromParcel(Parcel source) {
            return new PhotoData(source);
        }

        @Override
        public PhotoData[] newArray(int size) {
            return new PhotoData[size];
        }
    };
}