package com.uzair.rentme.Owner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uzair.rentme.R;

import java.util.HashMap;
import java.util.Map;

public class AddFlatsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int REQUEST_CODE = 1;
    private String[] type = {"Flat", "Room"};
    private String[] availableSeats = {"1", "2", "3", "4", "5", "All available"};
    private String[] area = {"Maripol", "Chernivtsi", " Uzhhorod", "Myklaiv", "Kryvyi Rih", "Ternopil", "Lutsk", "Luhansk",
            "Kam yanets", "Kyiv", "Lviv", "Odesa", "Kharkiv", "Lvano", "Dnipro", "Zaporizhzhia", "Donetsk",
            "Vinnytsia"};
    private String[] category = {"For Males", "For Females"};
    private ArrayAdapter adapter;

    private Spinner typeSpinner, availableSeatsSpinner, areaSpinner, categorySpinner;
    private TextInputLayout tvFlatNumber, tvFlatName, tvFlatQuantity, tvFlatRent;
    private ImageView flatImage;
    private Uri imageUri;
    private Button addImage;
    private String areaResult, typeResult, seatsResult, categoryResult;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser user;
    private ProgressDialog mProgress;
    private Toolbar mToolbar;
    private ConstraintLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flats);
        setTitle("Add Flats|Rooms");
        initViews();

        findViewById(R.id.uploadFlatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFlatDataToFirebase();
            }
        });

    }

    private void initViews() {

        mToolbar = findViewById(R.id.addFlatToolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layout = findViewById(R.id.addFlatLayout);

        // flat image
        flatImage = findViewById(R.id.flatImage);
        addImage = findViewById(R.id.btnAddImage);
        mProgress = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        // edit text
        tvFlatNumber = findViewById(R.id.flatNumber);
        tvFlatName = findViewById(R.id.flatName);
        tvFlatQuantity = findViewById(R.id.flatSeatsQuantity);
        tvFlatRent = findViewById(R.id.rentOfFlat);

        // spinners
        typeSpinner = findViewById(R.id.acomdat_add_flat_Spinner);
        typeSpinner.setOnItemSelectedListener(this);
        setAdapter(typeSpinner, type);

        availableSeatsSpinner = findViewById(R.id.avail_seats_spinner);
        availableSeatsSpinner.setOnItemSelectedListener(this);
        setAdapter(availableSeatsSpinner, availableSeats);

        areaSpinner = findViewById(R.id.area_flat_spinner);
        areaSpinner.setOnItemSelectedListener(this);
        setAdapter(areaSpinner, area);

        categorySpinner = findViewById(R.id.category_flat_spinner);
        categorySpinner.setOnItemSelectedListener(this);
        setAdapter(categorySpinner, category);


        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


    }


    // upload flat data to firebase
    private void addFlatDataToFirebase() {
        final String flatNumber, flatName, quantity, rent;
        flatName = tvFlatName.getEditText().getText().toString();
        flatNumber = tvFlatNumber.getEditText().getText().toString();
        quantity = tvFlatQuantity.getEditText().getText().toString();
        rent = tvFlatRent.getEditText().getText().toString();


        if (!flatName.isEmpty() && !flatNumber.isEmpty() &&
                !quantity.isEmpty() && !rent.isEmpty()
                && imageUri != null &&
                !areaResult.isEmpty() && !typeResult.isEmpty() &&
                !categoryResult.isEmpty() && !seatsResult.isEmpty()) {
            mProgress.setMessage("Please wait...");
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setCancelable(false);
            mProgress.show();


            databaseReference.child("Users").child(user.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                            final String phoneNumber = dataSnapshot.child("phone").getValue().toString();
                            final String ownerName = dataSnapshot.child("name").getValue().toString();


                            final StorageReference path = storageReference.child("FlatPhotos").child(imageUri.getLastPathSegment());
                            path.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String downloadUrl = String.valueOf(uri);

                                            Map<String, String> flatData = new HashMap<>();
                                            flatData.put("ownerName", ownerName);
                                            flatData.put("flatImage", downloadUrl);
                                            flatData.put("flatNumber", flatNumber);
                                            flatData.put("flatName", flatName);
                                            flatData.put("flatQuantity", quantity);
                                            flatData.put("flatRent", rent);
                                            flatData.put("ownerPhone", phoneNumber);
                                            flatData.put("type", typeResult);
                                            flatData.put("area", areaResult);
                                            flatData.put("category", categoryResult);
                                            flatData.put("availableSeats", seatsResult);
                                            flatData.put("userId", user.getUid());


                                            databaseReference.child("FlatAndRoom")
                                                    .push().setValue(flatData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {
                                                                mProgress.dismiss();
                                                            } else {
                                                                mProgress.dismiss();
                                                            }

                                                        }
                                                    });



                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    mProgress.dismiss();
                                    Log.d("url", "onFailure: " + e.getMessage());
                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        } else {
            if (imageUri == null) {

                Snackbar snackbar = Snackbar.make(layout, "Please Upload Image" , Snackbar.LENGTH_LONG);
                snackbar.show();

                Toast.makeText(this, "Upload Image", Toast.LENGTH_SHORT).show();
            } else {
                tvFlatRent.setError("Required");
                tvFlatQuantity.setError("Required");
                tvFlatNumber.setError("Required");
                tvFlatNumber.setError("Required");
            }
        }


    }


    //spinner adapter
    private void setAdapter(Spinner spinner, String[] items) {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);
    }


    public void addFlatPhoto(View view) {
        String[] options = {"Choose From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddFlatsActivity.this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);

                }
                if (which == 1) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            imageUri = data.getData();
            flatImage.setImageURI(imageUri);
            addImage.setText("Change Picture");

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.area_flat_spinner) {
            areaResult = adapterView.getItemAtPosition(i).toString();

        } else if (adapterView.getId() == R.id.avail_seats_spinner) {
            seatsResult = adapterView.getItemAtPosition(i).toString();

        } else if (adapterView.getId() == R.id.category_flat_spinner) {
            categoryResult = adapterView.getItemAtPosition(i).toString();

        } else if (adapterView.getId() == R.id.acomdat_add_flat_Spinner) {
            typeResult = adapterView.getItemAtPosition(i).toString();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
