package org.me.gcu.coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> implements Filterable{

    private Context context;
    private List<Location> locationList;
    private List<Location> locationListFull;

    public LocationAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
        this.locationListFull = new ArrayList<>(locationList);  // Keep a copy of the original list
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.nameTextView.setText(location.getName());
        holder.addressText.setText(location.getAddress());
        holder.ratingBar.setRating(location.getRating());
        holder.locationImageView.setImageResource(location.getImageResId());
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public Filter getFilter() {
        return locationFilter;
    }

    private Filter locationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Location> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(locationListFull);  // Return full list if no filter
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Location location : locationListFull) {
                    if (location.getName().toLowerCase().contains(filterPattern) ||
                            location.getAddress().toLowerCase().contains(filterPattern)) {
                        filteredList.add(location);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            locationList.clear();
            if (results != null && results.count > 0) {
                locationList.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    };






    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressText;
        public ImageView locationImageView;
        public RatingBar ratingBar;

        public LocationViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.locName);
            addressText = itemView.findViewById(R.id.locAdd);
            ratingBar = itemView.findViewById(R.id.locRating);
            locationImageView = itemView.findViewById(R.id.location_image);
        }
    }
}
