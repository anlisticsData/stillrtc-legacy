package br.com.stilldistribuidora.stillrtc.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.Movimentos;
import br.com.stilldistribuidora.stillrtc.interfac.ClickRecyclerView_Interface;

public class MovimentosViewHolder extends RecyclerView.Adapter<MovimentosViewHolder.MovimentosPersonalizados> {
    public static ClickRecyclerView_Interface clickRecyclerViewInterface;
    public static Context mctx;
    List<Movimentos> movimentos;



    public MovimentosViewHolder(Context self, List<Movimentos>movimentos, ClickRecyclerView_Interface clickRecyclerViewInterface) {
        this.movimentos = movimentos;
        this.mctx = self;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
    }


    @Override
    public MovimentosViewHolder.MovimentosPersonalizados onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_movimentos, viewGroup, false);
       return new MovimentosPersonalizados(v);
    }

    @Override
    public void onBindViewHolder(MovimentosViewHolder.MovimentosPersonalizados holder, int i) {
        final int poss = i;
        holder.grf_txt_qt__entregue.setVisibility(View.GONE);
        holder.grf_txt_qt__entregue_label.setVisibility(View.GONE);

        holder.grf_txt_operacao.setText("OPERAÇAO : "+movimentos.get(i).getMv_codigo());
        holder.grf_txt_cl_empresa.setText("Client/Empresa : "+movimentos.get(i).getEm_nome()+"("+movimentos.get(i).getCl_nome()+")");
        holder.grf_txt_material_formato.setText("Material: "+movimentos.get(i).getMt_nome());
        holder.grf_txt_qt_a_retirar.setText(movimentos.get(i).getMv_qt_retirar());
        holder.grf_txt_data_creat.setText(movimentos.get(i).getMv_create_at());
        if(movimentos.get(i).getMv_startus().equals("A")) {

            holder.grf_txt_status.setText("PENDENTE");

        }else if(movimentos.get(i).getMv_startus().equals("T")) {
            holder.grf_txt_status.setTextColor(Color.GREEN);
            holder.grf_txt_status.setText("TRANSPORTE");
        }else{
            holder.grf_txt_status.setTextColor(Color.BLUE);
            holder.grf_txt_status.setText("ENTREGUE");

            if(!movimentos.get(i).getMv_qt_entregue().trim().isEmpty()) {
                holder.grf_txt_qt__entregue.setVisibility(View.VISIBLE);
                holder.grf_txt_qt__entregue_label.setVisibility(View.VISIBLE);
                holder.grf_txt_qt__entregue.setText(movimentos.get(i).getMv_qt_entregue());


            }

        }







    }

    @Override
    public int getItemCount() {
        return  movimentos.size();
    }


    protected   class MovimentosPersonalizados extends RecyclerView.ViewHolder {


        private final TextView grf_txt_operacao;
        private final TextView grf_txt_cl_empresa;
        private final TextView grf_txt_material_formato;
        private final TextView grf_txt_qt_a_retirar;
        private final TextView grf_txt_status;
        private  final TextView grf_txt_data_creat;
        private  final TextView grf_txt_qt__entregue;
        private  final TextView grf_txt_qt__entregue_label;





        public MovimentosPersonalizados(@NonNull View itemView) {
            super(itemView);
            grf_txt_operacao = (TextView) itemView.findViewById(R.id.grf_txt_operacao);
            grf_txt_cl_empresa = (TextView) itemView.findViewById(R.id.grf_txt_cl_empresa);
            grf_txt_material_formato = (TextView) itemView.findViewById(R.id.grf_txt_material_formato);
            grf_txt_qt_a_retirar = (TextView) itemView.findViewById(R.id.grf_txt_qt_a_retirar);
            grf_txt_status = (TextView) itemView.findViewById(R.id.grf_txt_status);
            grf_txt_data_creat = (TextView) itemView.findViewById(R.id.grf_txt_data_creat);
            grf_txt_qt__entregue = (TextView) itemView.findViewById(R.id.grf_txt_qt__entregue);
            grf_txt_qt__entregue_label = (TextView) itemView.findViewById(R.id.grf_txt_qt__entregue_label);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecyclerViewInterface.onCustomClick(movimentos.get(getLayoutPosition()));
                }
            });




        }


    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
