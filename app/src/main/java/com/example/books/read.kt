package com.example.books

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.books.dataBase.BookData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [read.newInstance] factory method to
 * create an instance of this fragment.
 */
class read : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read, container, false)
    }

    private lateinit var viewModel: BookViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory=BookViewModelFactory((requireNotNull(this.activity).application))
        viewModel=ViewModelProvider(requireActivity(),factory).get(BookViewModel::class.java)

        val LibraryBooks = MutableLiveData<List<BookData>>()
        val LibraryList = ArrayList<BookData>()
        LibraryBooks.value = LibraryList

        var layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var adapter = BooksAdapter(viewModel, LibraryBooks, requireContext())
        LibraryBooks.observe(viewLifecycleOwner, {adapter.notifyDataSetChanged()})

        view.findViewById<RecyclerView>(R.id.recyclerView).let{
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        val database=Firebase.database("https://books-c0eec-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref=database.getReference("")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()){
                    LibraryList.clear()
                    for(book in snapshot.children){
                        val bookItem=book.getValue(BookData::class.java)
                        LibraryList.add(bookItem!!)
                    }
                    LibraryBooks.value=LibraryList
                }
            }
        })

        view.findViewById<TextInputEditText>(R.id.booksearch).doOnTextChanged { text, start, before, count ->
            var search = view.findViewById<TextInputEditText>(R.id.booksearch).text.toString()

            ref.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot!!.exists()){
                        LibraryList.clear()
                        for(game in snapshot.children) {
                            val Item = game.getValue(BookData::class.java)
                            if(Item?.title.toString().contains(search)) {
                                LibraryList.add(Item!!)
                            }
                        }
                        LibraryBooks.value = LibraryList
                    }
                }
            })
        }


        view.findViewById<Button>(R.id.addbook).apply{
            setOnClickListener{
                view.findNavController().navigate(R.id.action_read_to_addbook2)
            }
        }

        view.findViewById<Button>(R.id.logout).apply {
            setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(getActivity(), MainActivity::class.java)
                getActivity()?.startActivity(intent)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment read.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            read().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}