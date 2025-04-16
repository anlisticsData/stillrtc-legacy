package br.com.stilldistribuidora.stillrtc.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.OnListClickInteractionListener;
import br.com.stilldistribuidora.stillrtc.db.models.SupervisorOperacao;
import br.com.stilldistribuidora.stillrtc.ui.activities.SupervisorViewHolder;

/**
 * Created by Still Technology and Development Team on 08/08/2018.
 */

public class ListSupervisorAdapter extends RecyclerView.Adapter<SupervisorViewHolder> {

    private List<SupervisorOperacao> ListSupervisor;
    private  OnListClickInteractionListener mOnlistClick;

    public ListSupervisorAdapter(List<SupervisorOperacao> listSupervisor,OnListClickInteractionListener listener) {
        ListSupervisor = listSupervisor;
        mOnlistClick=listener;
    }


    @Override
    public SupervisorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_supervisor, parent, false);
        return new SupervisorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SupervisorViewHolder holder, int position) {
        SupervisorOperacao supevisor = this.ListSupervisor.get(position);
        holder.bindDate(supevisor,mOnlistClick,position);

    }

    @Override
    public int getItemCount() {
        return ListSupervisor.size();
    }





}
