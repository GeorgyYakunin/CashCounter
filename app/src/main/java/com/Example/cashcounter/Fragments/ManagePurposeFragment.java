package com.Example.cashcounter.Fragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.Example.cashcounter.Adapters_Items.PurposeAdapter;
import com.Example.cashcounter.Adapters_Items.Purpose_Items;
import com.Example.cashcounter.Class.CToast;
import com.Example.cashcounter.Class.Database;
import com.Example.cashcounter.Class.Helper;
import com.Example.cashcounter.R;
import java.util.ArrayList;

public class ManagePurposeFragment extends Fragment {
    ArrayList<Purpose_Items> arrayList = new ArrayList();
    SQLiteDatabase db;
    PurposeAdapter mAdapter;
    Database mDbHelper;
    private RecyclerView recyclerView;

    class getData extends AsyncTask<Void, Void, Void> {
        getData() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... voidArr) {
            ManagePurposeFragment.this.db = ManagePurposeFragment.this.mDbHelper.getReadableDatabase();
            Cursor query = ManagePurposeFragment.this.db.query(Database.TABLE_PURPOSE, null, null, null, null, null, "created_on DESC");
            ManagePurposeFragment.this.arrayList.clear();
            while (query.moveToNext()) {
                Purpose_Items purpose_Items = new Purpose_Items();
                purpose_Items.setId(query.getString(query.getColumnIndex(Database.SR_ID)));
                purpose_Items.setName(query.getString(query.getColumnIndex(Database.COL_P_NAME)));
                purpose_Items.setTransationType(query.getInt(query.getColumnIndex(Database.COL_P_TRANSATION_TYPE)));
                purpose_Items.setType(query.getInt(query.getColumnIndex(Database.COL_P_TYPE)));
                ManagePurposeFragment.this.arrayList.add(purpose_Items);
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            ManagePurposeFragment.this.mAdapter.notifyDataSetChanged();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_manage_purpose, viewGroup, false);
        this.mDbHelper = new Database(getActivity());
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview_manage_purpose_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new PurposeAdapter(getActivity(), this.arrayList);
        this.recyclerView.setAdapter(this.mAdapter);
        new getData().execute(new Void[0]);
        ((FloatingActionButton) inflate.findViewById(R.id.fab_purpose_add)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ManagePurposeFragment.this.addNewPurpose();
            }
        });
        return inflate;
    }

    public void addNewPurpose() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_purpose, null);
        Builder builder = new Builder(getActivity());
        builder.setView(inflate);
        final AlertDialog create = builder.create();
        final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.input_add_purpose_name);
        Button button = (Button) inflate.findViewById(R.id.btn_add_purpose_cancel);
        Button button2 = (Button) inflate.findViewById(R.id.btn_add_purpose_add);
        final Spinner spinner = (Spinner) inflate.findViewById(R.id.spn_add_purpose);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Cash In");
        arrayList.add("Cash Out");
        arrayList.add(Database.PURPOSE_SALES);
        arrayList.add("Exchange");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), 17367048, arrayList);
        arrayAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(arrayAdapter);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                create.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (textInputLayout.getEditText().getText().toString().trim().isEmpty()) {
                    textInputLayout.setError("Enter purpose name");
                    return;
                }
                ManagePurposeFragment.this.db = ManagePurposeFragment.this.mDbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(Database.COL_P_NAME, textInputLayout.getEditText().getText().toString().trim());
                contentValues.put(Database.COL_P_TRANSATION_TYPE, Integer.valueOf(spinner.getSelectedItemPosition() + 1));
                contentValues.put(Database.COL_P_TYPE, Integer.valueOf(2));
                contentValues.put(Database.CREATED_ON, Helper.getCurrentDateTime(Database.defaultFormate));
                ManagePurposeFragment.this.db.insert(Database.TABLE_PURPOSE, null, contentValues);
                create.dismiss();
                new CToast(ManagePurposeFragment.this.getActivity()).simpleToast("Purpose added", 0).show();
                new getData().execute(new Void[0]);
            }
        });
        create.show();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDbHelper.close();
    }
}
