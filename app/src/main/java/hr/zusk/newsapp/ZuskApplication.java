package hr.zusk.newsapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import java.util.List;
import java.util.Vector;

import hr.zusk.newsapp.model.Article;
import io.fabric.sdk.android.Fabric;

/**
 * Created by filipfloreani on 20/10/2016.
 */
public class ZuskApplication extends Application {

    private static ZuskApplication instance;

    private static List<Article> articleList = new Vector<>();

    public static ZuskApplication getInstance() {
        if (instance == null) {
            instance = new ZuskApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        articleList = new Vector<>();

        Fabric.with(this, new Crashlytics());
    }

    public List<Article> getArticleList() {
        return articleList;
    }
}
