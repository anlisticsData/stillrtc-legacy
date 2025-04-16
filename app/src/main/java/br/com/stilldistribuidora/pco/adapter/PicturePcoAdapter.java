package br.com.stilldistribuidora.pco.adapter;

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

import java.util.List;

import br.com.stilldistribuidora.pco.Interface.ClickRecyclerView_Interface;
import br.com.stilldistribuidora.pco.db.model.PictureImageGrafica;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.utils.DialogResultListener;


public class PicturePcoAdapter extends RecyclerView.Adapter<PicturePcoAdapter.ViewHolderItem> {
    public static ClickRecyclerView_Interface clickRecyclerViewInterface;
    public static Context mctx;
    List<PictureImageGrafica> imagensGrafica;



    public  PicturePcoAdapter(Context self, List<PictureImageGrafica>imagensGrafica, ClickRecyclerView_Interface clickRecyclerViewInterface){
        this.imagensGrafica =imagensGrafica;
        this.mctx = self;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;

    }



    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;
        v = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.row_picture_pco, viewGroup, false);
        return new ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, final int position) {
        final int poss = position;
        final PictureImageGrafica feedItem = imagensGrafica.get(poss);
        Glide.with(mctx).load(feedItem.getPath_file())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPic);

        final String strPicName = mctx.getResources().getString(R.string.str_picture)+" "+(position+1);
        holder.tvPicName.setText(strPicName);
        if(feedItem.getSincronizado().equals("1")) {
            holder.ivCheck.setVisibility(View.VISIBLE);
        }else {
            holder.ivCheck.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] array = {
                        feedItem.getName_file(),
                        "Sumary",
                        mctx.getString(R.string.btn_cancel),
                        mctx.getString(R.string.cast_tracks_chooser_dialog_ok)};

                showDialog(array, new DialogResultListener() {
                    @Override
                    public void onResult(boolean result) {

                    }
                }, position);

            }
        });


















    }

    @Override
    public int getItemCount() {
        return imagensGrafica.size();
    }

    static  class ViewHolderItem extends RecyclerView.ViewHolder {
        ImageView ivPic, ivCheck;
        TextView tvPicName;
        public ViewHolderItem(View itemView) {
            super(itemView);
            this.ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
            this.tvPicName = (TextView) itemView.findViewById(R.id.tvPicName);
            this.ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);

        }
    }


    private void showDialog(String[] array, final DialogResultListener listener, int position) {

        String title = array[0];
        final String content = array[1];
        String btnCancel = array[2];
        String btnDone = array[3];

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View dialog_layout = inflater.inflate(R.layout.dialog_image, null);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(mctx, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setView(dialog_layout);
        builder.setCancelable(false);

        ImageView thumbImage = (ImageView) dialog_layout.findViewById(R.id.thumbImage);
        Glide.with(mctx)
                .load(imagensGrafica.get(position).getPath_file())
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
