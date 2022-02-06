package com.kirill.kotlinblog.domain.dto.transform

interface Transform<A,B> {
    fun transform(source:A):B
}