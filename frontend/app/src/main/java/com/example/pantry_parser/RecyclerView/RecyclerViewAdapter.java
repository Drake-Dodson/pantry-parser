package com.example.pantry_parser.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantry_parser.R;
import com.example.pantry_parser.Recipe;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_LOADING = 0;
    final int VIEW_TYPE_ITEM = 1;



    public List<Recipe> mItemList;
    private OnRecipeListener mOnRecipeListener;

    /**
     *Recycler View Adapter Constructor
     * @param recipeList
     * @param onRecipeListener
     */
    public RecyclerViewAdapter(List<Recipe> recipeList, OnRecipeListener onRecipeListener) {
        mItemList = recipeList;
        this.mOnRecipeListener = onRecipeListener;
    }

    /**
     *Layout inflator and viewholder setup
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view, mOnRecipeListener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    /**
     *Populates rows on bind
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        }
    }

    /**
     *Returns the type of row, loading or item
     * @param position the position of the row in the list view
     * @return the item view type of the row at position
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /**
     *Gets the number of items in the list view
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeName;
        TextView minutesToMake;
        RatingBar ratingBar;
        OnRecipeListener onRecipeListener;

        /**
         *Sets the information for each Recipe in list view
         * @param itemView
         * @param onRecipeListener
         */
        public ItemViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.title);
            minutesToMake = itemView.findViewById(R.id.time);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            itemView.setOnClickListener(this);
            this.onRecipeListener = onRecipeListener;

            itemView.setOnClickListener(this);
        }

        /**
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
onRecipeListener.onRecipeClick(getAbsoluteAdapterPosition());
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        /**
         *
         * @param itemView
         */
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     *populates the row items with recipe information
     * @param viewHolder
     * @param position the position at which the row to be filled is in the list view
     */
    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        Recipe item = mItemList.get(position);
        viewHolder.recipeName.setText(item.getRecipeName());
        viewHolder.minutesToMake.setText(Integer.toString(item.getTimeToMake()));
        viewHolder.ratingBar.setRating((float) item.getRating());
    }

    public interface OnRecipeListener{
        /**
         *
         * @param position
         */
        void onRecipeClick(int position);
    }

}
