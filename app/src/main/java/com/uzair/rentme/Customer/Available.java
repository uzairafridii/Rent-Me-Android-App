package com.uzair.rentme.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.uzair.rentme.Models.OwnersData;
import com.uzair.rentme.Owner.OwnerHome;
import com.uzair.rentme.R;

public class Available extends AppCompatActivity {

    private Intent intent;
    private String area, type;
    private Toolbar mToolbar;
    private RecyclerView mAvailableList;
    private LinearLayoutManager layoutManager;
    private DatabaseReference mDatabaseRef;
    private FirebaseRecyclerOptions<OwnersData> options;
    private FirebaseRecyclerAdapter<OwnersData, MyViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available);
        setTitle("Available Rooms|Flats");
        initViews();


        setRecyclerAdapter();

    }

    private void initViews() {
        mToolbar = findViewById(R.id.availableToolBar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAvailableList = findViewById(R.id.rvAvailable);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mAvailableList.setLayoutManager(layoutManager);

        intent = getIntent();
        area = intent.getStringExtra("area");
        type = intent.getStringExtra("type");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("FlatAndRoom");

        Query query = mDatabaseRef.orderByChild("area").equalTo(area);

        options = new FirebaseRecyclerOptions.Builder<OwnersData>()
                .setQuery(query, OwnersData.class)
                .setLifecycleOwner(this)
                .build();


    }


    private void setRecyclerAdapter() {

        adapter = new FirebaseRecyclerAdapter<OwnersData, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull OwnersData model) {
                if (model.getType().equals(type)) {
                    holder.setHostelName(model.getFlatName());
                    holder.setHostelNumber(model.getFlatNumber());
                    holder.setCategory(model.getCategory());
                } else {
                    //TODO: set layout height to 0dp
                }


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View myView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.design_for_available_accomodation_recycler, null);
                MyViewHolder holder = new MyViewHolder(myView);
                return holder;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                findViewById(R.id.noFlatsAndRoom).setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }
        };

        mAvailableList.setAdapter(adapter);


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View myView;
        private TextView hostelName, hostelNumber, category;
        private Button checkDetailsBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
            checkDetailsBtn = myView.findViewById(R.id.checkDetailsBtn);
        }

        private void setHostelName(String name) {
            hostelName = myView.findViewById(R.id.hostelNameValue);
            hostelName.setText(name);
        }

        private void setHostelNumber(String number) {
            hostelNumber = myView.findViewById(R.id.roomQunatity);
            hostelNumber.setText(number);
        }

        private void setCategory(String cat) {
            category = myView.findViewById(R.id.category);
            category.setText(cat);
        }
    }

}
