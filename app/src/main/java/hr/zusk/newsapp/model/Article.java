package hr.zusk.newsapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by filipfloreani on 17/10/2016.
 */
public class Article implements Parcelable {

    @NonNull
    private String title;
    @NonNull
    private String body;
    @NonNull
    private String created;

    public Article(@NonNull String title, @NonNull String body, @NonNull String created) {
        this.title = title;
        this.body = body;
        this.created = created;
    }

    public Article(@NonNull String title, @NonNull String body, @NonNull Date created) {
        this(title, body, created.toString());
    }

    protected Article(Parcel in) {
        title = in.readString();
        body = in.readString();
        created = in.readString();
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
    public String getCreated() {
        return created;
    }

    public void setCreated(@NonNull String created) {
        this.created = created;
    }

    public void setCreated(@NonNull Date created) {
        this.created = created.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (!title.equals(article.title)) return false;
        if (!body.equals(article.body)) return false;
        return created.equals(article.created);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + created.hashCode();
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
        parcel.writeString(created);
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
