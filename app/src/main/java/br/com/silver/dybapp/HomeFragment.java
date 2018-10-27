package br.com.silver.dybapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txusballesteros.widgets.FitChart;

import br.com.silver.dybapp.domain.Counter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.delivery_count)
    TextView tvCount;

    @BindView(R.id.fitChart)
    FitChart fitChart;

    public HomeFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String scheduled = prefs.getString(getString(R.string.pref_scheduled), "100");

        fitChart.setMinValue(0f);
        fitChart.setMaxValue(Float.valueOf(scheduled));

        setCounterDisplay();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnReset)
    public void resetCount() {

        tvCount.setText("0");
        fitChart.setValue(0f);

        Realm realm = Realm.getDefaultInstance();
        Counter counter = realm.where(Counter.class).equalTo("id", 1).findFirst();

        if(counter == null) {
            return;
        }

        try {
            realm.beginTransaction();
            counter.setValue(0);
            realm.copyToRealm(counter);
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public void setCounterDisplay() {
        Realm realm = Realm.getDefaultInstance();
        Counter counter = realm.where(Counter.class).equalTo("id", 1).findFirst();

        tvCount.setText("0");
        float f = 0f;

        if(counter != null) {
            tvCount.setText(counter.toString());
            f = (float) counter.getValue();
        }

        fitChart.setValue(f);
    }

}
