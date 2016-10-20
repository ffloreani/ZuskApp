package hr.zusk.newsapp.network;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import hr.zusk.newsapp.model.Article;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by filipfloreani on 20/10/2016.
 */
public class SyncHTTPCall {
    private static final String START_PARAM_NAME = "start";

    private static SyncHTTPCall instance;
    public static SyncHTTPCall getInstance() {
        if (instance == null) {
            instance = new SyncHTTPCall();
        }
        return instance;
    }

    private static OkHttpClient httpClient;
    private static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient();
        }
        return httpClient;
    }

    private Request generateRequest(int pageNum) {
        String startParamValue = String.valueOf(pageNum * 4);

        HttpUrl.Builder httpBuilder = HttpUrl.parse("http://www.zusk.hr/index.php").newBuilder();
        httpBuilder.addQueryParameter(START_PARAM_NAME, startParamValue);
        String url = httpBuilder.build().toString();

        return new Request.Builder().url(url).build();
    }

    //Needs to be run in AsyncTask
    public List<Article> getArticles(int pageNum) {
        List<Article> articles = new Vector<>();

        Request request = generateRequest(pageNum);

        getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected response code " + response);
                }

                final String responseBody = response.body().string();

                // Run HTML article parser and return a set of article objects

                // Store parsed articles in method set
            }
        });

        return articles;
    }
}
