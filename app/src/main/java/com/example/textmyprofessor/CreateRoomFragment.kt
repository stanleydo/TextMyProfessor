package com.example.textmyprofessor

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.textmyprofessor.databinding.FragmentCreateRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_room.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateRoomFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateRoomBinding>(inflater,
            R.layout.fragment_create_room,container,false)
        // Inflate the layout for this fragment

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        auth.signInAnonymously()

        binding.createRoomBtn.setOnClickListener{
                view: View -> view.findNavController().navigate(CreateRoomFragmentDirections.actionCreateRoomFragmentToChatRoomFragment(binding.roomID.text.toString()))
            database.child("chat-rooms").child(binding.roomID.text.toString()).setValue("")
        }


//        return inflater.inflate(R.layout.fragment_create_room, container, false)
        return binding.root
    }

}
