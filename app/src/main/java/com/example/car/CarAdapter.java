package com.example.car;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.car.databinding.ItemCarLayoutBinding;

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
        ItemCarLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_car_layout, parent, false);
        return new CarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        Glide.with(context).load(Uri.parse(car.getImage()))
                .error(R.drawable.car_background).into(holder.binding.itemIv);
        holder.binding.itemTvColor.setText(car.getColor());
        holder.binding.itemTvModel.setText(car.getModel());
        holder.binding.itemTvDbl.setText(String.valueOf(car.getDpl()));
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
        ItemCarLayoutBinding binding;

        public CarViewHolder(@NonNull ItemCarLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);

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
