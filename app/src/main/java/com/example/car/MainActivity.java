package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.databases.CarContract;
import com.example.car.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CarAdapter.OnItemClickListener {
    public static int REQ_FAB = 1;
    public static int REQ_ITEM = 2;
    public static String KEY_ID = "id";

    private CarAdapter adapter;
    private CarContract carContract;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        setSupportActionBar(binding.mainToolbar);

        //get cars from databases
        carContract = CarContract.getInstance(this);
        carContract.openData();
        List<Car> allCars = carContract.getAllCars();
        carContract.closeData();

        //inflate items into recycler
        adapter = new CarAdapter(this, allCars, this);
        binding.mainRecycler.setAdapter(adapter);
        binding.mainRecycler.setHasFixedSize(true);
        binding.mainRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        //fab
        binding.mainFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                startActivityForResult(intent, REQ_FAB);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchView(menu);
        return true;
    }

    private void searchView(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.main_search_menu).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                carContract.openData();
                List<Car> allCars = carContract.getAllCars(query);
                carContract.closeData();
                adapter.setCars(allCars);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                carContract.openData();
                List<Car> allCars = carContract.getAllCars(newText);
                carContract.closeData();
                adapter.setCars(allCars);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                carContract.openData();
                List<Car> allCars = carContract.getAllCars();
                carContract.closeData();
                adapter.setCars(allCars);
                return false;
            }
        });
    }

    @Override
    public void onClickListener(int id) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(KEY_ID, id);
        startActivityForResult(intent, REQ_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            carContract.openData();
            List<Car> allCars = carContract.getAllCars();
            carContract.closeData();
            adapter.setCars(allCars);
        }
    }

}
