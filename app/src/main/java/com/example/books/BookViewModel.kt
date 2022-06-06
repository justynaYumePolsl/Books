package com.example.books

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.books.dataBase.BookData

class BookViewModel(application: Application): AndroidViewModel(application) {
    var id: Long
    var book: BookData
    var autor: String?
    var title: String?
    var gatunek:String?
    var opinia: String?
    var status: Boolean?

    init {
        id=0L
        book=BookData()
        autor=null
        title=null
        gatunek=null
        opinia=null
        status=false
    }
}