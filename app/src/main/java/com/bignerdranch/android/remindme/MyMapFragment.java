package com.bignerdranch.android.remindme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.view.LayoutInflater;


import com.bignerdranch.android.remindme.AppActivity;
import com.bignerdranch.android.remindme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MyMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View view;
    private EditText textInputField;
    private Button searchButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.map_fragment_view, container, false);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        textInputField = (EditText) view.findViewById(R.id.input_field);
        searchButton = (Button) view.findViewById(R.id.search_button);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager() .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) view.findViewById(R.id.input_field);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(27.746974, 85.301582);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Kathmandu, Nepal"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}



//


//package com.bignerdranch.android.remindme;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.Place;
//
//import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//
//public class MyMapFragment extends Fragment implements OnMapReadyCallback {
//
//    GoogleMap mGoogleMap;
//    MapView mMapView;
//    View mView;
//
//    public MyMapFragment() {
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        mView = inflater.inflate(R.layout.map_fragment_view, container, false);
//
//
////        Fragment fragment = new SearchBarFragment();
////
////        FragmentManager fm = getFragmentManager();
////        FragmentTransaction transaction = fm.beginTransaction();
////        transaction.replace(R.id.search_fragment, fragment);
////        transaction.commit();
//
//        return mView;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mMapView = (MapView) mView.findViewById(R.id.map);
//        if (mMapView != null) {
//
//            mMapView.onCreate(null);
//            mMapView.onResume();
//            mMapView.getMapAsync(this);
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        MapsInitializer.initialize(getContext());
//        mGoogleMap = googleMap;
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//    }
//}
