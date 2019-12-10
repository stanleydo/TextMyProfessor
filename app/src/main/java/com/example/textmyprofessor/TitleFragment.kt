package com.example.textmyprofessor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.NavigationUI
import com.example.textmyprofessor.databinding.FragmentTitleBinding
import kotlin.random.Random
import android.view.MotionEvent
import android.view.View.OnTouchListener



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TitleFragment : Fragment() {
    private val noReplaceList = mutableListOf<Int>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title,container,false)

        // Changed onclicklistener to on touch (feels more responsive)
        // and added glow effect
        binding.startBtn.setOnTouchListener { v, event ->
            val action = event.action
            when(action){
                MotionEvent.ACTION_DOWN -> {
                    binding.startBtn.setImageResource(R.mipmap.startbtnglow_foreground)
                }

                MotionEvent.ACTION_UP -> {
                    v.findNavController().navigate(R.id.action_titleFragment_to_createRoomFragment)
                }

                MotionEvent.ACTION_MOVE -> {}

                else -> {

                    binding.startBtn.setImageResource(R.mipmap.startbutton_foreground)
                }


            }
            true
        }


//        binding.startBtn.setOnClickListener{view: View ->
//                view.findNavController().navigate(R.id.action_titleFragment_to_createRoomFragment)
//        }

        return binding.root
    }

}
