package xyz.filipfloreani.zusk.newslist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import xyz.filipfloreani.zusk.BuildConfig;
import xyz.filipfloreani.zusk.R;
import xyz.filipfloreani.zusk.ZuskApplication;
import xyz.filipfloreani.zusk.article.Article;
import xyz.filipfloreani.zusk.article.ArticleDetailsActivity;
import xyz.filipfloreani.zusk.network.ASyncHTTPCall;

public class NewsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_ARTICLE = "hr.zusk.newsapp.extra_article";

    private List<Article> articleList;

    private SwipeRefreshLayout swipeContainer;
    private NewsListAdapter adapter;

    private EndlessScrollListener scrollListener;
    private NewsListAdapter.OnArticleSelectedListener articleSelectedListener = new NewsListAdapter.OnArticleSelectedListener() {
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

        // Swipe container setup
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));

        // Recycler view setup
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Adding horizontal divider between views
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        // Adding scroll listener for loading more data on scroll
        scrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount) {
                readNewArticleLoad(totalItemsCount / 4);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        if(getSupportActionBar() == null) return;
        getSupportActionBar().setTitle(getString(R.string.zusk));

        // Load data on app start
        articleList = ZuskApplication.getInstance().getArticleList();
        if (articleList.size() == 0) {
            readNewArticleLoad(0);
        }

        // Adapter setup
        adapter = new NewsListAdapter(articleSelectedListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog() {
        String message = String.format(getResources().getString(R.string.about_text), BuildConfig.VERSION_NAME);
        String title = getResources().getString(R.string.about);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Clears the current article list, loads new articles from the web & invalidates the adapter.
     */
    @Override
    public void onRefresh() {
        if (articleList != null) {
            articleList.clear();
        }

        if (scrollListener != null) scrollListener.reset();
        readNewArticleLoad(0);
    }

    /**
     * Loads the initial data set, which includes the 16 newest articles.
     */
    private void readNewArticleLoad(int startPage) {
        for (int i = startPage; i < startPage + 4; i++) {
                new IndexArticlesTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, i);
        }
    }

    /**
     * Custom AsyncTask that's used to load a single index page which contains 4 articles.
     */
    private class IndexArticlesTask extends AsyncTask<Integer, Void, List<Article>> {

        @Override
        protected List<Article> doInBackground(Integer... integers) {
            return ASyncHTTPCall.getInstance().getIndexArticles(integers[0]);
        }

        @Override
        protected void onPostExecute(List<Article> loadedArticles) {
            super.onPostExecute(loadedArticles);

            swipeContainer.setRefreshing(false);
            if (loadedArticles.size() == 0) return;

            addAll(loadedArticles);
            adapter.notifyDataSetChanged();
        }

        /**
         * Adds articles from given list which aren't already contained
         * in the main article list.
         *
         * @param loadedArticles Freshly loaded article list from the web
         */
        private void addAll(List<Article> loadedArticles) {
            for (Article article : loadedArticles) {
                if (!articleList.contains(article)) {
                    articleList.add(article);
                }
            }
        }
    }
}
