package br.com.stilldistribuidora.pco.adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.pco.Interface.ClickRecyclerView_Interface;
import br.com.stilldistribuidora.pco.db.dao.TblSincronizarDao;
import br.com.stilldistribuidora.pco.db.model.Movimentos;
import br.com.stilldistribuidora.pco.db.model.TblSincronizar;
import br.com.stilldistribuidora.stillrtc.R;


public class MovimentosViewHolder extends RecyclerView.Adapter<MovimentosViewHolder.MovimentosPersonalizados> {
    public static ClickRecyclerView_Interface clickRecyclerViewInterface;
    public static Context mctx;
    private final ClickRecyclerView_Interface clickRecyclerViewInterfacePause;
    private final ClickRecyclerView_Interface clickRecyclerViewInterfaceContinue;
    List<Movimentos> movimentos;
    private TblSincronizarDao sincronizarController;
    public List<Thread> times = new ArrayList<>();
    public Boolean ativo = true;







    public MovimentosViewHolder(Context self, List<Movimentos> movimentos,
                                ClickRecyclerView_Interface clickRecyclerViewInterface,
                                ClickRecyclerView_Interface clickRecyclerViewInterfacePause,
                                ClickRecyclerView_Interface clickRecyclerViewInterfaceContinue) {
        this.movimentos = movimentos;
        this.mctx = self;
        sincronizarController = new TblSincronizarDao(mctx);
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
        this.clickRecyclerViewInterfacePause = clickRecyclerViewInterfacePause;
        this.clickRecyclerViewInterfaceContinue = clickRecyclerViewInterfaceContinue;


    }


    @Override
    public MovimentosViewHolder.MovimentosPersonalizados onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_my_movimentos, viewGroup, false);
        return new MovimentosPersonalizados(v);
    }

    @Override
    public void onBindViewHolder(final MovimentosViewHolder.MovimentosPersonalizados holder, final int i) {
        final int poss = i;

        holder.grf_txt_qt__entregue.setVisibility(View.GONE);
        holder.grf_txt_qt__entregue_label.setVisibility(View.GONE);

        holder.grf_txt_operacao.setText("OPERAÇAO : " + movimentos.get(i).getMv_codigo());
        holder.grf_txt_cl_empresa.setText("Client/Empresa : " + movimentos.get(i).getEm_nome() + "(" + movimentos.get(i).getCl_nome() + ")");
        holder.grf_txt_material_formato.setText("Material: " + movimentos.get(i).getMt_nome());
        holder.grf_txt_qt_a_retirar.setText(movimentos.get(i).getMv_qt_retirar());
        holder.grf_txt_data_creat.setText(movimentos.get(i).getMv_create_at());


        if (movimentos.get(i).getMv_startus().equals("A")) {

            holder.grf_txt_status.setText("PENDENTE");
            holder.grf_lnl_time.setVisibility(View.GONE);

        } else if (movimentos.get(i).getMv_startus().equals("T")) {

            holder.grf_txt_status.setTextColor(Color.GREEN);
            holder.grf_txt_status.setText("TRANSPORTE");
            holder.grf_lnl_time.setVisibility(View.VISIBLE);
            holder.grf_txt_m_btn_pause_continue.setText("PAUSAR");
            holder.grf_m_img_display_pause_continue.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);


        } else if (movimentos.get(i).getMv_startus().equals("P")) {
            TblSincronizar t = new TblSincronizar();
            t.setCodigo_mv(movimentos.get(i).getMv_codigo());
            Object byTime1 = sincronizarController.getByTime(t);
            String timeDisplay = (String) byTime1;
            holder.grf_txt_m_time_display.setText(timeDisplay);
            //ic_pause_circle_filled_black  ic_play_circle_filled_black_24dp
            holder.grf_txt_m_btn_pause_continue.setText("CONTINUAR");
            holder.grf_m_img_display_pause_continue.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            holder.grf_txt_status.setTextColor(Color.RED);
            holder.grf_txt_status.setText("PAUSADO");
            holder.grf_lnl_time.setVisibility(View.VISIBLE);

        } else {

            holder.grf_txt_status.setTextColor(Color.BLUE);
            holder.grf_txt_status.setText("ENTREGUE");
            holder.grf_lnl_time.setVisibility(View.GONE);

            if (!movimentos.get(i).getMv_qt_entregue().trim().isEmpty()) {
                holder.grf_txt_qt__entregue.setVisibility(View.VISIBLE);
                holder.grf_txt_qt__entregue_label.setVisibility(View.VISIBLE);
                holder.grf_txt_qt__entregue.setText(movimentos.get(i).getMv_qt_entregue());


            }

        }







        Thread cronometro = new Thread(new Runnable() {
            @Override
            public void run() {


                while (true && ativo) {
                    try {
                        TblSincronizar t = new TblSincronizar();
                        System.out.println("##58 ---------------------");
                        try {
                            t.setCodigo_mv(movimentos.get(i).getMv_codigo());
                            Object byTime1 = sincronizarController.getByTime(t);
                            String timeDisplay = (String) byTime1;
                            System.out.println("##58 ---------------------CCCC2019 :" + movimentos.get(i).getMv_codigo() + "---" + timeDisplay);
                            if (timeDisplay == null) {
                                holder.grf_txt_m_time_display.setText("00:00:00");

                            } else if (timeDisplay.equals("")) {
                                holder.grf_txt_m_time_display.setText("00:00:00");

                            } else {
                                holder.grf_txt_m_time_display.setText(timeDisplay);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Thread.sleep(1 * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        });
        if (movimentos.get(i).getMv_startus().equals("T")) {
            cronometro.start();
            times.add(cronometro);
        }




    }


    private  class  Cronometro extends AsyncTask<Void,Void,Void>{

        public Cronometro(Context context){

        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }





    @Override
    public int getItemCount() {
        return movimentos.size();
    }


    protected class MovimentosPersonalizados extends RecyclerView.ViewHolder {


        private final TextView grf_txt_operacao;
        private final TextView grf_txt_cl_empresa;
        private final TextView grf_txt_material_formato;
        private final TextView grf_txt_qt_a_retirar;
        private final TextView grf_txt_status;
        private final TextView grf_txt_data_creat;
        private final TextView grf_txt_qt__entregue;
        private final TextView grf_txt_qt__entregue_label;
        private final TextView grf_txt_m_time_display;
        private final CardView grf_m_cdv_time;
        private final LinearLayout grf_lnl_time;
        private final Button grf_txt_m_btn_pause_continue;
        private final ImageView grf_m_img_display_pause_continue;


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
            grf_txt_m_time_display = (TextView) itemView.findViewById(R.id.grf_txt_m_time_display);
            grf_m_cdv_time = (CardView) itemView.findViewById(R.id.grf_m_cdv_time);
            grf_lnl_time = (LinearLayout) itemView.findViewById(R.id.grf_lnl_time);
            grf_txt_m_btn_pause_continue = (Button) itemView.findViewById(R.id.grf_txt_m_btn_pause_continue);
            grf_m_img_display_pause_continue = (ImageView) itemView.findViewById(R.id.grf_m_img_display_pause_continue);


            grf_txt_m_btn_pause_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ativo = false;
                    times.clear();

                    sincronizarController = new TblSincronizarDao(mctx);
                    TblSincronizar t = new TblSincronizar();
                    t.setCodigo_mv(movimentos.get(getLayoutPosition()).getMv_codigo());
                    Object retirada = sincronizarController.getBy(t);


                    String statusOperation = ((String) retirada);
                    String[] camposOperation = statusOperation.split(",");
                    ativo = false;
                    for (Thread time : times) {
                        try {
                            time.interrupt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    times.clear();


                    if (camposOperation[3].equals("P")) {
                        clickRecyclerViewInterfaceContinue.onCustomClick(movimentos.get(getLayoutPosition()));
                    } else {
                        clickRecyclerViewInterfacePause.onCustomClick(movimentos.get(getLayoutPosition()));
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ativo = false;
                    for (Thread time : times) {
                        try {
                            time.interrupt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    times.clear();
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

