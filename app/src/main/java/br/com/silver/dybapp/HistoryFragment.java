package br.com.silver.dybapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.silver.dybapp.domain.Delivery;
import br.com.silver.dybapp.domain.DeliveryAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    @BindView(R.id.lvHistory)
    ListView listView;

    DeliveryAdapter adapter;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        showList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Delivery> results = realm.where(Delivery.class)
                .sort("date", Sort.DESCENDING)
                .findAll();

        adapter = new DeliveryAdapter(getContext(), results);
        listView.setAdapter(adapter);
    }

    @OnClick({R.id.btnResetHistory})
    public void resetHistory(View v) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();

            RealmResults<Delivery> results = realm.where(Delivery.class)
                .equalTo("sync", Delivery.SYNCED)
                .findAll();

            if(results.size() > 0) {
                results.deleteAllFromRealm();
                showList();
            }

            realm.commitTransaction();
        }
        catch(Exception e) {
            Log.d("ERROR", e.getMessage());
        } finally {
            realm.close();
        }
    }

}
