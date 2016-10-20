package hr.zusk.newsapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import hr.zusk.newsapp.R;
import hr.zusk.newsapp.model.Article;


public class ArticleDetailsActivity extends AppCompatActivity {

    private Article article;

    private TextView title;
    private TextView body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        if(getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);

        article = getIntent().getParcelableExtra(NewsListActivity.EXTRA_ARTICLE);

        if(article != null) {
            setData();
        }
    }

    private void setData() {
        title.setText(article.getTitle());
        body.setText(article.getBody());
    }
}
