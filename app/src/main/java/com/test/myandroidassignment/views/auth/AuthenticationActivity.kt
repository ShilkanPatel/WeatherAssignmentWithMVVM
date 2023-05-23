package  com.test.myandroidassignment.views.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.test.myandroidassignment.HomeActivity
import com.test.myandroidassignment.R
import com.test.myandroidassignment.databinding.ActivityAuthenticationBinding
import com.test.myandroidassignment.repository.PrefRepository
import com.test.myandroidassignment.viewmodels.FireBaseViewModel
import com.test.myandroidassignment.viewmodels.FireBaseViewModelFactory

class AuthenticationActivity : AppCompatActivity(), LocationListener {
    private var binding: ActivityAuthenticationBinding? = null
    private lateinit var fireBaseViewModel: FireBaseViewModel
    internal fun fetchFireBaseViewModel() = fireBaseViewModel
    private var prefRepository: PrefRepository? = null
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        FirebaseApp.initializeApp(this)
        initViewModel()
    }

    private fun initViewModel() {
        val fireBaseViewModelFactory = FireBaseViewModelFactory()
        fireBaseViewModel =
            ViewModelProvider(this, fireBaseViewModelFactory).get(FireBaseViewModel::class.java)
        getLocation()
        prefRepository = PrefRepository(this)
        if (prefRepository!!.getLoggedIn()) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            val navHostFragment =
                (supportFragmentManager.findFragmentById(R.id.nav_host_firebase_fragment) as NavHostFragment)
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.firebase_nav_graph)
            navHostFragment.navController.graph = graph
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        Log.e(
            "lat long : ",
            "Latitude: " + location.latitude + " , Longitude: " + location.longitude
        )
        prefRepository!!.saveLocationData(location.latitude,location.longitude)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}