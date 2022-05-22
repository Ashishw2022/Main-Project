package com.example.afinal;

import static java.util.Locale.filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class Admin_doc_adap extends AppCompatActivity {

    private RecyclerView rv;
    private FirebaseFirestore firestore;

    private FirestoreRecyclerAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doc_adap);
        firestore=FirebaseFirestore.getInstance();



// Create a query against the collection.
        //view

        Query query = firestore.collection("Users").whereEqualTo("role","Doctor");
            //recyclerOptions
        FirestoreRecyclerOptions<doctor_model> options=new FirestoreRecyclerOptions.Builder<doctor_model>()
                .setQuery(query,doctor_model.class)
                .build();

         adapter= new FirestoreRecyclerAdapter<doctor_model,docview>(options) {
            @NonNull
            @Override
            public docview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admdoc_tv,parent,false);
                return new docview(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull docview holder, int position, @NonNull doctor_model model) {
                holder.n_name.setText(model.getName());
                holder.n_email.setText(model.getUemail());

            }
        };
        rv=(RecyclerView)findViewById(R.id.recycler_doc);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        }


    private class docview extends RecyclerView.ViewHolder{
        private TextView n_name,n_email;
        public docview(@NonNull View itemView) {
            super(itemView);
            n_name=itemView.findViewById(R.id.admdoc_name);
            n_email=itemView.findViewById(R.id.admdoc_email);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}

