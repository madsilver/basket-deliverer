package br.com.silver.dybapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.silver.dybapp.domain.Delivery;
import br.com.silver.dybapp.domain.DeliveryAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    ListView listView;
    DeliveryAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        //listView = new ListView(getContext());
        listView = view.findViewById(R.id.lvHistory);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Delivery> results = realm.where(Delivery.class)
                .sort("date", Sort.DESCENDING)
                .findAll();

        adapter = new DeliveryAdapter(getContext(), results);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

}
