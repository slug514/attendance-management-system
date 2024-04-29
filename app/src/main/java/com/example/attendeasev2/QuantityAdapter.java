package com.example.attendeasev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuantityAdapter extends RecyclerView.Adapter<QuantityAdapter.ViewHolder> {
   View view;
   Context context;
   ArrayList<String> arrayList;
   QuantityListner quantityListner;

   ArrayList<String> arrayList_0=new ArrayList<>();


    public QuantityAdapter(Context context, ArrayList<String> arrayList, QuantityListner quantityListner) {
        this.context = context;
        this.arrayList = arrayList;
        this.quantityListner = quantityListner;
    }

    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.rv_layout,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull QuantityAdapter.ViewHolder holder, int position) {

        while (arrayList!=null && arrayList.size()>0)
        {
            holder.checkBox.setText(arrayList.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = holder.getAdapterPosition();
                    if (holder.checkBox.isChecked()) {
                        arrayList_0.add(arrayList.get(currentPosition));
                    }
                    else
                    {
                        arrayList_0.remove(arrayList.get(currentPosition));
                    }
                    quantityListner.onQuantityChange(arrayList_0);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            checkBox=itemView.findViewById(R.id.check_box);

        }
    }
}
