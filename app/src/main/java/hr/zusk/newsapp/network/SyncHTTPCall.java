package hr.zusk.newsapp.network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import hr.zusk.newsapp.model.Article;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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


    public List<Article> getArticles(int pageNum) {
        List<Article> articles = new Vector<>();
        Request request = generateRequest(pageNum);

        ResponseBody responseBody = null;
        try {
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response);
            }

            responseBody = response.body();
            final String body = responseBody.string();

            articles = parseResponse(body);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
        }

        return articles;
    }

    private Request generateRequest(int pageNum) {
        String startParamValue = String.valueOf(pageNum * 4);

        HttpUrl.Builder httpBuilder = HttpUrl.parse("http://www.zusk.hr/index.php").newBuilder();
        httpBuilder.addQueryParameter(START_PARAM_NAME, startParamValue);
        String url = httpBuilder.build().toString();

        return new Request.Builder().url(url).build();
    }

    private List<Article> parseResponse(String response) {
        List<Article> articles = new Vector<>();

        Document doc = Jsoup.parse(response);
        Elements posts = doc.select("div[class*=leading-]");
        for (Element post : posts) {
            Article article = new Article();

            Elements children = post.children();

            String title = children.get(0).text();
            String body = "";

            for (int i = 1; i < children.size() - 1; i++) {
                body += children.get(i).text();
            }
            if (body.equals("")) continue;

            article.setTitle(title);
            article.setBody(body);

            articles.add(article);
        }

        return articles;
    }
}
