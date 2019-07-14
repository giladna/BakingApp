package com.udacity.giladna.bakingapp.ui;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.udacity.giladna.bakingapp.R;
import com.udacity.giladna.bakingapp.databinding.ItemMainBinding;
import com.udacity.giladna.bakingapp.model.Recipe;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder> {

    private Context mContext;
    private List<Recipe> mList;

    private final ClickCallback<Recipe> mRecipeClickCallback;

    public MainAdapter(Context context, ClickCallback<Recipe> clickCallback) {
        mContext = context;
        mRecipeClickCallback = clickCallback;
    }

    @NonNull
    @Override
    public MainAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMainBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_main, parent, false);
        binding.setCallback(mRecipeClickCallback);
        return new MainAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapterViewHolder holder, int position) {
        Recipe recipe = mList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void setList(List<Recipe> list) {
        mList = list;
        notifyDataSetChanged();
    }

    class MainAdapterViewHolder extends RecyclerView.ViewHolder {
        final ItemMainBinding binding;

        MainAdapterViewHolder(ItemMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Recipe recipe) {
            binding.setRecipe(recipe);

            if (!TextUtils.isEmpty(recipe.getImage())) {
                Glide.with(mContext)
                        .load(recipe.getImage())
                        .error(R.drawable.error)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.recipeImage);
            } else {
                binding.recipeImage.setImageResource(R.drawable.placeholder);
            }
        }
    }
}
