package com.hjz.storyapp.maps

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.hjz.storyapp.R
import com.hjz.storyapp.data.model.UserModelFactory
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.databinding.ActivityMapsBinding
import com.hjz.storyapp.home.HomeActivity

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel by viewModels<MapsViewModel> {
        UserModelFactory.getInstance(this)
    }

    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSession()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getSession() {

        viewModel.getSession().observe(this) {user ->

            token = user.token

            getStoriesWithLocation(token)

            if (!user.isLogin){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun getStoriesWithLocation(token : String) {
        viewModel.getStoriesWithLocation(token)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.mapStory.observe(this){ data : List<ListStoryItem> ->
            data.forEach { dataMaps ->
                val latLng = LatLng(dataMaps.lat as Double, dataMaps.lon as Double)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(dataMaps.name)
                        .snippet(dataMaps.description)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}