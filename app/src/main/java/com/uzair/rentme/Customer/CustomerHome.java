package com.uzair.rentme.Customer;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uzair.rentme.Adapters.ViewPagerAdapter;
import com.uzair.rentme.R;


public class CustomerHome extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] accomdationName = {"Flats", "Room"};
    private String[] citiesName = {"Maripol", "Chernivtsi", " Uzhhorod", "Myklaiv", "Kryvyi Rih", "Ternopil", "Lutsk", "Luhansk",
            "Kam yanets", "Kyiv", "Lviv", "Odesa", "Kharkiv", "Lvano", "Dnipro", "Zaporizhzhia", "Donetsk",
            "Vinnytsia"};
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private ViewPagerAdapter viewPagerAdapter;
    private int dotscount;
    private ImageView[] dots;
    private Spinner areasSpinner, accomodataionSpinner;
    private ArrayAdapter adapter;
    private Toolbar mToolbar;
    private String areaName, type;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        initViews();
        setViewPager();

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    finish();
                }
                else if (item.getItemId() == R.id.shareApp) {

                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(CustomerHome.this)
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


    private void initViews() {

        mToolbar = findViewById(R.id.customerToolBar);
        mToolbar.inflateMenu(R.menu.menu);
        mToolbar.setTitle("Home");


        viewPager = (ViewPager) findViewById(R.id.viewPagerCustomers);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        viewPagerAdapter = new ViewPagerAdapter(this);

        // spinners
        areasSpinner = findViewById(R.id.areaSpinner);
        accomodataionSpinner = findViewById(R.id.accomodationSpinner);

        //area spinner adapter
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, citiesName);
        areasSpinner.setOnItemSelectedListener(this);
        areasSpinner.setAdapter(adapter);
        //accommodation spinner adapter
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accomdationName);
        accomodataionSpinner.setOnItemSelectedListener(this);
        accomodataionSpinner.setAdapter(adapter);

        //firebase
        mAuth = FirebaseAuth.getInstance();


    }


    private void setViewPager() {

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    // search button click
    public void searchBtnClick(View view) {
        Intent searchIntent = new Intent(CustomerHome.this, Available.class);
        searchIntent.putExtra("area", areaName);
        searchIntent.putExtra("type", type);
        startActivity(searchIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.areaSpinner) {
            areaName = adapterView.getItemAtPosition(i).toString();

        } else if (adapterView.getId() == R.id.accomodationSpinner) {
            type = adapterView.getItemAtPosition(i).toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
