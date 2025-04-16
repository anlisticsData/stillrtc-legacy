package br.com.stilldistribuidora.stillrtc.ui.adapters;

/**
 * Created by Still Technology and Development Team on 23/04/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.interfac.IClickListSimultaneas;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OperationAdapterSimultaneas extends RecyclerView.Adapter<OperationAdapterSimultaneas.ViewHolderItem>  implements IClickListSimultaneas {

    private Activity context;
    private int lastPosition = -1;
    private Boolean empty;
    private int pos;
    private boolean loading;
    private Operation.Result phraseResult;
    private OperationBusiness deliveryFragmentBusiness;
    private PrefsHelper prefsHelper;
    private int deviceId;
    private int lastSelectedPosition = -1;  // declare this variable
    private ArrayList<String> keys= new ArrayList<>();
    private IClickListSimultaneas onActionSelects;
    private ApiConfig appConfig;

    public static class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }



    public OperationAdapterSimultaneas(Activity context, ArrayList<Operation> phraseArrayList, Boolean empty,IClickListSimultaneas onActionSelects) {
        this.context = context;
        appConfig=new ApiConfig(context);

        this.empty = empty;
        this.phraseResult = new Operation.Result();
        this.phraseResult.ar = phraseArrayList;
        this.onActionSelects = onActionSelects;

        deliveryFragmentBusiness = new OperationBusiness(context);

        prefsHelper = new PrefsHelper(context);

        if(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_ID) != null){
            deviceId = prefsHelper.getPref(PrefsHelper.PREF_DEVICE_ID);
        } else {
            deviceId = 0;
        }

    }

    public void update(Boolean empty, int pos) {

        this.empty = empty;
        this.pos = pos;
        notifyDataSetChanged();
    }

    public void updateArraySearch(ArrayList<Operation> designs) {
        this.phraseResult = new Operation.Result();
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
    public OperationAdapterSimultaneas.ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empty, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_delivery_simultanio, parent, false);
        }
        return new OperationAdapterSimultaneas.ViewHolderItem(v);
    }



    @Override
    public void onBindViewHolder(final OperationAdapterSimultaneas.ViewHolderItem customViewHolder, final int position) {
        final Operation feedItem = phraseResult.ar.get(position);
        String dtDaOperacaoAtiva="";


        System.out.println("####$"+feedItem.getDeliveryId());


        if (this.empty) {
             customViewHolder.tvTitle.setText(feedItem.getAreaName());
        } else {

            customViewHolder.tvTitle.setText(context.getString(R.string.str_operation)+": "+feedItem.getDeliveryId());
            customViewHolder.tvStore.setText(context.getString(R.string.str_client_store)+": "+feedItem.getClientName()+" ("+feedItem.getStoreName()+")");
            customViewHolder.tvOperation.setText(context.getString(R.string.str_area)+": "+feedItem.getAreaName());
            customViewHolder.tvGroup.setText(context.getString(R.string.str_group)+": "+feedItem.getGroup());


            String[] timeCriatedAt = feedItem.getCreatedAt().substring(0).split(" ");
            String[] timeCriatedAtHora=timeCriatedAt[1].toString().split(":");
            int Hora = Integer.valueOf(timeCriatedAtHora[0]);
            timeCriatedAtHora[0] =String.format("%02d", Hora-3);
            String timeCriateAtBr=timeCriatedAt[0]+"  "+timeCriatedAtHora[0]+":"+timeCriatedAtHora[1]+":"+timeCriatedAtHora[2]+"";
            customViewHolder.tvCreatedAt.setText(timeCriateAtBr);


            if(!keys.contains(String.valueOf(feedItem.getDeliveryId()))){

                // customViewHolder.itemView.setBackgroundColor(true ? Color.BLACK : Color.WHITE);
                customViewHolder.tvselecionado.setText("");
                customViewHolder.tvselecionado.setBackgroundColor(Color.parseColor("#FFFFFF"));


            }else{
                //customViewHolder.itemView.setBackgroundColor(false ? Color.BLACK : Color.WHITE);

                customViewHolder.tvselecionado.setText("SELECIONADO");
                customViewHolder.tvselecionado.setBackgroundColor(Color.parseColor("#BABABA"));
            }


          try {
                if (getStatus(feedItem.getDeliveryFragmentId()) > 0) {
                    customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    customViewHolder.tvStatus.setText(context.getString(R.string.str_finalized).toUpperCase());
                } else {
                    if(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_ID) !=null) {
                        if(Constants.IS_USER_IDENTIFIER.equals("D")){
                            customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorStatusWarning));
                            customViewHolder.tvStatus.setText(context.getString(R.string.str_pending).toUpperCase());
                        }else{
                            //Faz a Check do Status da Operação
                            CheckStatusOperation_(customViewHolder,String.valueOf(feedItem.getDeliveryId()),timeCriatedAt[0]);
                            //Faz a Check do Status da Operação
                        }
                    } else {
                        if(Constants.IS_USER_IDENTIFIER.equals("C")){
                            //Faz a Check do Status da Operação
                            CheckStatusOperation_(customViewHolder,String.valueOf(feedItem.getDeliveryId()),timeCriatedAt[0]);
                            //Faz a Check do Status da Operação

                        }else {

                            customViewHolder.tvStatus.setVisibility(View.GONE);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Intent intent;
                    try {
                        int deliveryFragmentId = feedItem.getDeliveryFragmentId();
                        if(!keys.contains(String.valueOf(deliveryFragmentId))){
                            keys.add(String.valueOf(deliveryFragmentId));
                           // customViewHolder.itemView.setBackgroundColor(true ? Color.BLACK : Color.WHITE);
                            customViewHolder.tvselecionado.setText("SELECIONADO");
                            customViewHolder.tvselecionado.setBackgroundColor(Color.parseColor("#BABABA"));


                        }else{
                            //customViewHolder.itemView.setBackgroundColor(false ? Color.BLACK : Color.WHITE);
                            keys.remove(String.valueOf(deliveryFragmentId));
                            customViewHolder.tvselecionado.setText("");
                            customViewHolder.tvselecionado.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }

                        System.out.println(feedItem);
                        
                        /*
                        if (context.getClass() == OpslistagenActivity.class) {
                            intent = new Intent(context, MapWebViewActivity.class);
                        } else {
                            if (getStatus(feedItem.getDeliveryFragmentId()) > 0) {
                                intent = new Intent(context, OperationDetailsActivity.class);
                            } else {
                                intent = new Intent(context, MapsActivity.class);
                            }
                        }
                        intent.putExtra("object", phraseResult);
                        intent.putExtra("position", position);
                        */

                        /*
                        context.startActivity(intent);
                        context.overridePendingTransition(0, 0);

                        */

                        //Toast.makeText(context,String.valueOf(keys.size()), Toast.LENGTH_SHORT).show();

                        onActionSelects.initOperacoesSelecionadas(keys);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }






    public void CheckStatusOperation_(final OperationAdapterSimultaneas.ViewHolderItem customViewHolder, String idDelyvery, String dtcAoperacao) {

        ApiEndpointInterface apiService =
                ApiClient.getClient(appConfig.Api.getEndPointAccess().getDataJson()).
                        create(ApiEndpointInterface.class);

        Call<ResponseBody> call = apiService.checkoutOperacaoStatus(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                idDelyvery,dtcAoperacao);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null  ) {
                    try {
                        String bodyString = response.body().string();
                        JSONObject jsonObj = null;
                        jsonObj = new JSONObject(bodyString);

                        JSONArray contacts = jsonObj.getJSONArray("apps");


                        if(jsonObj.getBoolean("error")) {


                            JSONObject c = contacts.getJSONObject(0);
                            int resStatusDaOperacao = Integer.parseInt(c.getString("statusOperacao"));
                            Log.i("REQ", "OOO" + c.getString("statusOperacao"));

                            if (resStatusDaOperacao == 0) {
                                customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorStatusWarning));
                                customViewHolder.tvStatus.setText(context.getString(R.string.str_pending).toUpperCase());
                            } else if (resStatusDaOperacao == 1) {
                                customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorStatusSuccess));
                                customViewHolder.tvStatus.setText(context.getString(R.string.str_in_progress).toUpperCase());
                            } else if (resStatusDaOperacao == 2) {
                                customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                customViewHolder.tvStatus.setText(context.getString(R.string.str_finalized).toUpperCase());
                            } else if (resStatusDaOperacao == -1) {
                                customViewHolder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                customViewHolder.tvStatus.setText(context.getString(R.string.str_finalized).toUpperCase() + "*");
                            }

                        }



                    }catch (JSONException e){
                        //Toast.makeText(context.getBaseContext(),"Erro :"+e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                       // Toast.makeText(context.getBaseContext(),"Erro :"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else{
                   // Toast.makeText(context.getBaseContext(),"Não há Informação..!",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
             //  Toast.makeText(context.getBaseContext(),"Função : CheckStatusOperation****"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }





    @Override
    public int getItemCount() {
        return (null != phraseResult.ar ? phraseResult.ar.size() : 0);
    }



    @Override
    public void initOperacoesSelecionadas(ArrayList<String> s) {

    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {

        TextView tvselecionado;
        TextView tvTitle, tvOperation, tvStore, tvGroup, tvCreatedAt;
        TextView tvStatus;

        ViewHolderItem(View view) {
            super(view);

            this.tvselecionado = (TextView) view.findViewById(R.id.tvSelecionado);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            this.tvOperation = (TextView) view.findViewById(R.id.tvOperation);
            this.tvStore = (TextView) view.findViewById(R.id.tvStore);
            this.tvGroup = (TextView) view.findViewById(R.id.tvGroup);
            this.tvCreatedAt = (TextView) view.findViewById(R.id.tvCreatedAt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && this.empty)
            return 0;
        return 1;
    }

    private int getStatus(int deliveryFragmentID){
        return  deliveryFragmentBusiness.getList(Constants.KEY_DELIVERY_FRAGMENT_ID + "=" + deliveryFragmentID+" AND "+Constants.KEY_DEVICE_ID + "="+deviceId, "").size();
    }














}