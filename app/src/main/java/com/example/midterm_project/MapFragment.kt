package com.example.midterm_project

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.midterm_project.Domain.Location
import com.example.midterm_project.Helper.LocationPermissionHelper
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.lang.ref.WeakReference

class MapFragment : Fragment() {
    private val TAG = "TestingFragment";
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }
    private lateinit var mapView: MapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = root.findViewById(R.id.mapView);
        locationPermissionHelper = LocationPermissionHelper(WeakReference(activity))
        locationPermissionHelper.checkPermissions {
            getLocations();
        }

        return root;
    }

    private fun getLocations() {
        val database : DatabaseReference = Firebase.database.reference

        database.child("locations").get().addOnSuccessListener {
            val locations: ArrayList<Location> = ArrayList()

            for (snapShot in it.children) {
                val location = snapShot.getValue<Location>()
                locations.add(location!!)
            }

            onMapReady(locations)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun onMapReady(locations: ArrayList<Location>) {
        mapView.getMapboxMap().setCamera(
                CameraOptions.Builder()
                        .zoom(14.0)
                        .build()
        )

        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS,
        ) {
            addAnnotationToMap(locations)
            initLocationComponent()
            setupGesturesListener()
        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                    bearingImage = this@MapFragment.context?.let {
                        AppCompatResources.getDrawable(
                                it,
                                com.mapbox.maps.R.drawable.mapbox_user_puck_icon,
                        )
                    },
                    shadowImage = this@MapFragment.context?.let {
                        AppCompatResources.getDrawable(
                                it,
                                com.mapbox.maps.R.drawable.mapbox_user_icon_shadow,
                        )
                    },
                    scaleExpression = interpolate {
                        linear()
                        zoom()
                        stop {
                            literal(0.0)
                            literal(0.6)
                        }
                        stop {
                            literal(20.0)
                            literal(1.0)
                        }
                    }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
//        Toast.makeText(context, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
                .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
                .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.location
                .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
                .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun addAnnotationToMap(locations: ArrayList<Location>) {
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        context?.let {
            bitmapFromDrawableRes(
                    it,
                R.drawable.red_marker
            )?.let {
                val annotationApi = mapView?.annotations
                val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)

                val points: ArrayList<PointAnnotationOptions> = ArrayList();

                for (location in locations) {
                    // Set options for the resulting symbol layer.
                    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                            // Define a geographic coordinate.
                            .withPoint(Point.fromLngLat(location.longitude!!, location.latitude!!))
                            // Specify the bitmap you assigned to the point annotation
                            // The bitmap will be added to map style automatically.
                            .withIconImage(it)

                    points.add(pointAnnotationOptions);
                }

                // Add the resulting pointAnnotation to the map.
                pointAnnotationManager.create(points)
            }
        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
            convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth, drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}