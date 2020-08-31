package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.news_app.API.ApiClient;
import com.example.news_app.API.Api_Interface;
import com.example.news_app.Models.Article;
import com.example.news_app.Models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String Api_KEY =  "e5cf6af3e9e640f393de82a8a07da6aa";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Article> articles = new ArrayList<>();
    Adapter adapter;
    String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(new int[]{R.color.colorAccent});
        recyclerView = findViewById(R.id.recycle_view);

        layoutManager = new LinearLayoutManager(this , RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        on_loading_swip_fresh("");

    }

    private void load_JSON( String key_word) {
        Api_Interface api_interface = ApiClient.getApiClient().create(Api_Interface.class);
        swipeRefreshLayout.setRefreshing(true);
        String country = Utils.getCountry();
        String lang = Utils.getLanguage();

        Call<News> call ;
        if (key_word.length()>0){
            call= api_interface.getNewsSearch(key_word,lang , "PublishedAt" , Api_KEY);
        }
        else {
            call= api_interface.getNews(country,Api_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()&&response.body().getArticle()!=null){
                    if (!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    adapter = new Adapter(articles,MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    intiate_lisner();
                    swipeRefreshLayout.setRefreshing(false);

                }
                else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Lasted News ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>2)
                    on_loading_swip_fresh(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                load_JSON(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false,false);


        return true;
    }

    @Override
    public void onRefresh() {
        load_JSON("");
    }

    private void on_loading_swip_fresh(final String keyword){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                load_JSON(keyword);
            }
        });
    }

   void intiate_lisner(){
        adapter.set_on_item_click_lisner(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this , News_details_activity.class);
                Article article = articles.get(position);
                intent.putExtra("title" , article.getTitle());
                intent.putExtra("url" , article.getUrl());
                intent.putExtra("img" , article.getUrlToImage());
                intent.putExtra("date" , article.getPublishedAt());
                intent.putExtra("source" , article.getSource().getName());
                intent.putExtra("author" , article.getAuthor());


                startActivity(intent);
            }
        });
   }

}
