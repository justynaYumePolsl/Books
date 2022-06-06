package com.example.books.dataBase

data class BookData(
    var id: String? = null,
    var autor: String? = null,
    var title: String? = null,
    var gatunek: String? = null,
    var opinia: String? = null,
    var status: Boolean? = null
) {}