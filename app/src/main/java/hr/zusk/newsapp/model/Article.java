package hr.zusk.newsapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by filipfloreani on 17/10/2016.
 */
public class Article implements Parcelable {

    @NonNull
    private String title;
    @NonNull
    private String body;
    @NonNull
    private String detailsUrl;

    public Article() {}

    public Article(@NonNull String title, @NonNull String body) {
        this.title = title;
        this.body = body;
    }

    protected Article(Parcel in) {
        title = in.readString();
        body = in.readString();
        detailsUrl = in.readString();
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    public void setBody(@NonNull String body) {
        this.body = body;
    }

    @NonNull
    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(@NonNull String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (!title.equals(article.title)) return false;
        return detailsUrl.equals(article.detailsUrl);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + detailsUrl.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(detailsUrl);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

}
