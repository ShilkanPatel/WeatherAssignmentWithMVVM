package  com.test.myandroidassignment.viewmodels

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.test.myandroidassignment.common.ResultOf
import com.test.myandroidassignment.models.UserInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class FireBaseViewModel(private val dispatcher: CoroutineDispatcher) : ViewModel(),
    LifecycleObserver {
    private var auth: FirebaseAuth? = null
    private var storage: FirebaseStorage
    private var rootNode: FirebaseDatabase
    private var reference: DatabaseReference
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        rootNode = FirebaseDatabase.getInstance()
        reference = rootNode.getReference("userInfo")
        loading.postValue(false)

    }

    private val _registrationStatus = MutableLiveData<ResultOf<String>>()
    val registrationStatus: LiveData<ResultOf<String>> = _registrationStatus
    fun signUp(email: String, password: String) {
        loading.postValue(true)
        viewModelScope.launch(dispatcher) {
            val errorCode = -1
            try {
                auth?.let { authentication ->
                    authentication.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (!task.isSuccessful) {
                                println("Registration Failed with ${task.exception}")
                                _registrationStatus.postValue(ResultOf.Success("Registration Failed with ${task.exception}"))
                            } else {
                                _registrationStatus.postValue(ResultOf.Success("UserCreated"))

                            }
                            loading.postValue(false)
                        }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    private val _signInStatus = MutableLiveData<ResultOf<String>>()
    val signInStatus: LiveData<ResultOf<String>> = _signInStatus
    fun signIn(email: String, password: String) {
        loading.postValue(true)
        viewModelScope.launch(dispatcher) {
            val errorCode = -1
            try {
                auth?.let { login ->
                    login.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->

                            if (!task.isSuccessful) {
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(ResultOf.Success("Login Failed with ${task.exception}"))
                            } else {
                                _signInStatus.postValue(ResultOf.Success("Login Successful"))

                            }
                            loading.postValue(false)
                        }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    fun resetSignInLiveData() {
        _signInStatus.value = ResultOf.Success("Reset")
    }

    private val _signOutStatus = MutableLiveData<ResultOf<String>>()
    val signOutStatus: LiveData<ResultOf<String>> = _signOutStatus
    fun signOut() {
        loading.postValue(true)
        viewModelScope.launch(dispatcher) {
            var errorCode = -1
            try {
                auth?.let { authentation ->
                    authentation.signOut()
                    _signOutStatus.postValue(ResultOf.Success("Signout Successful"))
                    loading.postValue(false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _signOutStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _signOutStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    private val _saveResult = MutableLiveData<ResultOf<String>>()
    val saveResult: LiveData<ResultOf<String>> = _saveResult
    fun saveUserInfoDetails(userInfo: UserInfo) {
        loading.postValue(true)
        viewModelScope.launch(dispatcher) {
            val errorCode = -1
            try {
                val id: String? = reference.push().key


                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                        if (id != null) {
                            reference.child(id).setValue(userInfo)
                        }

                        _saveResult.postValue(ResultOf.Success("Data Saved Successfully"))
                        loading.postValue(false)
                    }

                    override fun onCancelled(@NonNull error: DatabaseError) {
                        _saveResult.postValue(ResultOf.Success("Data Save Failed"))
                        loading.postValue(false)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _saveResult.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _saveResult.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }
            }
        }
    }

    private val _taxInfoMutableLiveDataList = MutableLiveData<ResultOf<MutableList<UserInfo>>>()
    val taxInfoMutableLiveDataList: LiveData<ResultOf<MutableList<UserInfo>>> =
        _taxInfoMutableLiveDataList

    fun fetchEmpInfoDetails(email: String) {
        loading.postValue(true)
        val userInfoMutableList = mutableListOf<UserInfo>()
        viewModelScope.launch(dispatcher) {
            val errorCode = -1
            try {
                val checkUser: Query = reference.orderByChild("email").equalTo(email)

                checkUser.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in dataSnapshot.children) {
                            val userInfo = postSnapshot.getValue(UserInfo::class.java)
                            if (userInfo != null) {
                                userInfoMutableList.add(userInfo)
                            }
                        }
                        _taxInfoMutableLiveDataList.postValue(ResultOf.Success(userInfoMutableList))

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(
                            "FireBaseViewModel",
                            "loadPost:onCancelled",
                            databaseError.toException()
                        )
                        _taxInfoMutableLiveDataList.postValue(
                            ResultOf.Failure(
                                "Failed with Error Code ${errorCode} ",
                                databaseError.toException()
                            )
                        )
                        loading.postValue(false)
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _saveResult.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _saveResult.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }
            }

        }
    }

    fun fetchLoading(): LiveData<Boolean> = loading
}