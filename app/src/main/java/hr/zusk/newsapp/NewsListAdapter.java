package hr.zusk.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

import hr.zusk.newsapp.model.Article;

/**
 * Created by filipfloreani on 17/10/2016.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ArticleViewHolder> {

    public interface OnArticleSelectedListener {
        void onArticleSelected(Article article);
    }

    private List<Article> articles = new Vector<>();
    private Context context;
    private OnArticleSelectedListener onArticleSelectedListener;

    public NewsListAdapter(List<Article> articles, Context context, OnArticleSelectedListener listener) {
        super();
        this.articles = articles;
        this.context = context;
        this.onArticleSelectedListener = listener;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_article, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.setData(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    class ArticleViewHolder extends RecyclerView.ViewHolder {

        View articleView;
        TextView titleView;
        TextView createdView;
        TextView bodyView;

        ArticleViewHolder(View itemView) {
            super(itemView);

            articleView = itemView.findViewById(R.id.article_card);

            titleView = (TextView) articleView.findViewById(R.id.article_title);
            createdView = (TextView) articleView.findViewById(R.id.article_created);
            bodyView = (TextView) articleView.findViewById(R.id.article_body);
        }

        void setData(final Article article) {
            titleView.setText(article.getTitle());
            createdView.setText(article.getCreated());
            bodyView.setText(article.getBody());

            articleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onArticleSelectedListener.onArticleSelected(article);
                }
            });
        }
    }
}
