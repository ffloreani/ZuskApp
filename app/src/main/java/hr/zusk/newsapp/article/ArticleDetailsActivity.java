package hr.zusk.newsapp.article;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hr.zusk.newsapp.R;
import hr.zusk.newsapp.ZuskApplication;
import hr.zusk.newsapp.network.ASyncHTTPCall;
import hr.zusk.newsapp.newslist.NewsListActivity;


public class ArticleDetailsActivity extends AppCompatActivity {

    private Article article;

    private TextView title;
    private TextView body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        if(getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);

        article = getIntent().getParcelableExtra(NewsListActivity.EXTRA_ARTICLE);

        fetchFullArticle();
    }

    private void setData() {
        if(article == null) return;

        title.setText(article.getTitle());
        body.setText(article.getBody());
    }

    private void fetchFullArticle() {
        new FullArticleTask().execute();
    }

    private class FullArticleTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(ArticleDetailsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            ASyncHTTPCall.getInstance().getFullArticle(article);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            setData();
        }
    }
}
