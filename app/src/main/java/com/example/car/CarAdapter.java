package com.example.car;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> cars;
    private OnItemClickListener listener;
    protected Context context;

    public CarAdapter(Context context, List<Car> cars, OnItemClickListener listener) {
        this.cars = cars;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_layout, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        Glide.with(context).load(Uri.parse(car.getImage()))
                .error(R.drawable.car_background).into(holder.imageView);
        holder.textColor.setText(car.getColor());
        holder.textModel.setText(car.getModel());
        holder.textDpl.setText(String.valueOf(car.getDpl()));
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void setCars(List<Car> allCars) {
        this.cars = allCars;
        notifyDataSetChanged();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textColor, textModel, textDpl;

        public CarViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.item_iv);
            textColor = view.findViewById(R.id.item_tv_color);
            textDpl = view.findViewById(R.id.item_tv_dbl);
            textModel = view.findViewById(R.id.item_tv_model);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                Car car = cars.get(getAdapterPosition());
//                int id = (int) imageView.getTag();
                listener.onClickListener(car.getId());
            }
        }
    }

    public interface OnItemClickListener {
        void onClickListener(int id);
    }
}
