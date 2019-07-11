package com.example.car;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.car.databases.CarContract;
import com.google.android.material.textfield.TextInputEditText;

public class DetailsActivity extends AppCompatActivity {

    public static int REQ_PICK_IMAGE = 3;

    private ImageView imageView;
    private Toolbar toolbar;
    private TextInputEditText et_Model, et_Color, et_Description, et_Dpl;

    private int id;
    private CarContract carContract;
    private Uri imageUri = Uri.parse("");
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //inflate views
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.image_view);
        et_Color = findViewById(R.id.details_edit_color);
        et_Description = findViewById(R.id.details_edit_description);
        et_Dpl = findViewById(R.id.details_edit_dpi);
        et_Model = findViewById(R.id.details_edit_model);


        setSupportActionBar(toolbar);
        carContract = CarContract.getInstance(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_IMAGE);

            }
        });


        id = getIntent().getIntExtra(MainActivity.KEY_ID, -1);
        if (id != -1) {
            onEditItem();
        }

    }

    private void onEditItem() {
        carContract.openData();
        Car car = carContract.getCar(id);
        carContract.closeData();
        if (car != null) {
            image = car.getImage();
            Glide.with(this).load(Uri.parse(image))
                    .error(R.drawable.car_background).into(imageView);
            et_Color.setText(car.getColor());
            et_Description.setText(car.getDescription());
            et_Model.setText(car.getModel());
            et_Dpl.setText(String.valueOf(car.getDpl()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem delete = menu.findItem(R.id.details_delete_menu);
        if (id == -1)
            delete.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_check_menu:
                checkItem();
                return true;
            case R.id.details_delete_menu:
                carContract.openData();
                Boolean car = carContract.deleteCar(id);
                carContract.closeData();
                if (car) {
                    Toast.makeText(this, "car deleted successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkItem() {

        String model = et_Model.getText().toString().trim();
        String color = et_Color.getText().toString().trim();
        String description = et_Description.getText().toString().trim();
        String dpl = et_Dpl.getText().toString().trim();
        if (!model.isEmpty() && !color.isEmpty() && !description.isEmpty()
                && !dpl.isEmpty()) {
            Car car = new Car(model, color, imageUri.toString(), description, Double.valueOf(dpl));
            if (id == -1) {
                carContract.openData();
                Boolean insertCar = carContract.insertCar(car);
                carContract.closeData();
                if (insertCar) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "car added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                car.setId(id);
                if (imageUri.toString().equals(""))
                    car.setImage(image);
                carContract.openData();
                Boolean updateCar = carContract.updateCar(car);
                carContract.closeData();
                if (updateCar) {
                    setResult(RESULT_OK);
                    Toast.makeText(this, "car updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


        } else
            Toast.makeText(this, "Please inset all data", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();

            Glide.with(DetailsActivity.this).load(imageUri)
                    .error(R.drawable.car_background).into(imageView);

        }
    }
}
