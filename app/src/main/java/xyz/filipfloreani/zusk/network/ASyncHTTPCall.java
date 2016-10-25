package xyz.filipfloreani.zusk.network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import xyz.filipfloreani.zusk.article.Article;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by filipfloreani on 20/10/2016.
 */
public class ASyncHTTPCall {
    // Intent extra label
    private static final String START_PARAM_NAME = "start";
    // Web URLs
    private static final String ZUSK_BASE_URL = "http://www.zusk.hr";
    private static final String INDEX_URL = "/index.php";
    // CSS selectors
    private static final String INDEX_QUERY = "div[class*=leading-]";
    private static final String ARTICLE_DETAILS_QUERY = "div[itemprop=articleBody]";
    // Double new line
    private static final String DOUBLE_NEW_LINE = "\n\n";

    private static ASyncHTTPCall instance;

    public static ASyncHTTPCall getInstance() {
        if (instance == null) {
            instance = new ASyncHTTPCall();
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

    public List<Article> getIndexArticles(int pageNum) {
        List<Article> articles = new Vector<>();
        Request request = generateIndexRequest(pageNum);

        ResponseBody responseBody = null;
        try {
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response);
            }

            responseBody = response.body();
            final String body = responseBody.string();

            articles = parseIndexResponse(body);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
        }

        return articles;
    }

    private Request generateIndexRequest(int pageNum) {
        String startParamValue = String.valueOf(pageNum * 4);

        HttpUrl.Builder httpBuilder = HttpUrl.parse(ZUSK_BASE_URL + INDEX_URL).newBuilder();
        httpBuilder.addQueryParameter(START_PARAM_NAME, startParamValue);
        String url = httpBuilder.build().toString();

        return new Request.Builder().url(url).build();
    }

    private List<Article> parseIndexResponse(String response) {
        List<Article> articles = new Vector<>();

        Document doc = Jsoup.parse(response);
        Elements posts = doc.select(INDEX_QUERY);
        for (Element post : posts) {
            Article article = new Article();

            Elements children = post.children();

            String title = children.get(0).text();
            String body = "";

            for (int i = 1; i < children.size() - 1; i++) {
                body += children.get(i).text();
            }
            if (body.equals("")) continue;

            Element detailsButtonLink = children.get(children.size() - 1).child(0);
            if (detailsButtonLink != null) {
                String detailsUrl = detailsButtonLink.attr("href");
                article.setDetailsUrl(detailsUrl);
            }

            article.setTitle(title);
            article.setBody(body);

            articles.add(article);
        }

        return articles;
    }

    public Article getFullArticle(Article article) {
        Request request = generateFullArticleRequest(article.getDetailsUrl());

        ResponseBody responseBody = null;
        try {
            Response response = getHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response);
            }

            responseBody = response.body();
            final String body = responseBody.string();

            parseFullArticleResponse(body, article);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
        }

        return article;
    }

    private Request generateFullArticleRequest(String detailsUrl) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(ZUSK_BASE_URL + detailsUrl).newBuilder();
        String url = httpBuilder.build().toString();

        return new Request.Builder().url(url).build();
    }

    private void parseFullArticleResponse(String response, Article article) {
        Document doc = Jsoup.parse(response);
        Elements posts = doc.select(ARTICLE_DETAILS_QUERY);
        for (Element post : posts) {
            Elements children = post.children();

            String elementBody = "";

            for (int i = 1; i < children.size(); i++) {
                elementBody += children.get(i).text();
                if (i != children.size() - 1) {
                    elementBody += DOUBLE_NEW_LINE;
                }
            }
            if (elementBody.equals("")) continue;

            article.setBody(elementBody);
        }
    }
}
