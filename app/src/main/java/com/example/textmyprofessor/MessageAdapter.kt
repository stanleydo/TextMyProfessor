import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textmyprofessor.Message
import com.example.textmyprofessor.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_create_room.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.w3c.dom.Comment
import android.graphics.Color

//import com.example.textmyprofessor.Message

class MessageAdapter(private val database: DatabaseReference, private val recyclerView: RecyclerView): RecyclerView.Adapter<CustomViewHolder>() {

    var messagesList: MutableList<Message?> = mutableListOf()
    val professorTextColor: String = "#C04161"
    val studentTextColor: String = "#122960"

    init {
        // Create child event listener
        // [START child_event_listener_recycler]
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot)

//                Log.d(TAG, "MAP: " + map)

                // A new comment has been added, add it to the displayed list
                val msg: Message? = dataSnapshot.getValue(Message::class.java)
                Log.d(TAG, "USER: " + msg?.user)
                Log.d(TAG, "TEXT: " + msg?.text)
                Log.d(TAG, "TIME: " + msg?.time)
                messagesList.add(msg)
                notifyDataSetChanged()
                recyclerView.scrollToPosition(messagesList.size-1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
            }
        }
        database.addChildEventListener(childEventListener)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val msg = messagesList.get(position)

        if (msg?.user == "Professor") {
            holder.view.user_item.setTextColor(Color.parseColor(professorTextColor))
            holder.view.text_item.setTextColor(Color.parseColor(professorTextColor))
            holder.view.timestamp_item.setTextColor(Color.parseColor(professorTextColor))
        } else {
            holder.view.user_item.setTextColor(Color.parseColor(studentTextColor))
            holder.view.text_item.setTextColor(Color.parseColor(studentTextColor))
            holder.view.timestamp_item.setTextColor(Color.parseColor(studentTextColor))
        }

        holder.view.text_item.text = msg?.text
        holder.view.user_item.text = msg?.user
        holder.view.timestamp_item.text = msg?.time
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

//    fun bindMessage(message: Message?) {
//        with(message!!) {
//            item
//        }
//    }
}