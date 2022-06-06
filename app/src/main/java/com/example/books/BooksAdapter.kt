package com.example.books

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.books.dataBase.BookData
import java.util.concurrent.Executors

class BooksAdapter(private val viewModel: BookViewModel, private val books: MutableLiveData<List<BookData>>, context: Context): RecyclerView.Adapter<BooksAdapter.MyView>() {
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var bookautor = view.findViewById<TextView>(R.id.bookauthor)
        var booktitle = view.findViewById<TextView>(R.id.booktitle)
        var myView = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.book,
            parent,
            false
        )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        //set photo
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        when (books.value?.get(position)?.status) {
            true -> holder.myView.findViewById<ImageView>(R.id.readornot)
                .setImageResource(R.drawable.taknanie)
            false -> holder.myView.findViewById<ImageView>(R.id.readornot)
                .setImageResource(R.drawable.nienaprze)
        }

        holder.bookautor.text = books.value?.get(position)?.autor
        holder.booktitle.text = books.value?.get(position)?.title

        holder.myView.setOnClickListener() {
            viewModel.book= BookData(books.value?.get(position)?.id!!,books.value?.get(position)?.autor!!,books.value?.get(position)?.title!!,books.value?.get(position)?.gatunek!!,books.value?.get(position)?.opinia!!,books.value?.get(position)?.status!!)
            holder.myView.findNavController().navigate(R.id.action_read_to_editBook)
        }

    }


    override fun getItemCount() = books.value?.count() ?: 0
}