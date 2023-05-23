package com.test.myandroidassignment.views.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.test.myandroidassignment.HomeActivity
import com.test.myandroidassignment.R
import com.test.myandroidassignment.common.ResultOf
import com.test.myandroidassignment.databinding.FragmentLoginBinding
import com.test.myandroidassignment.repository.PrefRepository
import com.test.myandroidassignment.viewmodels.FireBaseViewModel

class LoginFragment : Fragment(), LifecycleObserver {
    private lateinit var fireBaseViewModel: FireBaseViewModel
    private lateinit var binding: FragmentLoginBinding
    private var prefRepository: PrefRepository? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireBaseViewModel = (activity as AuthenticationActivity).fetchFireBaseViewModel()
        prefRepository = PrefRepository(requireContext())
        FirebaseApp.initializeApp(requireContext())
        observerLoadingProgress()
        binding.registrationBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.loginBtn.setOnClickListener {
            if (TextUtils.isEmpty(binding.loginEmail.text.toString()) || TextUtils.isEmpty(binding.loginPwd.text.toString())) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_field_can_emplty),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                signIn(binding.loginEmail.text.toString().trim(), binding.loginPwd.text.toString())
            }
        }
    }

    private fun signIn(email: String, pwd: String) {
        fireBaseViewModel.signIn(email, pwd)
        observeSignIn()
    }

    private fun observeSignIn() {
        fireBaseViewModel.signInStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is ResultOf.Success -> {
                        if (it.value.equals(getString(R.string.login_sucess), ignoreCase = true)) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.login_sucessful),
                                Toast.LENGTH_LONG
                            )
                                .show()
                            fireBaseViewModel.resetSignInLiveData()
                            prefRepository?.setLoggedIn(true)
                            navigateUserInfoFragment()
                        } else if (it.value.equals("Reset", ignoreCase = true)) {

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Login failed with ${it.value}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    is ResultOf.Failure -> {
                        val failedMessage = it.message ?: "Unknown Error"
                        Toast.makeText(
                            requireContext(),
                            "Login failed with $failedMessage",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun observerLoadingProgress() {
        fireBaseViewModel.fetchLoading().observe(viewLifecycleOwner) {
            if (!it) {
                println(it)
                binding.loginProgress.visibility = View.GONE
            } else {
                binding.loginProgress.visibility = View.VISIBLE
            }

        }
    }

    private fun navigateUserInfoFragment() {
        startActivity(Intent(context, HomeActivity::class.java))
    }

}