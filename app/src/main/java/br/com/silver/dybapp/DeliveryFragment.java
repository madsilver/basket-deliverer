package br.com.silver.dybapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.IntDef;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.silver.dybapp.domain.Counter;
import br.com.silver.dybapp.domain.Delivery;
import br.com.silver.dybapp.utils.WebClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment {

    @BindView(R.id.cardDelivered)
    CardView cardDelivered;

    @BindView(R.id.cardNotDelivered)
    CardView cardNotDelivered;

    @BindView(R.id.rgDelivered)
    RadioGroup rgDelivered;

    @BindView(R.id.rgNotDelivered)
    RadioGroup rgNotDelivered;

    private Double lat, lng;
    private String imei;
    private boolean statusDelivery;
    private static final int REQUEST_CODE = 101;

    public DeliveryFragment() {
        this.lat = 0.0;
        this.lng = 0.0;
        this.imei = "";
    }

    public static DeliveryFragment newInstance(String code) {
        DeliveryFragment fragment = new DeliveryFragment();

        Bundle args = new Bundle();
        args.putString("code", code);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        cardDelivered.setVisibility(View.INVISIBLE);
        cardNotDelivered.setVisibility(View.INVISIBLE);

        getImei();
        getGPS();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        ButterKnife.bind(this, view);

        setStrictMode();

        return view;
    }

    private void setStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void getGPS() {

        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } catch(SecurityException ex){
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getImei() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            showMessage("Não foi possível obter o IMEI");
            return;
        }

        this.imei = tm.getDeviceId();
    }

    @OnClick({R.id.btnSuccess})
    public void showCardDelivered(View v) {
        statusDelivery = true;
        cardDelivered.setVisibility(View.VISIBLE);
        cardNotDelivered.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.btnFail})
    public void showCardOccurrence(View v) {
        statusDelivery = false;
        cardDelivered.setVisibility(View.INVISIBLE);
        cardNotDelivered.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btnDeliveredOk, R.id.btnNotDeliveredOk})
    public void setDelivery(View v) {
        String code = getArguments().getString("code", null);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Delivery delivery = new Delivery();
        delivery.setImei(this.imei);
        delivery.setCode(code);
        delivery.setLat(String.valueOf(this.lat));
        delivery.setLng(String.valueOf(this.lng));
        delivery.setStatus(getStatusDelivery(v.getId()));
        delivery.setStatusDetail(getStatusDetail());
        delivery.setDate(sdf.format(currentTime));

        counter();

        sendServer(delivery);
    }

    public void counter() {
        Realm realm = Realm.getDefaultInstance();
        Counter counter = realm.where(Counter.class).equalTo("id", 1).findFirst();

        try {
            realm.beginTransaction();

            if(counter == null) {
                counter = realm.createObject(Counter.class);
                counter.setId(1);
            }

            counter.increment();
            realm.copyToRealm(counter);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    @OnClick({R.id.btnDeliveredCancel, R.id.btnNotDeliveredCancel})
    public void cancel() {
        Fragment fragment = ScannerFragment.newInstance();
        openFragment(fragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showMessage(CharSequence text) {
        Context context = getContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public int getStatusDelivery(int id) {
        int status = id == R.id.btnDeliveredOk ?
                Delivery.DELEVIRED :
                Delivery.OCCURRENCE;
        return status;
    }

    public int getStatusDetail() {
        RadioButton radioButton;

        if(statusDelivery) {
            radioButton = getActivity().findViewById(rgDelivered.getCheckedRadioButtonId());
        } else {
            radioButton = getActivity().findViewById(rgNotDelivered.getCheckedRadioButtonId());
        }

        return Integer.parseInt(radioButton.getTag().toString());
    }

    public void sendServer(Delivery delivery) {
        WebClient client = new WebClient(getContext());
        String resp = client.post(delivery);

        if(resp.equals("")) {
            delivery.setSync(Delivery.SYNCED);
            resp = getResources().getString(R.string.message_success);
        }

        showMessage(resp);

        saveDelivery(delivery);

        redirectHistoryFragment();
    }

    public void saveDelivery(Delivery delivery) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm(delivery);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public void redirectHistoryFragment() {
        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_history);

        Fragment fragment = HistoryFragment.newInstance();
        openFragment(fragment);
    }
}
