package br.com.stilldistribuidora.partners.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.PaternsModel;

public class DadosBancariosActivity extends AppCompatActivity {
    private Spinner dropdown;
    private RadioButton pixAccount;
    private RadioButton banckAccount;
    private ConstraintLayout pixContent,bancoContent;
    private RadioButton termos_pix,termos_cc;
    private Button btnadvence;
    private PaternsModel paternsModel = new PaternsModel();
    private CheckBox checkbox_meat;
    private String TypeAccountUsed="";
    private StringBuffer Json=new StringBuffer();
    private  String[] items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_bancarios);
        loadDataReceived();

        btnadvence = findViewById(R.id.btnadvence);
        pixContent = findViewById(R.id.pixAccount);
        bancoContent = findViewById(R.id.bancoAccount);
        checkbox_meat = (CheckBox)findViewById(R.id.checkbox_meat);

        checkbox_meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked()){
                    btnadvence.setVisibility(View.VISIBLE);
                }else{
                    btnadvence.setVisibility(View.GONE);
                }
            }
        });

        this.termos_pix = (RadioButton)findViewById(R.id.radio_pix);
        this.termos_pix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pixContent.setVisibility(View.VISIBLE);
                bancoContent.setVisibility(View.GONE);
                TypeAccountUsed="PIX";
                checkbox_meat.setChecked(false);
                btnadvence.setVisibility(view.GONE);
            }
        });

        this.termos_cc=(RadioButton)findViewById(R.id.radio_conta);
        this.termos_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pixContent.setVisibility(View.GONE);
                bancoContent.setVisibility(View.VISIBLE);
                TypeAccountUsed="CONTA";
                checkbox_meat.setChecked(false);
                btnadvence.setVisibility(view.GONE);
            }
        });

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        items = new String[]{
            "CPF ou CNPJ",
            "E-mail",
            "Número de Celular",
            "Chave Aleatória"
        };
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);


        btnadvence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    StringBuffer mensagens=new StringBuffer();

                    if(TypeAccountUsed.equals("PIX")){
                        EditText input_pix_name_completed =(EditText) findViewById(R.id.input_pix_name_completed);
                        EditText input_pix_cp_cpnjf =(EditText) findViewById(R.id.input_pix_cp_cpnjf);
                        EditText input_pix_banck =(EditText) findViewById(R.id.input_pix_banck);
                        Spinner spinner1 =(Spinner) findViewById(R.id.spinner1);
                        EditText input_pix =(EditText) findViewById(R.id.input_pix);
                        int spinner_pos = spinner1.getSelectedItemPosition();

                        if(input_pix_name_completed.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_name));
                        }
                        if(input_pix_cp_cpnjf.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_cpf));
                        }

                        if(input_pix_banck.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_anme_bank));
                        }
                        if(input_pix.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_pix_key));
                        }
                        validacao(mensagens);
                        if(mensagens.length()==0){
                            Json = new StringBuffer();
                            Json.append("{");
                            Json.append("\"type\":\"PIX\"");
                            Json.append(",");
                            Json.append("\"name_completed\":\""+input_pix_name_completed.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"cpf\":\""+input_pix_cp_cpnjf.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"name_bank\":\""+input_pix_banck.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"type_pix\":\""+spinner1.getSelectedItem().toString()+"\"");
                            Json.append(",");
                            Json.append("\"pix\":\""+input_pix.getText().toString()+"\"");
                            Json.append("}");
                            paternsModel.setPixJson(Json.toString());
                        }
                    }else if(TypeAccountUsed.equals("CONTA")){
                        EditText input_bank_name_completed =(EditText) findViewById(R.id.input_bank_name_completed);
                        EditText input_bank_cp_cpnjf =(EditText) findViewById(R.id.input_bank_cp_cpnjf);
                        EditText input_bank_bank =(EditText) findViewById(R.id.input_bank_bank);
                        EditText input_bank_agency =(EditText) findViewById(R.id.input_bank_agency);
                        EditText input_bank_account =(EditText) findViewById(R.id.input_bank_account);

                        if(input_bank_name_completed.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_name));
                        }
                        if(input_bank_cp_cpnjf.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_cpf));
                        }

                        if(input_bank_bank.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_anme_bank));
                        }
                        if(input_bank_agency.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_agency));
                        }

                        if(input_bank_account.getText().toString().isEmpty()){
                            mensagens.append(getString(R.string.str_obr_agency_accont));
                        }
                        validacao(mensagens);
                        if(mensagens.length()==0){
                            Json = new StringBuffer();
                            Json.append("{");
                            Json.append("\"type\":\"CONTA\"");
                            Json.append(",");
                            Json.append("\"name_completed\":\""+input_bank_name_completed.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"cpf\":\""+input_bank_cp_cpnjf.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"name_bank\":\""+input_bank_bank.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"agency\":\""+input_bank_agency.getText().toString()+"\"");
                            Json.append(",");
                            Json.append("\"conta\":\""+input_bank_account.getText().toString()+"\"");
                            Json.append("}");
                            paternsModel.setPixJson(Json.toString());

                        }
                    }

                if(mensagens.length()==0){
                    Bundle bundle = new Bundle();
                    bundle.putString("name",paternsModel.getName());
                    bundle.putString("lastName",paternsModel.getLastName());
                    bundle.putString("cpf",paternsModel.getCpf());
                    bundle.putString("mobliePhone",paternsModel.getMobliePhone());
                    bundle.putString("email",paternsModel.getEmail());
                    bundle.putString("password",paternsModel.getPassword());
                    bundle.putString("address",paternsModel.getAddress());
                    bundle.putString("pix",paternsModel.getPixJson());
                    Intent intent = new Intent(DadosBancariosActivity.this, TermsActivity.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    private void validacao(StringBuffer mensagens) {
        if(mensagens.length() > 0){
            new AlertDialog.Builder(DadosBancariosActivity.this)
                .setTitle(getString(R.string.obrigatorio))
                .setMessage(mensagens.toString())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                .show();
        }
    }


    private void loadDataReceived() {
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
    }
}
