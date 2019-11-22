package com.example.textmyprofessor

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.textmyprofessor.databinding.FragmentChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChatRoomFragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentChatRoomBinding>(inflater,
            R.layout.fragment_chat_room,container,false)

        database = FirebaseDatabase.getInstance().reference

        val args = ChatRoomFragmentArgs.fromBundle(arguments!!)
        val room_id = args.roomid

        binding.sendBtn.setOnClickListener{
            val text = binding.inputMsgText.text.toString()
            database.child("chat-rooms").child(room_id).child("Professor at " + Date()).setValue(text)
        }

        return binding.root
    }

}
