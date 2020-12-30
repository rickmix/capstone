package com.example.capstone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home_screen.*

class HomeScreen : Fragment() {

    private val users = arrayListOf<User>()
    private val userAdapter = UserAdapter(users)
    private val viewModel: UserViewModel by viewModels()
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        session = SessionManager(requireContext())
        getActivity()?.setTitle(session.getUserName());
        if(!session.isLoggedIn()) {
            findNavController().navigate(
                R.id.action_homeScreen_to_FirstFragment
            )
        }

        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNewContact.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeScreen_to_contactList
            )
        }
        initViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_history -> {
                Toast.makeText(context, "Removed all contacts", Toast.LENGTH_SHORT).show()
                viewModel.users.observe(viewLifecycleOwner, Observer { allUsers ->
                    for(i in allUsers) {
                        if(i.phone == session.getUserPhone()) {
                            viewModel.deleteAllContacts(i)
                            break
                        }
                    }
                })
                true
            }
            R.id.logout -> {
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                session.logOutUser()
                findNavController().navigate(
                    R.id.action_homeScreen_to_FirstFragment
                )
                true
            }
            R.id.settings -> {
                findNavController().navigate(
                    R.id.action_homeScreen_to_setUserDetails
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        rvHomePage.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvHomePage.adapter = userAdapter
        rvHomePage.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        userAdapter.onItemClick = { contact ->
            val args = Bundle()
            args.putString(ARG_PHONE_NUMBER, contact.phone)
            args.putString(ARG_USERNAME_OTHER, contact.username)

            //setFragmentResult(REQ_REMINDER_KEY, bundleOf(Pair(BUNDLE_REMINDER_KEY, contact.phone)))

            findNavController().navigate(
                R.id.action_homeScreen_to_chatFragment,
                args
            )
        }

        createItemTouchHelper().attachToRecyclerView(rvHomePage)
//        session.logOutUser()
        observeAddUser()
    }

    private fun observeAddUser() {
        var ownNumber = session.getUserPhone()

        viewModel.getUserData(ownNumber).observe(viewLifecycleOwner, Observer { userData ->
            viewModel.users.observe(viewLifecycleOwner, Observer { allUsers ->
                this@HomeScreen.users.clear()
                val list: MutableList<User> = ArrayList()
                var userList: List<User> = allUsers

                userData
                //i = user 123 WITH Friends 7, 8
//                if(userData.friends) {
                    for (i in userData.friends) {
                        //User = The user to compare with
                        for (user in userList) {
                            if (i == user.id.toString()) {
                                list.add(user)
                            }
                        }
                    }
//                }

                this@HomeScreen.users.addAll(list)
                userAdapter.notifyDataSetChanged()
            })
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
                val userToDelete = users[position]
                viewModel.users.observe(viewLifecycleOwner, Observer { allUsers ->
                    for(i in allUsers) {
                        if(i.phone == session.getUserPhone()) {
                            viewModel.deleteUser(userToDelete, i)
                            break
                        }
                    }
                })
            }
        }
        return ItemTouchHelper(callback)
    }

}