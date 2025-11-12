package com.example.aura.data.mapper

import com.example.aura.data.local.entity.ArticleEntity
import com.example.aura.data.remote.dto.ArticleDTO
import com.example.aura.domain.model.Article

// Mapper Remoto (DTO) -> Domain
fun ArticleDTO.toDomain(): Article {
    return Article(
        id = id,
        titulo = titulo,
        descricao = descricao,
        conteudo = conteudo,
        dataPublicacao = dataPublicacao,
        fonte = fonte,
        url = url,
        imagem = imagem
    )
}

// Mapper Domain -> Local (Entity para salvar no banco)
fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        id = id,
        titulo = titulo,
        descricao = descricao,
        conteudo = conteudo,
        dataPublicacao = dataPublicacao,
        fonte = fonte,
        url = url,
        imagem = imagem
    )
}

// Mapper Local (Entity) -> Domain (para ler do banco)
fun ArticleEntity.toDomain(): Article {
    return Article(
        id = id,
        titulo = titulo,
        descricao = descricao,
        conteudo = conteudo,
        dataPublicacao = dataPublicacao,
        fonte = fonte,
        url = url,
        imagem = imagem
    )
}