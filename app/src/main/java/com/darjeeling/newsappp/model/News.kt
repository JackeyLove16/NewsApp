package com.darjeeling.newsappp.model

data class News(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int,
)