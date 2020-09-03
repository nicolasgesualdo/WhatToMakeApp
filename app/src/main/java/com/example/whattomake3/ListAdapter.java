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

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context myContext;
    private Cursor myCursor;
    private long id;

    public ListAdapter(Context context, Cursor cursor){
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

    public class ListViewHolder extends RecyclerView.ViewHolder{
        public TextView myIngText;
        public ImageView myDeleteX;

        //Constructor
        public ListViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            myIngText = itemView.findViewById(R.id.ingTextList);
            myDeleteX = itemView.findViewById(R.id.xButtonList);

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
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(myContext);
        View v = inflater.inflate(R.layout.list_item, parent,false);
        ListViewHolder fvh = new ListViewHolder(v,myListener);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        if(!myCursor.moveToPosition(position)){
            return;
        }
        String name = myCursor.getString(myCursor.getColumnIndex(ListContract.ListEntry.COLUMN_NAME));
        id = myCursor.getLong(myCursor.getColumnIndex(ListContract.ListEntry._ID));

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

