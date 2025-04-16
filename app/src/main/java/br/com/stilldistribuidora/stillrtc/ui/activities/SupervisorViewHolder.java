package br.com.stilldistribuidora.stillrtc.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.models.OnListClickInteractionListener;
import br.com.stilldistribuidora.stillrtc.db.models.StatisticDispositivo;
import br.com.stilldistribuidora.stillrtc.db.models.SupervisorOperacao;
import br.com.stilldistribuidora.stillrtc.services.DownloadImageTask;

/**
 * Created by Still Technology and Development Team on 08/08/2018.
 */

public class SupervisorViewHolder extends RecyclerView.ViewHolder {
    private View context;
    private ImageView img_navegar;
    private TextView txt_descricao;
    private TextView txt_dispFinalizados;
    private final ImageView img_loja;
    private ImageView img_maps_supervisor;
    private TextView quantidadeDispositivos;
    private TextView keyOperation;
    private TextView txt_ativos;
    private TextView txt_finalOsioso;
    private TextView txt_destaqueOperation;


    public SupervisorViewHolder(View itemView) {
        super(itemView);
        context = itemView;

        this.txt_descricao = (TextView) itemView.findViewById(R.id.sup_descricao);
        this.img_loja = (ImageView) itemView.findViewById(R.id.sup_img_client);
        this.quantidadeDispositivos = (TextView) itemView.findViewById(R.id.sup_qt_dispositivo);
        this.txt_ativos = (TextView) itemView.findViewById(R.id.sup_disp_ativos);
        this.keyOperation = (TextView) itemView.findViewById(R.id.sup_key_operation);
        this.img_maps_supervisor = (ImageView) itemView.findViewById(R.id.sup_img_maps);
        this.txt_dispFinalizados = (TextView) itemView.findViewById(R.id.sup_disp_finalizados);
        this.img_navegar = (ImageView) itemView.findViewById(R.id.sup_navegar_points);
        this.txt_destaqueOperation = (TextView) itemView.findViewById(R.id.str_string_pendent);
        this.txt_finalOsioso=(TextView) itemView.findViewById(R.id.sup_txt_finalizar);
    }

    public void bindDate(final SupervisorOperacao supevisor, final OnListClickInteractionListener listener, final int position) {
        int dispositivoAtivos = 0, dispositivoFinalizados = 0, dispositivo_osioso = 0;
        String[] diferenciaUltimoPing = new String[3];
        this.img_maps_supervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick("", position);
            }
        });
        this.img_navegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick("ULTIMA_ROTA", position);
            }
        });
        //set the ontouch listener
        this.img_maps_supervisor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
            }
        });
        if (Integer.parseInt(supevisor.statusOperacao) == 0) {
            this.txt_destaqueOperation.setText(R.string.str_pending);
            this.txt_destaqueOperation.setTextColor(context.getResources().getColor(R.color.colorStatusDanger));
            this.txt_destaqueOperation.setTextSize(12);


        } else if (Integer.parseInt(supevisor.statusOperacao) == 1) {
            this.txt_destaqueOperation.setText(R.string.str_in_progress);
            this.txt_destaqueOperation.setTextColor(context.getResources().getColor(R.color.colorStatusSuccess));
            this.txt_destaqueOperation.setTextSize(25);


        } else {
            this.txt_destaqueOperation.setText(R.string.str_finalized);
            this.txt_destaqueOperation.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            this.txt_destaqueOperation.setTextSize(15);
        }
        this.txt_descricao.setText(supevisor.clienteLoja);


        if (!supevisor.avatar.isEmpty()) {
            new DownloadImageTask(img_loja).execute(supevisor.avatar);
        } else {
            Bitmap icon = BitmapFactory.decodeResource(itemView.getResources(),
                    R.drawable.ic_loja_company);
            this.img_loja.setImageBitmap(icon);
        }


        dispositivoFinalizados = 0;
        dispositivoAtivos = 0;
        dispositivo_osioso = 0;

        String copyOperation = supevisor.operacao_id;
        this.keyOperation.setText("#" + supevisor.operacao_id);

        if (supevisor.Dispositivos.size() > 0) {
            if (Integer.valueOf(supevisor.statusOperacao) == 2) {
                for (StatisticDispositivo dis : supevisor.Dispositivos) {
                    if (dis.operation.trim().equals(supevisor.operacao_id)) {
                        dispositivoFinalizados++;
                    }
                }
                this.quantidadeDispositivos.setText(String.valueOf(supevisor.quantideDispositivoOperacao));

                this.keyOperation.setText("#" + supevisor.operacao_id);
                this.txt_ativos.setText(String.valueOf(dispositivoAtivos));
                this.txt_finalOsioso.setText("Final.");
                this.txt_dispFinalizados.setText(String.valueOf(Math.abs(dispositivoFinalizados)));

            } else {
                String[] horaOciosa = new String[3];
                for (StatisticDispositivo dis : supevisor.Dispositivos) {
                    if (dis.operation.trim().equals(supevisor.operacao_id)) {
                        dispositivoAtivos++;
                        //Se Tive Ocioso..
                        horaOciosa = dis.ociosidade.trim().split(":");
                        if ((Integer.parseInt(horaOciosa[0])-Constants.FUSO_HORA ) > 0) {
                            dispositivo_osioso++;
                        }else if ((Integer.parseInt(horaOciosa[1])) > Constants.TEMPO_DISP_OSIOSO) {
                            dispositivo_osioso++;
                        }
                        //Se Tive Ocioso..

                    }
                }

                //Adiciona na Tela

                this.quantidadeDispositivos.setText(String.valueOf(supevisor.quantideDispositivoOperacao));
                this.keyOperation.setText("#" + supevisor.operacao_id);
                this.txt_ativos.setText(String.valueOf(dispositivoAtivos));
                this.txt_finalOsioso.setText("Ocioso.");
                this.txt_dispFinalizados.setText(String.valueOf(Math.abs(dispositivo_osioso)));

            }
        }

    }


}