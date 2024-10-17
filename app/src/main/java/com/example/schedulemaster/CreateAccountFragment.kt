package com.example.schedulemaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccountFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var mEditIDText: EditText
    private lateinit var mEditTextText: EditText
    private lateinit var mCreateButton: Button
    private lateinit var mDeleteButton: Button
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //currently no params
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_create_account, container, false) // view
        val tag = "OnCreateView"
        Log.i(tag, "-----------------------NEW LOG---------------------")
        Log.i(tag, "onCreateView fragment started.")

        // references to the ui elements specified in XML
        mEditIDText = v.findViewById(R.id.idEditText)
        mEditTextText = v.findViewById(R.id.textEditText)
        mCreateButton = v.findViewById(R.id.createButton)
        mCreateButton.setOnClickListener(this)
        mDeleteButton = v.findViewById(R.id.deleteButton)
        mDeleteButton.setOnClickListener(this)
        return v;
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.createButton -> {
                Log.d("INSIDE CreateAccount.kt", "creating account")
                Toast.makeText(requireContext(), "create account button clicked", Toast.LENGTH_SHORT).show()
                val id = mEditIDText.text.toString()
                val text = mEditTextText.text.toString()

                databaseRef = FirebaseDatabase.getInstance().getReference("Test Data")
                val data = testData(id,text)
                databaseRef.child(id).setValue(data)

                mEditIDText.text.clear()
                mEditTextText.text.clear()
                Toast.makeText(requireContext(), "Data saved!", Toast.LENGTH_SHORT).show()
            }

            R.id.deleteButton -> {
                Log.d("INSIDE CreateAccount.kt", "creating account")
                Toast.makeText(requireContext(), "create account button clicked", Toast.LENGTH_SHORT).show()
                val id = mEditIDText.text.toString()
                val text = mEditTextText.text.toString()

                databaseRef = FirebaseDatabase.getInstance().getReference("Test Data")
                databaseRef.child(id).removeValue()

                mEditIDText.text.clear()
                mEditTextText.text.clear()
                Toast.makeText(requireContext(), "Data Deleted!", Toast.LENGTH_SHORT).show()
            }
            else -> Log.e("INSIDE LoginFragment.kit", "Error: Invalid button press")
        }
    }
}