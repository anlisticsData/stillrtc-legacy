package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.OnListClickInteractionListener;
import br.com.stilldistribuidora.stillrtc.db.models.PointsInMap;

/**
 * Created by Still Technology and Development Team on 20/08/2018.
 */

public class ListAdpterPointsViewHolder extends RecyclerView.ViewHolder {
    private final Context mContex;
    private final CardView viewById;
    private TextView txt_dispositivo;
    private ImageView img_googleNavegar;
    private ImageView img_wazerNavegar;
    private ImageView img__dispositivoStatus;

    public ListAdpterPointsViewHolder(View itemView) {
        super(itemView);
        mContex = itemView.getContext();
        txt_dispositivo = (TextView) itemView.findViewById(R.id.sup_txt_dispositivo);
        img_googleNavegar = (ImageView) itemView.findViewById(R.id.sup_img_google);
        img_wazerNavegar = (ImageView) itemView.findViewById(R.id.sup_img_wazer);

        viewById = (CardView) itemView.findViewById(R.id.row_card_points);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bindData(PointsInMap point, final OnListClickInteractionListener listener, final int position) {
        txt_dispositivo.setText(point.dispositivoId);


        if (Integer.parseInt(point.operacao_status) == 2) {
            viewById.setCardBackgroundColor(Color.WHITE);
        } else {

            if (Integer.parseInt(point.operacao_status) == 0) {
                viewById.setCardBackgroundColor(Color.WHITE);
            } else {
                if (point.dispositivoOsiosos.contains(point.dispositivoId)) {
                    viewById.setCardBackgroundColor(mContex.getResources().getColor(R.color.colorPeachPuf));
                } else {
                    viewById.setCardBackgroundColor(Color.WHITE);
                }
            }
        }
        img_googleNavegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick("MAPS", position);
            }
        });
        img_wazerNavegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick("WAZER", position);
            }
        });


    }
}
