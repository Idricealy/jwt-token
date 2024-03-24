package com.idricealy.jwttokens.service

import com.idricealy.jwttokens.model.Article
import com.idricealy.jwttokens.repository.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository
) {
    fun findAll(): List<Article> = articleRepository.findAll()
}