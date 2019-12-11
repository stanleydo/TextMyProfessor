package com.example.textmyprofessor

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.textmyprofessor.databinding.FragmentCreateRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateRoomFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var password: String = ""

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

        // ** CREATE ROOM BUTTON **
        binding.createRoomBtn.setOnClickListener{
                view: View ->
            val postListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val inputText = binding.roomID.text.toString()
                    val hasPassword = dataSnapshot.child("chat-rooms").child(inputText).child("password").value != ""

                    if (inputText == "") {
                        emptyTextToast()
                    }
                    else {
                        // Get Post object and use the values to update the UI
                        if (dataSnapshot.child("chat-rooms").hasChild(inputText)) {
//                            roomTakenToast()

                            // Alert Dialogue box
                            val builder = AlertDialog.Builder(context)

                            // Display a message on alert dialog
                            builder.setMessage("This room name already exists. Do you want to make a new one?")

                            // Set a positive button and its click listener on alert dialog (Create New Room)
                            builder.setPositiveButton("New Room") { dialog, which ->

                                if (hasPassword) {
                                    val joinPass = dataSnapshot.child("chat-rooms").child(inputText).child("password").value
                                    val passBuilder = AlertDialog.Builder(context)
                                    passBuilder.setMessage("This room is password secured. Please enter the password.")

                                    val input = EditText(context)
                                    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT)
                                    input.setLayoutParams(lp)
                                    passBuilder.setView(input)

                                    passBuilder.setPositiveButton("Create") { _, _ ->
                                        val input_pw = input.text.toString()
                                        if (input_pw == joinPass) {
                                            roomCreatedToast()
                                            database.child("chat-rooms").child(inputText).child("messages").setValue("")
                                            database.child("chat-rooms").child(inputText).child("password").setValue(password)
                                            view.findNavController().navigate(
                                                CreateRoomFragmentDirections.actionCreateRoomFragmentToChatRoomFragment(
                                                    inputText
                                                )
                                            )
                                        } else {
                                            passwordInvalidToast()
                                        }
                                    }

                                    passBuilder.setNeutralButton("Cancel") { _, _ ->
                                    }

                                    val passDialog: AlertDialog = passBuilder.create()
                                    passDialog.show()
                                } else {

                                    roomCreatedToast()

                                    // Create new room
                                    database.child("chat-rooms").child(inputText).child("messages")
                                        .setValue("")
                                    if (password != "") {
                                        database.child("chat-rooms").child(inputText).child("password").setValue(password)
                                    }
                                    // Change view
                                    view.findNavController().navigate(
                                        CreateRoomFragmentDirections.actionCreateRoomFragmentToChatRoomFragment(
                                            inputText
                                        )
                                    )
                                }
                                // Remove the database EventListener
                                database.removeEventListener(this)
                            }

                            // Display a negative button on alert dialog (Join Existing Room)
                            builder.setNegativeButton("Join Existing Room") { dialog, which ->

                                if (hasPassword) {
                                    val joinPass = dataSnapshot.child("chat-rooms").child(inputText).child("password").value
                                    val passBuilder2 = AlertDialog.Builder(context)
                                    passBuilder2.setMessage("This room is password secured. Please enter the password.")

                                    val input = EditText(context)
                                    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT)
                                    input.setLayoutParams(lp)
                                    passBuilder2.setView(input)

                                    passBuilder2.setPositiveButton("Create") { _, _ ->
                                        val input_pw = input.text.toString()
                                        if (input_pw == joinPass) {
                                            roomJoinedToast()
                                            view.findNavController().navigate(
                                                CreateRoomFragmentDirections.actionCreateRoomFragmentToChatRoomFragment(
                                                    inputText
                                                )
                                            )
                                        } else {
                                            passwordInvalidToast()
                                        }
                                    }

                                    passBuilder2.setNeutralButton("Cancel") { _, _ ->
                                    }

                                    val passDialog2: AlertDialog = passBuilder2.create()
                                    passDialog2.show()

                                } else {
                                    roomJoinedToast()
                                    // Join room and change view
                                    view.findNavController().navigate(
                                        CreateRoomFragmentDirections.actionCreateRoomFragmentToChatRoomFragment(
                                            inputText
                                        )
                                    )
                                }
                                // Remove the database EventListener
                                database.removeEventListener(this)
                            }

                            // Display a neutral button on alert dialog (Cancel)
                            builder.setNeutralButton("Cancel") { _, _ ->
                            }

                            // Finally, make the alert dialog using builder
                            val dialog: AlertDialog = builder.create()

                            // Display the alert dialog on app interface
                            dialog.show()
                        } else {
                            // Creates a new room
                            database.child("chat-rooms").child(inputText).child("messages").setValue("")
                            database.child("chat-rooms").child(inputText).child("password").setValue(password)
                            //Changed view
                            view.findNavController().navigate(
                                CreateRoomFragmentDirections.actionCreateRoomFragmentToChatRoomFragment(
                                    inputText
                                )
                            )
                            roomCreatedToast()
                            // Remove the database EventListener
                            database.removeEventListener(this)
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            // Add database Event Listener
            database.addValueEventListener(postListener)
        }

        //** SETTINGS/PREFERENCES BUTTON
        binding.settingsBtn.setOnClickListener{
            // passwordLock is just a imageview that is visible/invisible based off the password.
            // If a password has been set, the passwordLock becomes visible. Invisible if it's nothing.
            openSettings(binding.passwordLock)
        }

        binding.passwordLock.setOnClickListener{
            if (passwordValid(password)) {
                displayPasswordToast()
            } else {
                passwordInvalidToast()
            }
        }

        password = ""
        return binding.root
    }

    // Toasts for easy implementation
    fun roomTakenToast(){
        Toast.makeText(this.context,"Room is Taken", Toast. LENGTH_SHORT).show()
    }

    fun roomCreatedToast(){
        Toast.makeText(this.context,"Room Created", Toast. LENGTH_SHORT).show()
    }

    fun roomJoinedToast(){
        Toast.makeText(this.context,"Room Joined", Toast. LENGTH_SHORT).show()
    }

    fun emptyTextToast(){
        Toast.makeText(this.context,"Please Enter a Room Name", Toast. LENGTH_SHORT).show()
    }

    fun passwordInvalidToast() {
        Toast.makeText(this.context,"Password is Invalid. Please Try Again.", Toast. LENGTH_SHORT).show()
    }

    fun passwordSetToast() {
        Toast.makeText(this.context,"Password Set!", Toast. LENGTH_SHORT).show()
    }

    fun noPasswordSetToast() {
        Toast.makeText(this.context,"No Password Set.", Toast. LENGTH_SHORT).show()
    }

    fun displayPasswordToast() {
        Toast.makeText(this.context, "Password: $password", Toast.LENGTH_LONG).show()
    }

    fun openSettings(passwordLock: ImageView) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Select Your Preference")

        builder.setMessage("Enter a Password")

        val input = EditText(context)
        val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        input.setLayoutParams(lp)
        builder.setView(input)

        builder.setPositiveButton("Set Password") { dialog, which ->
            val input_pw = input.text.toString()

            if (passwordValid(input_pw)) {
                password = input_pw
                passwordSetToast()
                passwordLock.visibility = View.VISIBLE
            } else {
                passwordInvalidToast()
                passwordLock.visibility = View.INVISIBLE
            }
        }

        builder.setNeutralButton("Cancel") { _, _ ->
        }

//        builder.set

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun passwordValid(password: String) : Boolean {
        if (password == "") {
            return false
        } else {
            return true
        }
    }
}
