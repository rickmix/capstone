package com.example.capstone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home_screen.*
import kotlinx.android.synthetic.main.screen_login.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginScreen : Fragment() {

    private val users = arrayListOf<User>()
    private val viewModel: UserViewModel by viewModels()
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getActivity()?.setTitle("Rick Chat");
        session = SessionManager(requireContext())

//        viewModel.deleteAllContacts()

        if(session.isLoggedIn()) {
            findNavController().navigate(
                R.id.action_FirstFragment_to_homeScreen
            )
        }

        viewModel.users.observe(viewLifecycleOwner, Observer { games ->
            this@LoginScreen.users.clear()
            this@LoginScreen.users.addAll(games)
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            addUser()
        }

        observeGame()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_history -> {
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addUser() {
        val userPhone = inputPhone.text.toString()
        var alreadyExists = false;
        var userName: String = ""

        view?.clearFocus()

        if(users.isNotEmpty()) {
            for (i in users) {
                if (i.phone == userPhone) {
                    alreadyExists = true
                    userName = i.username
                }
            }

            if (alreadyExists) {
                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                //@TODO Change to username
                Toast.makeText(context, "Welcome $userName", Toast.LENGTH_SHORT).show()
                session.createLoginSession(userPhone, userName)
                findNavController().navigate(
                    R.id.action_FirstFragment_to_homeScreen
                )
            } else {
                Toast.makeText(context, "User didn't exists", Toast.LENGTH_SHORT).show()
                session.createLoginSession(userPhone, userPhone)
                viewModel.newUser(userPhone, userPhone, "test")

            }
        } else {
            Toast.makeText(
                context,
                "User didn't exists and no users where found",
                Toast.LENGTH_SHORT
            ).show()
            session.createLoginSession(userPhone, userPhone)
            viewModel.newUser(userPhone, userPhone, "test")

        }
    }

    private fun observeGame() {
        viewModel.success.observe(viewLifecycleOwner, androidx.lifecycle.Observer { success ->
            findNavController().navigate(
                R.id.action_FirstFragment_to_setUserDetails
            )
        })
    }


//    meike houct van rickie
}