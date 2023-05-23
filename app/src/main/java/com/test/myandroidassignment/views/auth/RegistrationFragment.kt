package  com.test.myandroidassignment.views.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.test.myandroidassignment.HomeActivity
import com.test.myandroidassignment.R
import com.test.myandroidassignment.common.ResultOf
import com.test.myandroidassignment.databinding.FragmentRegistrationBinding
import com.test.myandroidassignment.models.UserInfo
import com.test.myandroidassignment.repository.PrefRepository
import com.test.myandroidassignment.viewmodels.FireBaseViewModel

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var fireBaseViewModel: FireBaseViewModel
    private var prefRepository: PrefRepository? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireBaseViewModel = (activity as AuthenticationActivity).fetchFireBaseViewModel()
        prefRepository = PrefRepository(requireContext())
        FirebaseApp.initializeApp(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (isUserValid()) {
                    doRegistration()
                }
            }
        }
        observeRegistration()
    }

    private fun isUserValid(): Boolean {
        if (TextUtils.isEmpty(binding.edEmailAddress.text.toString())) {
            Toast.makeText(
                requireContext(), getString(R.string.enter_email_id), Toast.LENGTH_LONG
            ).show()
            return false
        } else if (TextUtils.isEmpty(
                binding.edPassword.text.toString()
            )
        ) {
            Toast.makeText(
                requireContext(), getString(R.string.please_enter_password), Toast.LENGTH_LONG
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edConfirmPassword.text.toString())) {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_enter_confirm_password),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edPassword.text.toString() != binding.edConfirmPassword.text.toString()) {
            Toast.makeText(
                requireContext(), getString(R.string.password_not_matched), Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }


    private fun doRegistration() {
        fireBaseViewModel.signUp(
            binding.edEmailAddress.text.toString(), binding.edPassword.text.toString()
        )
    }

    private fun observeRegistration() {
        fireBaseViewModel.registrationStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is ResultOf.Success -> {
                        if (it.value.equals("UserCreated", ignoreCase = true)) {
                            val userInfo = UserInfo(
                                binding.edEmailAddress.text.toString().trim(),
                                binding.edUserName.text.toString().trim(),
                                binding.edShortBio.text.toString().trim(),
                                it.value
                            )
                            prefRepository?.saveLoginData(userInfo)
                            fireBaseViewModel.saveUserInfoDetails(userInfo)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Registration failed with ${it.value}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    is ResultOf.Failure -> {
                        val failedMessage = it.message ?: "Unknown Error"
                        Toast.makeText(
                            requireContext(),
                            "Registration failed with $failedMessage",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        fireBaseViewModel.saveResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is ResultOf.Success -> {
                        if (it.value.equals(
                                getString(R.string.data_saved_sucess),
                                ignoreCase = true
                            )
                        ) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.user_created),
                                Toast.LENGTH_LONG
                            ).show()
                            prefRepository?.setLoggedIn(true)

                            navigateToMainScreen()
                        } else {
                            println(getString(R.string.data_fail_to_save))
                        }
                    }
                    is ResultOf.Failure -> {
                        val failedMessage = it.message ?: "Unknown Error"
                        Toast.makeText(
                            requireContext(), "Save failed $failedMessage", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        startActivity(Intent(context, HomeActivity::class.java))
    }

}