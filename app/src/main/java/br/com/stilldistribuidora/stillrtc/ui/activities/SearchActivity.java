package br.com.stilldistribuidora.stillrtc.ui.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.views.RegisterPartnersActivity;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.PaternsModel;
import br.com.stilldistribuidora.stillrtc.interfac.OnClickList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private ApiConfig apiConfig;
    private List<String> address = new ArrayList<>();
    private Call<ResponseBody> responseBodyCall=null;
    private ApiEndpointInterface apiService=null;
    private RecyclerView mRecyclerViewGrains;
    private AddressAdapter mAdapter;
    private OnClickList onClickList;
    private Context self;
    private PaternsModel paternsModel;
    private ProgressBar load;
    private TextView textProcess;
    private ConstraintLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_ctivity);

        paternsModel = loadInfoToPaternsData();
        self = this;

        this.load = findViewById(R.id.loads);
        this.container = findViewById(R.id.container);
        this.textProcess = findViewById(R.id.textProcess);

        this.container.requestFocus();
        this.apiConfig = new ApiConfig(this);

        if (apiConfig.Api.getEndPointAccess() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson() == null ||
                apiConfig.Api.getEndPointAccess().getDataJson().isEmpty()) {
            Intent intent = new Intent(this, InitAppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        apiService = ApiClient
            .getClient(apiConfig.Api.getEndPointAccess().getDataJson().trim())
            .create(ApiEndpointInterface.class);
        
        // Configurando o gerenciador de layout para ser uma lista.
        mRecyclerViewGrains = findViewById(R.id.recycler_view_layour_recycler);
        
        EditText editText= findViewById(R.id.textSearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                processando(true);
                info("",false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (responseBodyCall !=null) responseBodyCall.cancel();

                    address.clear();
                    if(!editable.toString().isEmpty()){
                        responseBodyCall = apiService.searchingByAddress(
                                apiConfig.Api.getEndPointAccessToken().getDataJson(),
                                editable.toString());

                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try{
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            String rawData = response.body().string();
                                            JSONArray jsonObject = new JSONObject(rawData).getJSONArray("locations");
                                            List<String> address= new ArrayList<>();
                                            for(int next=0;next < jsonObject.length();next++){
                                                address.add(jsonObject.getJSONObject(next).getString("endereco"));
                                            }
                                            OnClickList onClickList = new OnClickList() {
                                                @Override
                                                public void onClick(Object object) {
                                                    loafActivityRegister(object);
                                                }
                                            };
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                                            mRecyclerViewGrains.setLayoutManager(layoutManager);
                                            mAdapter = new AddressAdapter(address,onClickList);
                                            mRecyclerViewGrains.setAdapter(mAdapter);

                                            processando(false);
                                            if(address.size()==0)
                                                info(getString(R.string.notAddress), true);
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
                    }else{
                        onClickList = new OnClickList() {
                            @Override
                            public void onClick(Object object) {}
                        };

                        processando(false);
                        address.clear();
                        info("Digite seu endereço acima",true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                        mRecyclerViewGrains.setLayoutManager(layoutManager);
                        mAdapter = new AddressAdapter(address,onClickList);
                        mRecyclerViewGrains.setAdapter(mAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void info(String info,boolean ativo){
        if (ativo) {
            textProcess.setText(info);
            textProcess.setVisibility(View.VISIBLE);
        } else {
            textProcess.setText(info);
            textProcess.setVisibility(View.GONE);
        }
    }

    private void processando(boolean ativo){
        if (ativo) {
            this.load.setVisibility(View.VISIBLE);
            this.mRecyclerViewGrains.setVisibility(View.GONE);
        } else {
            this.load.setVisibility(View.GONE);
            this.mRecyclerViewGrains.setVisibility(View.VISIBLE);
        }
    }
    
    private void loafActivityRegister(Object object) {
        Bundle bd= (Bundle)object;
        Bundle bundle = new Bundle();
        bundle.putString("EXTRA_INFO_ADDRESS","");
        bundle.putString("name",paternsModel.getName());
        bundle.putString("lastName",paternsModel.getLastName());
        bundle.putString("cpf",paternsModel.getCpf());
        bundle.putString("mobliePhone",paternsModel.getMobliePhone());
        bundle.putString("email",paternsModel.getEmail());
        bundle.putString("password",paternsModel.getPassword());
        bundle.putString("address",bd.getString("EXTRA_INFO_ADDRESS"));
        bundle.putString("pix",paternsModel.getPixJson());

        Intent intent = new Intent(SearchActivity.this, RegisterPartnersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                Bundle bd = new Bundle();
                bd.putString("EXTRA_INFO_ADDRESS",paternsModel.getAddress());
                loafActivityRegister(bd);
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }
    
    private PaternsModel loadInfoToPaternsData() {
        PaternsModel paternsModel = new PaternsModel();
        if (getIntent().hasExtra("name")) {
            paternsModel.setName(getIntent().getStringExtra("name"));
        }

        if (getIntent().hasExtra("lastName")) {
            paternsModel.setLastName(getIntent().getStringExtra("lastName"));
        }

        if (getIntent().hasExtra("cpf")) {
            paternsModel.setCpf(getIntent().getStringExtra("cpf"));
        }
        
        if (getIntent().hasExtra("mobliePhone")) {
            paternsModel.setMobliePhone(getIntent().getStringExtra("mobliePhone"));
        }

        if (getIntent().hasExtra("email")) {
            paternsModel.setEmail(getIntent().getStringExtra("email"));
        }

        if (getIntent().hasExtra("password")) {
            paternsModel.setPassword(getIntent().getStringExtra("password"));
        }

        if (getIntent().hasExtra("address")) {
            paternsModel.setAddress(getIntent().getStringExtra("address"));
        }

        if (getIntent().hasExtra("pix")) {
            paternsModel.setPixJson(getIntent().getStringExtra("pix"));
        }

        return paternsModel;
    }

    public static class RowAddress extends RecyclerView.ViewHolder {
        public TextView title, amont;
        public ImageView foto;
        public Button btnSelection;
        public OnClickList onClickList;
        public void RowAddress(OnClickList onClickList){
            this.onClickList=onClickList;
        }
        public RowAddress(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.main_line_title);
            btnSelection= itemView.findViewById(R.id.btnSelection);
        }
    }

    public static class AddressAdapter extends RecyclerView.Adapter<RowAddress> {
        private final OnClickList onClickList;
        private List<String> address = new ArrayList<>();

        public AddressAdapter(List<String> address,OnClickList onClickList){
            this.onClickList=onClickList;
            this.address=address;
        }

        @NonNull
        @Override
        public RowAddress onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RowAddress(
                LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.address_rows_view,parent,false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RowAddress holder, int position) {
            int _position=position;
            holder.title.setText(address.get(position));
            holder.btnSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bd =new Bundle();
                    bd.putString("EXTRA_INFO_ADDRESS", address.get(_position));
                    onClickList.onClick(bd);
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.address.size();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class ApiConfig {
        private final AppCompatActivityBase Api;
        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterPartnersActivity.class);
        startActivity(intent);
    }
}
