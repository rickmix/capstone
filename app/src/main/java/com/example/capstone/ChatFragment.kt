package com.example.capstone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_contact_list.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ARG_PHONE_NUMBER = "arg_phone_number"
const val ARG_USERNAME_OTHER = "arg_username_other"

class ChatFragment : Fragment() {

    private val chats = arrayListOf<String>()
    private val chatAdapter = ChatAdapter(chats)
    private val viewModel: ChatViewModel by viewModels()
    lateinit var session: SessionManager
    var chatData: Chat = Chat(ArrayList(), ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = SessionManager(requireContext())
        getActivity()?.setTitle(arguments?.getString(ARG_USERNAME_OTHER))

        if(!session.isLoggedIn()) {
            findNavController().navigate(
                R.id.action_chatFragment_to_homeScreen
            )
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddMessage.setOnClickListener {
            addMessage()
        }

        initViews()
    }

    private fun initViews() {
        rvChatList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rvChatList.adapter = chatAdapter
        rvChatList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        setupChat()
//        createItemTouchHelper().attachToRecyclerView(rvContactList)
    }

    private fun setupChat() {
        var numbersArray: MutableList<String> = ArrayList()
        var doesChatExists = false

        val userPhoneNumber = arguments?.getString(ARG_PHONE_NUMBER)

        viewModel.chats.observe(viewLifecycleOwner, Observer { allChats ->
            for(i in allChats) {
                if(i.phones[0] == session.getUserPhone() && i.phones[1] == userPhoneNumber ||
                    i.phones[1] == session.getUserPhone() && i.phones[0] == userPhoneNumber){
                    //Start chat
                    chatData = Chat(i.phones, i.messages, i.id)
                    doesChatExists = true
                }
            }

            if(doesChatExists) {
                Toast.makeText(activity, "Starting chat", Toast.LENGTH_SHORT).show()
                var newList: ArrayList<String> = ArrayList()
                for (i in chatData.messages) {
                    newList.add(i)
                }

                this@ChatFragment.chats.clear()
                this@ChatFragment.chats.addAll(newList)
                chatAdapter.notifyDataSetChanged()

            } else {
                Toast.makeText(activity, "Creating new chat", Toast.LENGTH_SHORT).show()
                numbersArray.add(session.getUserPhone())
                if (userPhoneNumber != null) {
                    numbersArray.add(userPhoneNumber)
                }
                viewModel.newUser(ArrayList(),numbersArray)
            }
        })
    }

    private fun addMessage() {
        var message = inputChatMessage.text.toString()

        viewModel.insertMessage(message, chatData)

        viewModel.success.observe(viewLifecycleOwner, androidx.lifecycle.Observer { success ->
            Toast.makeText(activity,"Message send!",Toast.LENGTH_SHORT).show()
            view?.clearFocus()
            chatAdapter.notifyDataSetChanged()
        })
    }

}