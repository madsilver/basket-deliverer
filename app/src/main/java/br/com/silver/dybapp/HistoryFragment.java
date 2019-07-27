package br.com.silver.dybapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import br.com.silver.dybapp.domain.Delivery;
import br.com.silver.dybapp.domain.DeliveryAdapter;
import br.com.silver.dybapp.utils.WebClient;
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
        makeReset();
    }

    @OnClick({R.id.btnResend})
    public void resend(View v) {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<Delivery> results = realm.where(Delivery.class)
                    .equalTo("sync", Delivery.NOT_SYNCED)
                    .findAll();

            if(results.size() == 0) {
                showMessage("Nenhum envio pendente");
                return;
            }

            ArrayList<String> list = new ArrayList<>();

            for(final Delivery delivery: results) {
                list.add(delivery.toString());
            }

            WebClient client = new WebClient(getContext());
            String resp = client.postAll(list);

            if(resp.equals("")) {
                for(final Delivery delivery: results) {
                    realm.beginTransaction();
                    delivery.setSync(Delivery.SYNCED);
                    realm.copyToRealm(delivery);
                    realm.commitTransaction();
                }

                resp = getResources().getString(R.string.message_success_resend);
            }

            showMessage(resp);
        }
        catch(Exception e) {
            showMessage("Failure: " + e.getMessage());
        } finally {
            realm.close();
        }

        showList();
    }

    public void makeReset() {
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

    public void showMessage(CharSequence text ) {
        Context context = getContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
