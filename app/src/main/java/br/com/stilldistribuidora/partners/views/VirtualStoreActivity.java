package br.com.stilldistribuidora.partners.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Base.ActivityBaseApp;
import br.com.stilldistribuidora.partners.Casts.Prizes;
import br.com.stilldistribuidora.partners.Casts.PrizesCast;
import br.com.stilldistribuidora.partners.Casts.Produto;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCast;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.interfac.OnActionParentActivity;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;
import br.com.stilldistribuidora.stillrtc.services.DownloadImageTask;

public class VirtualStoreActivity extends ActivityBaseApp {
    private static final Components components = new Components();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_store);

        InitPermisionGeral();
        components.contextActivity = this;
        components.conf = new ServerConfiguration(components.contextActivity);
        components.appOpeationsRepository = new AppOpeationsRepository(components.contextActivity);
        components.servicesHttp = new ServicesHttp(this, components.conf, CallBackOnResponseGlobalApi());

        String userLogged = components.conf.getUserLogged();
        components.userLoginCast = new Gson().fromJson(userLogged, UserLoginCast.class);

        components.stillcoins = findViewById(R.id.stillCoins);

        components.txt_message = findViewById(R.id.txt_message);
        components.recycler_view_layour_productions = findViewById(R.id.recycler_view_layour_productions);
        components.layoutManager = new LinearLayoutManager(components.contextActivity);
        components.recycler_view_layour_productions.setLayoutManager(components.layoutManager);
        components.btnFinish = findViewById(R.id.btnFinish);
        components.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(this));

        initGradeProductor();
        loadStore();
        initOnClickButtonsEvents();
        updateInfoUser();
    }

    private void updateInfoUser() {
        Locale localeBR = new Locale("pt","BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeBR);
        String show = numberFormat.format(components.configurationsAdapter.getStillCoins());
        components.stillcoins.setText(show.replace("R$", " SK$ "));
    }

    private void initOnClickButtonsEvents() {
        ProgressDialog progressDialog = new ProgressDialog(components.contextActivity);
        components.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (components.shoppingCart.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                    builder.setMessage("Seu carrinho está vazio").setTitle(R.string.dialog_message);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                builder.setMessage(String.format("Você tem %s prêmio(s) para retirar.", components.shoppingCart.size())).setTitle(R.string.dialog_message);
                builder.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressDialog.setTitle("Processando...");
                                progressDialog.setMessage("Confirmando seu Pedido...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                OnResponseHttp onResponseHttp = new OnResponseHttp() {
                                    @Override
                                    public void onResponse(Object data) {
                                        ResponseBaseCast response = (ResponseBaseCast) data;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                                        builder.setMessage(response.msn).setTitle(R.string.dialog_message);
                                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                double balance = components.configurationsAdapter.getStillCoins() - components.stillCoinsUsed;
                                                components.shoppingCart.clear();
                                                components.stillCoinsUsed = 0;
                                                components.configurationsAdapter.updateStillCoins(balance);
                                                finish();
                                                progressDialog.dismiss();

                                            }
                                        });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                    @Override
                                    public void onError(Object error) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                                        builder.setMessage((String) error).setTitle(R.string.dialog_message);
                                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                                updateInfoUser();
                                                progressDialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                };

                                StringBuffer prizesSeparatedPipe = new StringBuffer();
                                for (Prizes prize : components.shoppingCart) {
                                    prizesSeparatedPipe.append(prize.id);
                                    prizesSeparatedPipe.append(Resources.PIPE);
                                }
                                components.servicesHttp.withdrawPrize(
                                        Integer.valueOf(components.userLoginCast.getUuid()),
                                        components.stillCoinsUsed, prizesSeparatedPipe.toString(), onResponseHttp);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                components.shoppingCart.clear();
                                components.stillCoinsUsed = 0;
                                progressDialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    private void loadStore() {
        components.prizes.clear();
        OnResponseHttp onResponseHttp = new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {
                PrizesCast prizes = (PrizesCast) data;
                for (Prizes prize : prizes.prizes) {
                    components.prizes.add(prize);
                }
                initGradeProductor();
            }

            @Override
            public void onError(Object error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                builder.setMessage((String) error).setTitle(R.string.dialog_message);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                components.servicesHttp.getPrizesLeveBy(components.userLoginCast.getNivelId(), components.pager, components.limit, onResponseHttp);
            }
        }).start();
    }

    private void initGradeProductor() {
        if (components.prizes.size() == 0) {
            components.recycler_view_layour_productions.setVisibility(View.GONE);
            components.txt_message.setVisibility(View.VISIBLE);
            return;
        }
        components.txt_message.setVisibility(View.GONE);
        components.recycler_view_layour_productions.setVisibility(View.VISIBLE);
        /*Eventos Grade*/
        OnClick onClick = new OnClick() {
            @Override
            public void onClick(Object obj) {}
        };

        OnActionParentActivity onActionParentActivity = new OnActionParentActivity() {
            @Override
            public void onStartActivity(String type, Object objetct) {}
        };
        /*Eventos Grade*/
        components.recycler_view_layour_productions.setAdapter(new LineProductorStore(components.prizes, onClick, onActionParentActivity));
    }

    private OnResponseHttp CallBackOnResponseGlobalApi() {
        OnResponseHttp onResponseHttp = new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {
            }

            @Override
            public void onError(Object error) {
            }
        };
        return onResponseHttp;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onHome (View view) {
        startActivity(new Intent(components.contextActivity, HomeActivity.class));
    }

    /*Lista de Produtos*/
    public static class PrizeRow extends RecyclerView.ViewHolder {
        public PrizeRow(View itemView) {
            super(itemView);

        }
    }

    public static class LineProductorStore extends RecyclerView.Adapter<PrizeRow> {

        private final OnActionParentActivity onClickEventsParents;
        private final OnClick onClick;
        private List<Prizes> prizes = new ArrayList<>();
        private ImageView imgPrize;
        private TextView txtDescription, txtstillcoinssk;
        private CardView cardPrize;
        private Button btnpurchase;
        private CheckBox chkselect;


        public LineProductorStore(List<Prizes> products, OnClick onClick, OnActionParentActivity onClickEventsParents) {
            this.prizes = products;
            this.onClick = onClick;
            this.onClickEventsParents = onClickEventsParents;
            notifyDataSetChanged();
        }

        @Override
        public PrizeRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PrizeRow(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rows_productor, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PrizeRow holder, @SuppressLint("RecyclerView") int position) {


            txtDescription = holder.itemView.findViewById(R.id.txtDescription);
            txtstillcoinssk = holder.itemView.findViewById(R.id.stillcoinssk);
            cardPrize = holder.itemView.findViewById(R.id.cardPrize);
            imgPrize = holder.itemView.findViewById(R.id.imgPrize);
            btnpurchase = holder.itemView.findViewById(R.id.btnpurchase);
            chkselect = holder.itemView.findViewById(R.id.chkselect);
            txtDescription.setText(this.prizes.get(position).premios);
            this.getImageServer(imgPrize, this.prizes.get(position).photo);

            Float sks = Float.parseFloat(this.prizes.get(position).sks);
            Locale localeBR = new Locale("pt","BR");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeBR);
            String show = numberFormat.format(sks);
            txtstillcoinssk.setText(show.replace("R$", " SK$ "));



            /*String sks = this.prizes.get(position).sks;
            Double valorReal = Double.valueOf(sks);

            *//*
            DecimalFormat df = new DecimalFormat("0.##");
            String valorT = df.format(valorReal);

             *//*

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);


            txtstillcoinssk.setText(String.format("SK$ %s ",df.format(valorReal)));*/
            chkselect.setVisibility(View.VISIBLE);
            chkselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    double openingBalance = components.configurationsAdapter.getStillCoins();
                    double saldo = (openingBalance - components.stillCoinsUsed);
                    double itemValor = Double.valueOf(prizes.get(position).sks);

                    if (isChecked) {
                        if (saldo <= itemValor) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(components.contextActivity);
                            builder.setMessage("Saldo insuficiente").setTitle(R.string.dialog_message);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            buttonView.setChecked(false);
                            return;

                        }
                        components.shoppingCart.add(prizes.get(position));
                    } else {
                        components.shoppingCart.remove(prizes.get(position));
                    }
                    updateCarShop();
                }
            });


            Double stillCoins = components.configurationsAdapter.getStillCoins();
            Double stillCoinsValue = Double.valueOf(prizes.get(position).sks);

            if (stillCoins < stillCoinsValue) {
                chkselect.setVisibility(View.GONE);
                System.out.println("#12 " + prizes.get(position).id + "    " + stillCoinsValue + "  " + stillCoins);

            }


        }


        @Override
        public int getItemCount() {
            return prizes != null ? prizes.size() : 0;
        }

        private void getImageServer(ImageView imgPrize, String photo) {
            try {
                File file = new File(photo);
                if (file.exists()) {
                    imgPrize.setImageURI(Uri.parse(file.toString()));
                } else {
                    if (!photo.trim().isEmpty()) {
                        byte[] data = Base64.decode(photo, Base64.DEFAULT);
                        String text = new String(data, StandardCharsets.UTF_8);
                        if (text.equals(Resources.DEFAULT_IMAGE)) {
                            imgPrize.setImageResource(R.drawable.semfoto);
                        } else {
                            new DownloadImageTask(imgPrize).execute(text);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("#4 " + e.getMessage());
            }

        }

        private void updateCarShop() {
            Double total = 0.0;
            Double stillCoins = components.configurationsAdapter.getStillCoins();
            for (Prizes prize : components.shoppingCart) {
                total += Double.valueOf(prize.sks);
            }
            double stillCoinsused = total;
            float f = Math.round(stillCoinsused * 100.0f) / 100.0f;
            components.stillCoinsUsed = f;
            components.stillcoins.setText(String.format("Sk$ \n %.2f ", stillCoins - f));
            if (components.shoppingCart.size() > 0) {
                components.btnFinish.setVisibility(View.VISIBLE);
            } else {
                components.btnFinish.setVisibility(View.GONE);
            }
            Toast.makeText(components.contextActivity, Resources.UPDATED_CAR, Toast.LENGTH_LONG).show();
        }
    }

    private static class Components {
        public List<Produto> produtos = new ArrayList<>();
        public List<Prizes> prizes = new ArrayList<>();
        public List<Prizes> shoppingCart = new ArrayList<>();
        public double stillCoinsUsed = 0;

        public ServicesHttp servicesHttp;
        public UserLoginCast userLoginCast;
        public ServerConfiguration conf = null;
        public Context contextActivity;
        public AppOpeationsRepository appOpeationsRepository;
        public Bitmap image;
        public TextView stillcoins;
        public TextView txtNivel;
        public TextView txt_message;
        public RecyclerView recycler_view_layour_productions;
        public LinearLayoutManager layoutManager;
        public int pager = 1, limit = 50;
        public Button btnFinish;
        public ConfigurationsUser configurationsAdapter;
        public Toolbar toolbar;
        public Locale localBrasil = new Locale("pt", "BR");

    }


}