package br.com.stilldistribuidora.stillrtc.ui.adapters;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

import android.app.Activity;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.Store;
import br.com.stilldistribuidora.stillrtc.ui.activities.DeliveriesActivity;


public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolderItem> {

    private Activity context;
    private int lastPosition = -1;
    private Boolean empty;
    private int pos;
    private boolean loading;
    private Store.Result storeResult;

    public StoreAdapter(Activity context, ArrayList<Store> phraseArrayList, Boolean empty) {
        this.context = context;
        this.empty = empty;
        this.storeResult = new Store.Result();
        this.storeResult.ar = phraseArrayList;
    }

    public void update(Boolean empty, int pos) {

        this.empty = empty;
        this.pos = pos;
        notifyDataSetChanged();
    }

    public void updateArraySearch(ArrayList<Store> designs) {
        this.storeResult = new Store.Result();
        this.storeResult.ar = designs;
    }

    public void removeItem(int position) {
        storeResult.ar.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }


    @Override
    public StoreAdapter.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empty, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_store, parent, false);
        }
        return new StoreAdapter.ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(final StoreAdapter.ViewHolderItem customViewHolder, final int position) {
        final Store feedItem = storeResult.ar.get(position);

        if (this.empty) {
            customViewHolder.tvTitle.setText(feedItem.getName());
            customViewHolder.ivNoItem.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_store_black_48dp));

        } else {

            try {

                customViewHolder.tvTitle.setText(feedItem.getName());
                customViewHolder.tvManager.setText("Gerente: " + feedItem.getNameManager());
                customViewHolder.tvEmail.setText("Email: " + feedItem.getEmail());


//                if (feedItem.getStatus() == 1) {
//                    customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorStatusSuccess));
//                    customViewHolder.tvStatus.setText(context.getString(R.string.str_finalized).toUpperCase());
//                } else if(feedItem.getStatus() == 2) {
//                    customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorStatusWarning));
//                    customViewHolder.tvStatus.setText(context.getString(R.string.str_in_progress).toUpperCase());
//                } else {
//                    customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorStatusDanger));
//                    customViewHolder.tvStatus.setText(context.getString(R.string.str_pending).toUpperCase());
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;

                    intent = new Intent(context, DeliveriesActivity.class);
                    intent.putExtra("object", storeResult);
                    intent.putExtra("position", position);
                    intent.putExtra("name", feedItem.getName());
                    context.startActivity(intent);
                    context.overridePendingTransition(0, 0);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != storeResult.ar ? storeResult.ar.size() : 0);
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvManager;
        TextView tvEmail;
        ImageView ivNoItem;

        ViewHolderItem(View view) {
            super(view);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tvManager = (TextView) view.findViewById(R.id.tvManager);
            this.tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            this.ivNoItem = (ImageView) view.findViewById(R.id.ivNoItem);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && this.empty)
            return 0;
        return 1;
    }

}