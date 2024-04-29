package com.example.attendeasev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<String> mLists;
    private List<String> selectedValues = new ArrayList<>();
    private Context mContext;

    public Adapter(List<String> myDataset, Context context) {
        mLists = myDataset;
        mContext = context;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_checkboxes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, int position) {
        final String item = mLists.get(position);
        holder.cbActivitiesListReg.setText(item);

        holder.cbActivitiesListReg.setChecked(selectedValues.contains(item));
        holder.cbActivitiesListReg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedValues.add(item);
            } else {
                selectedValues.remove(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox cbActivitiesListReg;

        public ViewHolder(View v) {
            super(v);
            cbActivitiesListReg = v.findViewById(R.id.checkBox);
        }
    }
}
