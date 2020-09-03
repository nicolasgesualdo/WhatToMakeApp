package com.example.whattomake3;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Ingredient> ingList;

    public CustomAdapter(ArrayList<Ingredient> ing){
        ingList = ing;
    }

    private OnItemClickListener myListener;

    public interface OnItemClickListener{
        void OnItemClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        myListener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView myIngName;
        public TextView myIngQuantity;
        public ImageView myDeleteX;

        //Constructor
        public CustomViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            myIngName = itemView.findViewById(R.id.addIngName);
            myDeleteX = itemView.findViewById(R.id.xDelete);
            myIngQuantity = itemView.findViewById(R.id.addIngQuantity);

            myDeleteX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(v,myListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Ingredient currentItem = ingList.get(position);

        holder.myIngName.setText(currentItem.getIngName());
        holder.myIngQuantity.setText(currentItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        return ingList.size();
    }
}
