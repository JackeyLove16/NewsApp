package com.darjeeling.newsappp.repository

import com.darjeeling.newsappp.local.ArticleDatabase
import com.darjeeling.newsappp.model.Article
import com.darjeeling.newsappp.remote.NewsApi
import com.darjeeling.newsappp.util.Constants.Companion.API_KEY
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val database: ArticleDatabase,
    private val newsApi: NewsApi,
) {

    suspend fun getAllArticles(searchQuery: String, pageNumber: Int) =
        newsApi.getNews(searchQuery, pageNumber, API_KEY)

    fun getFavoriteArticles() = database.articleDao().getArticles()

    suspend fun insert(article: Article) = database.articleDao().insert(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)
}