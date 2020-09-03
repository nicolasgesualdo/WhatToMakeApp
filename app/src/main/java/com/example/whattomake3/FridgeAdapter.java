package com.example.whattomake3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.FridgeViewHolder> {

    private Context myContext;
    private Cursor myCursor;
    private long id;

    public FridgeAdapter(Context context, Cursor cursor){
        myContext = context;
        myCursor = cursor;
    }

    private OnItemClickListener myListener;

    public interface OnItemClickListener{
        void OnItemClick(long id);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        myListener = listener;
    }


    public class FridgeViewHolder extends RecyclerView.ViewHolder{
        public TextView myIngText;
        public ImageView myDeleteX;

        //Constructor
        public FridgeViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            myIngText = itemView.findViewById(R.id.ingText);
            myDeleteX = itemView.findViewById(R.id.xButton);

            myDeleteX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        long position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick((long)itemView.getTag());
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View v = inflater.inflate(R.layout.fridge_item, parent,false);
        FridgeViewHolder fvh = new FridgeViewHolder(v,myListener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeViewHolder holder, int position) {
        if(!myCursor.moveToPosition(position)){
            return;
        }
        String name = myCursor.getString(myCursor.getColumnIndex(FridgeContract.FridgeEntry.COLUMN_NAME));
        id = myCursor.getLong(myCursor.getColumnIndex(FridgeContract.FridgeEntry._ID));

        holder.myIngText.setText(name);
        holder.itemView.setTag(id);//item view is whole item of entry(name and x)
    }

    @Override
    public int getItemCount() {
        return myCursor.getCount();
    }

    //need new cursor every time we want to pass to database
    public void swapCursor(Cursor newCursor) {
        if(myCursor != null){
            myCursor.close();
        }
        myCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
}
