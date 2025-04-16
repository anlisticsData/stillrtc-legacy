package br.com.stilldistribuidora.stillrtc.ui.adapters;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.OnListClickInteractionListener;
import br.com.stilldistribuidora.stillrtc.db.models.PointsInMap;
import br.com.stilldistribuidora.stillrtc.ui.activities.ListAdpterPointsViewHolder;

/**
 * Created by Still Technology and Development Team on 20/08/2018.
 */

public class ListAdapterPoints extends RecyclerView.Adapter<ListAdpterPointsViewHolder> {
    private List<PointsInMap> Points;
    private  OnListClickInteractionListener mOnlistClick;


    public ListAdapterPoints( List<PointsInMap> Points,OnListClickInteractionListener listener){

        this.Points = Points;
        this.mOnlistClick = listener;


    }

    @Override
    public ListAdpterPointsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.row_points, parent, false);


        return new ListAdpterPointsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ListAdpterPointsViewHolder holder, int position) {

        PointsInMap point =Points.get(position);
        holder.bindData(point,mOnlistClick,position);








    }

    @Override
    public int getItemCount() {
        return Points.size();
    }
}
