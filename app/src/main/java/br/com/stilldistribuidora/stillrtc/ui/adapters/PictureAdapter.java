package br.com.stilldistribuidora.stillrtc.ui.adapters;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import br.com.stilldistribuidora.stillrtc.ui.activities.MapsActivity;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;
import br.com.stilldistribuidora.stillrtc.utils.DialogResultListener;


public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolderItem> {

    private Context context;
    private int lastPosition = -1;
    private Boolean empty;
    private int pos;
    private boolean loading;
    private Picture.Result phraseResult;


    public PictureAdapter(Context context, ArrayList<Picture> phraseArrayList, Boolean empty) {
        this.context = context;
        this.empty = empty;
        this.phraseResult = new Picture.Result();
        this.phraseResult.ar = phraseArrayList;
    }

    public void update(Boolean empty, int pos) {

        this.empty = empty;
        this.pos = pos;
        notifyDataSetChanged();
    }

    public void updateArraySearch(ArrayList<Picture> designs) {
        this.phraseResult = new Picture.Result();
        this.phraseResult.ar = designs;
    }

    public void removeItem(int position) {
        phraseResult.ar.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }


    @Override
    public PictureAdapter.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_picture, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_picture, parent, false);
        }
        return new PictureAdapter.ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(final PictureAdapter.ViewHolderItem customViewHolder, final int position) {
        final Picture feedItem = phraseResult.ar.get(position);

        if (this.empty) {
            //customViewHolder.desc.setText("rolou não");
        } else {


            Glide.with(context).load(feedItem.getUri())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(customViewHolder.ivPic);

            String strPicName = context.getResources().getString(R.string.str_picture)+" "+(position+1);
            customViewHolder.tvPicName.setText(strPicName);

            if(feedItem.getSync()==1)
                customViewHolder.ivCheck.setVisibility(View.VISIBLE);
            else customViewHolder.ivCheck.setVisibility(View.GONE);


            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(context.getClass()== MapsActivity.class){
                        ((MapsActivity) context).definirLocalizacao(feedItem.getLatitude(), feedItem.getLongitude());
                    } else {

                        String[] array = {
                                "Titulo",
                                "Sumary",
                                context.getString(R.string.btn_cancel),
                                context.getString(R.string.cast_tracks_chooser_dialog_ok)};

                        showDialog(array, new DialogResultListener() {
                            @Override
                            public void onResult(boolean result) {

                            }
                        }, position);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != phraseResult.ar ? phraseResult.ar.size() : 0);
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {
        ImageView ivPic, ivCheck;
        TextView tvPicName;
        ViewHolderItem(View view) {
            super(view);
            this.ivPic = (ImageView) view.findViewById(R.id.ivPic);
            this.tvPicName = (TextView) view.findViewById(R.id.tvPicName);
            this.ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && this.empty)
            return 0;
        return 1;
    }

    private void showDialog(String[] array, final DialogResultListener listener, int position) {

        String title = array[0];
        final String content = array[1];
        String btnCancel = array[2];
        String btnDone = array[3];

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.dialog_image, null);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setView(dialog_layout);
        builder.setCancelable(false);

        ImageView thumbImage = (ImageView) dialog_layout.findViewById(R.id.thumbImage);
        Glide.with(context)
                .load(phraseResult.ar.get(position).getUri())
                .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                .into(thumbImage);

        builder.setPositiveButton(btnDone, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //listener.onResult(true);
            }
        });
        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //listener.onNeutral(0);
            }
        });

        builder.show();
    }
}