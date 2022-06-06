package com.example.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.books.dataBase.BookData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditBook.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditBook : Fragment() {
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

    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_book, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory=BookViewModelFactory((requireNotNull(this.activity).application))
        viewModel=ViewModelProvider(requireActivity(),factory).get(BookViewModel::class.java)


        view.findViewById<TextInputEditText>(R.id.autorET).setText(viewModel.book.autor)
        view.findViewById<TextInputEditText>(R.id.tytulET).setText(viewModel.book.title)
        view.findViewById<TextInputEditText>(R.id.gatunekET).setText(viewModel.book.gatunek)
        view.findViewById<TextInputEditText>(R.id.opiniaET).setText(viewModel.book.opinia)

        if (viewModel.book.status==true){
            view.findViewById<CheckBox>(R.id.status).isChecked
        }


        val database=Firebase.database("https://books-c0eec-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref=database.getReference("")


        view.findViewById<Button>(R.id.usun).apply {
            setOnClickListener {
                ref.child(viewModel.book.id.toString()).removeValue()
                view.findNavController().navigate(R.id.action_editBook_to_read)
            }
        }

        view.findViewById<Button>(R.id.edytuj).apply {
            setOnClickListener {
                ref.child(viewModel.book.id.toString()).removeValue()

                var status=false

                if(view.findViewById<CheckBox>(R.id.status).isChecked()){
                    status=true
                }
                var booktoadd=BookData(viewModel.book.id.toString(),view.findViewById<EditText>(R.id.autorET).text.toString(),view.findViewById<EditText>(
                    R.id.tytulET).text.toString(),view.findViewById<EditText>(R.id.gatunekET).text.toString(),view.findViewById<EditText>(R.id.opiniaET).text.toString(),status)

                ref.child(viewModel.book.id.toString()!!).setValue(booktoadd)

                view.findNavController().navigate(R.id.action_editBook_to_read)
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
         * @return A new instance of fragment EditBook.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditBook().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}