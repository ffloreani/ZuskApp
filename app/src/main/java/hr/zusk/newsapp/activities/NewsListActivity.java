package hr.zusk.newsapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import hr.zusk.newsapp.R;
import hr.zusk.newsapp.ZuskApplication;
import hr.zusk.newsapp.adapters.NewsListAdapter;
import hr.zusk.newsapp.model.Article;
import hr.zusk.newsapp.network.SyncHTTPCall;

public class NewsListActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE = "hr.zusk.newsapp.extra_article";

    private static final boolean DEBUG = false;

    private List<Article> articleList;

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewsListAdapter adapter;

    private NewsListAdapter.OnArticleSelectedListener listener = new NewsListAdapter.OnArticleSelectedListener() {
        @Override
        public void onArticleSelected(Article article) {
            Intent intent = new Intent(NewsListActivity.this, ArticleDetailsActivity.class);
            intent.putExtra(EXTRA_ARTICLE, article);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        if (!DEBUG) {
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchArticlesAsync(0);
                }
            });
        }
        swipeContainer.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        articleList = ZuskApplication.getInstance().getArticleList();
        if (articleList.size() == 0) {
            if (!DEBUG) {
                fetchArticlesAsync(0);
            } else {
                loadArticles();
            }
        }

        adapter = new NewsListAdapter(this, listener);
        recyclerView.setAdapter(adapter);
    }

    // This should call the HTTP request task
    public void fetchArticlesAsync(int page) {
       new ArticleTask().execute(page);
    }

    public void loadArticles() {
        articleList.add(new Article("Prvi", "Ovo je moj prvi clanak. Njime testiram prelamanje teksta, sto bi znacilo da ovo mora bit dovoljno dugacko da ispuni 2 reda teksta i prelije se preko, cime ce se pokazati trotocje (ako je ovo uopce rijec na ovom nasem divnom jeziku). Meh, valjda je dosta.", "01/10/2016"));
        articleList.add(new Article("Drugi", "Ovo je moj drugi clanak.", "02/10/2016"));
        articleList.add(new Article("Treci", "Ovo je moj treci clanak.", "03/10/2016"));
        articleList.add(new Article("Cetvrti", "Ovo je moj cetvrti clanak.", "04/10/2016"));
        articleList.add(new Article("Peti", "Ovo je moj peti clanak.", "05/10/2016"));
        articleList.add(new Article("Sesti", "Ovo je moj sesti clanak.", "06/10/2016"));
        articleList.add(new Article("Sedmi", "Ovo je moj sedmi clanak.", "07/10/2016"));
        articleList.add(new Article("Osmi", "Ovo je moj osmi clanak.", "08/10/2016"));
        articleList.add(new Article("Deveti", "Ovo je moj deveti clanak.", "09/10/2016"));
        articleList.add(new Article("Deseti", "Ovo je moj deseti clanak.", "10/10/2016"));
        articleList.add(new Article("Jedanaesti", "Ovo je moj jedanaesti clanak.", "11/10/2016"));
        articleList.add(new Article("Sto cetrdesest cetvrti", "Evo ne znam vise sta da pisem.", "31/12/2168"));
        articleList.add(new Article("Sedamsto sedamdeset cetvrti", "Kriza identiteta.", "03/11/2216"));
        articleList.add(new Article("Captains log", "Captains' log, stardate 2256.26.", "26/01/2256"));
    }

    private class ArticleTask extends AsyncTask<Integer, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Integer... integers) {
            return SyncHTTPCall.getInstance().getArticles(integers[0]);
        }

        @Override
        protected void onPostExecute(List<Article> loadedArticles) {
            super.onPostExecute(loadedArticles);

            swipeContainer.setRefreshing(false);
            if (loadedArticles.size() == 0) return;

            articleList.clear();
            articleList.addAll(loadedArticles);
            adapter.clear();
            adapter.addAll(loadedArticles);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
