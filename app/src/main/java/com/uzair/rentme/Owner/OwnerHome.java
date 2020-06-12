package com.uzair.rentme.Owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uzair.rentme.Customer.CustomerHome;
import com.uzair.rentme.Models.OwnersData;
import com.uzair.rentme.R;

public class OwnerHome extends AppCompatActivity {


    private Toolbar mToolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private RecyclerView ownerRentList;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerOptions<OwnersData> options;
    private FirebaseRecyclerAdapter<OwnersData , MyViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        initViews();

        setRecyclerAdapter();


        findViewById(R.id.addFlat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(OwnerHome.this , AddFlatsActivity.class));

            }
        });


        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    firebaseAuth.signOut();
                    finish();
                }
                else if (item.getItemId() == R.id.shareApp) {

                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(OwnerHome.this)
                            .setType("text/plain")
                            .setText(getResources()
                                    .getString(R.string.share_app) +
                                    "\n" + "https://play.google.com/store/apps/details?id=" + getPackageName())
                            .getIntent(), "Share App!"));


                }
                else if (item.getItemId() == R.id.about) {

                }

                return true;
            }
        });



    }


    private void initViews()
    {
        ownerRentList = findViewById(R.id.flatsOwnerRecycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        ownerRentList.setLayoutManager(layoutManager);

        mToolbar = findViewById(R.id.ownerFlatToolbar);
        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setTitle("Your Flats|Rooms");


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("FlatAndRoom");

        Query query = databaseReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid());

        options = new FirebaseRecyclerOptions.Builder<OwnersData>()
                .setQuery(query, OwnersData.class)
                .setLifecycleOwner(this)
                .build();


    }


    private void setRecyclerAdapter()
    {

        adapter = new FirebaseRecyclerAdapter<OwnersData, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull OwnersData model)
            {
                holder.setHostelName(model.getFlatName());
                holder.setHostelNumber(model.getFlatNumber());
                holder.setCategory(model.getCategory());


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View myView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.design_for_owner_add_flat_list , null);
                MyViewHolder holder = new MyViewHolder(myView);

                return holder;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                findViewById(R.id.noFlatsAndRoom).setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

            }};

        ownerRentList.setAdapter(adapter);




    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private View myView;
        private TextView hostelName , hostelNumber , category;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView  = itemView;
        }

        private void setHostelName(String name)
        {
            hostelName = myView.findViewById(R.id.hostelNameValueInOwner);
            hostelName.setText(name);
        }

        private void setHostelNumber(String number)
        {
            hostelNumber = myView.findViewById(R.id.roomQunatityOwner);
            hostelNumber.setText(number);
        }

        private void setCategory(String cat)
        {
            category = myView.findViewById(R.id.categoryInOwner);
            category.setText(cat);
        }
    }


}
