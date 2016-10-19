package hr.zusk.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;
import java.util.Vector;

import hr.zusk.newsapp.model.Article;

public class NewsListActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE = "hr.zusk.newsapp.extra_article";

    private static final boolean DEBUG = true;

    private List<Article> articleList = new Vector<>();

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadArticles();

        adapter = new NewsListAdapter(articleList, this, listener);
        recyclerView.setAdapter(adapter);
    }

    public void loadArticles() {
        // This should run a simple AsyncTask that hosts the HTTP connection & article parser
        if (DEBUG) {
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
    }
}
