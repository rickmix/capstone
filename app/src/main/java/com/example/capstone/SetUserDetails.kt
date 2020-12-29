package com.example.capstone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_set_user_details.*
import kotlinx.android.synthetic.main.screen_login.*

class SetUserDetails : Fragment() {

    lateinit var session: SessionManager
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getActivity()?.setTitle("Settings");
        session = SessionManager(requireContext())

//        if(session.isLoggedIn()) {
//            findNavController().navigate(
//                R.id.action_setUserDetails_to_homeScreen
//            )
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnContinue.setOnClickListener {
            editUserName()
        }

    }

    fun editUserName() {
        val userName = inputUsername.text.toString()
        view?.clearFocus();

        session.editUserDetails(userName)
        viewModel.editUserDetails(userName, session.getUserPhone())

        viewModel.success.observe(viewLifecycleOwner, androidx.lifecycle.Observer { success ->
            Toast.makeText(context, "Username changed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.action_setUserDetails_to_homeScreen
            )
        })

        viewModel.error.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                message -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })
    }

}