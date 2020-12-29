package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_contact.*
import kotlinx.android.synthetic.main.fragment_contact_list.*
import kotlinx.android.synthetic.main.fragment_home_screen.*

class ContactList : Fragment() {

    private val users = arrayListOf<User>()
    private val contactListAdapter = ContactListAdapter(users)
    private val viewModel: UserViewModel by viewModels()
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        getActivity()?.setTitle("Add contact");
        session = SessionManager(requireContext())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        rvContactList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rvContactList.adapter = contactListAdapter
        rvContactList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        createItemTouchHelper().attachToRecyclerView(rvContactList)
        observeAddUser()
    }

    private fun observeAddUser() {
        viewModel.users.observe(viewLifecycleOwner, Observer { userData ->
            this@ContactList.users.clear()

            var newList: ArrayList<User> = ArrayList()

            for(i in userData) {
                if(i.phone != session.getUserPhone()) {
                    newList.add(i)
                }
            }

            this@ContactList.users.addAll(newList)
            contactListAdapter.notifyDataSetChanged()
        })
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val userToAdd = users[position].id.toString()

                viewModel.getUserData(session.getUserPhone()).observe(viewLifecycleOwner, Observer { userData ->
                    viewModel.addFriend(userToAdd, userData)
                })

                viewModel.error.observe(viewLifecycleOwner, Observer {
                        message -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                })

                viewModel.successMessage.observe(viewLifecycleOwner, Observer {
                        message -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                })
            }
        }
        return ItemTouchHelper(callback)
    }


}