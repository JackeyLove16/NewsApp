package com.darjeeling.newsappp.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darjeeling.newsappp.model.Article
import com.darjeeling.newsappp.model.News
import com.darjeeling.newsappp.repository.ArticleRepository
import com.darjeeling.newsappp.util.Constants.Companion.DEFAULT_TOPIC
import com.darjeeling.newsappp.util.Constants.Companion.STARTING_PAGE_INDEX
import com.darjeeling.newsappp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val articleRepository: ArticleRepository) : ViewModel() {

    val articles: MutableLiveData<Resource<News>> = MutableLiveData()
    private var newsResponse: News? = null
    var page = STARTING_PAGE_INDEX

    init {
        getAllArticles(DEFAULT_TOPIC)
    }

    fun getAllArticles(q: String) = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        val response = articleRepository.getAllArticles(q, page)
        articles.postValue(handleArticlesResponse(response))
    }

    fun getAllNewArticles(q: String) = viewModelScope.launch {
        articles.postValue(Resource.Loading())
        val response = articleRepository.getAllArticles(q, page)
        articles.postValue(handleNewArticlesResponse(response))
    }

    private fun handleArticlesResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let {
                page++
                if (newsResponse == null) {
                    newsResponse = it
                } else {
                    val oldArticles = newsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleNewArticlesResponse(response: Response<News>): Resource<News> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getFavoriteArticles() = articleRepository.getFavoriteArticles()

    fun addArticleToFavorites(article: Article) = viewModelScope.launch {
        articleRepository.insert(article)
    }

    fun removeArticleFromFavorites(article: Article) = viewModelScope.launch {
        articleRepository.deleteArticle(article)
    }
}