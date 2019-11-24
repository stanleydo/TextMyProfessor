package com.example.textmyprofessor

import MessageAdapter
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textmyprofessor.databinding.FragmentChatRoomBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat_room.*
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

        // Passing the room ID from the Create Room Fragment
        val args = ChatRoomFragmentArgs.fromBundle(arguments!!)
        val room_id = args.roomid

        // Populating the RecyclerView
        binding.chatBox.layoutManager = LinearLayoutManager(this.context)
        binding.chatBox.adapter = MessageAdapter(database.child("chat-rooms").child(room_id), binding.chatBox)

        binding.sendBtn.setOnClickListener{
            //The editText that the professor will use as input
            val text = binding.inputMsgText.text.toString()
            //Creates a new entry in the database in "chat-rooms" with name "Professor at *DATE*" and sets the value to the input
            val date = Date()
            val msg = Message(time = date.toString(), user = "Professor", text = text)

            val autoGenKey = database.child("chat-rooms").child(room_id).push()

            val key: String = autoGenKey.key.toString()

            database.child("chat-rooms").child(room_id).child(key).setValue(msg)
//            Log.d(TAG, "onChildAdded:" + DataSnapshot.getValue(Message::class.javaObjectType)!!)
        }

        return binding.root
    }

}
