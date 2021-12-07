package com.example.pantry_parser.RecyclerView;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantry_parser.R;
import com.example.pantry_parser.Models.Recipe;
import com.example.pantry_parser.Utilities.User;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_LOADING = 0;
    final int VIEW_TYPE_ITEM = 1;

    public List<Recipe> mItemList;
    private OnRecipeListener mOnRecipeListener;
    private String type;

    /**
     *Recycler View Adapter Constructor
     * @param recipeList
     * @param onRecipeListener
     */
    public RecyclerViewAdapter(List<Recipe> recipeList, OnRecipeListener onRecipeListener, String type) {
        mItemList = recipeList;
        this.mOnRecipeListener = onRecipeListener;
        this.type = type;
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
        if (viewType == VIEW_TYPE_ITEM && type == "r") {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view, mOnRecipeListener);
        } else if (viewType == VIEW_TYPE_ITEM && type == "i") {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_row, parent, false);
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
        ImageView chefVerified;
        ImageView recipeImage;
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
            if(type != "i"){
                minutesToMake = itemView.findViewById(R.id.time);
                ratingBar = itemView.findViewById(R.id.ratingBar);
                chefVerified = itemView.findViewById(R.id.Chef);
                recipeImage = itemView.findViewById(R.id.RecipeImage);
            }
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

        if(type != "i"){
            viewHolder.minutesToMake.setText(Integer.toString(item.getTimeToMake()));
            viewHolder.ratingBar.setRating((float) item.getRating());
            Picasso.get().load("http://coms-309-032.cs.iastate.edu:8080/recipe/" + item.getRecipeID() + "/image").centerCrop().resize(600, 400).into(viewHolder.recipeImage);
            if (item.getChefVerified() == false){
                viewHolder.chefVerified.setVisibility(View.INVISIBLE);
            }
        } else {
            if(((IngredientListView) mOnRecipeListener).viewType.contains("ADMIN")){
                String[] chunks = item.getRecipeName().split("--");
                String currRole = chunks[1].trim().toLowerCase();
                switch(currRole) {
                    case User.DESIGNATION_ADMIN:
                        viewHolder.recipeName.setTextColor(Color.RED);
                        break;
                    case User.DESIGNATION_CHEF:
                        viewHolder.recipeName.setTextColor(Color.BLUE);
                        break;
                    default:
                        viewHolder.recipeName.setTextColor(Color.BLACK);
                        break;
                }
            } else if (((IngredientListView) mOnRecipeListener).selected.contains(item.getRecipeName())){
                viewHolder.recipeName.setTextColor(Color.RED);
            }
        }
    }

    public interface OnRecipeListener{
        /**
         *
         * @param position
         */
        void onRecipeClick(int position);
    }

}
