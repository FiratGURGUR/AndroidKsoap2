package com.gurgur.androidksoap2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;

    private static final String NAMESPACE = "http://www.dataaccess.com/webservicesserver/";
    private static final String URL = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso";
    private static final String SOAP_ACTION = "http://www.dataaccess.com/webservicesserver/NumberToWords";
    private static final String METHOD_NAME = "NumberToWords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edt_);
        textView = findViewById(R.id.text);
        button = findViewById(R.id.btn_convert);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new asynTask().execute(editText.getText().toString());

            }
        });

    }

    private class asynTask extends AsyncTask<String,Void,Void> {
        String resultText;
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog alert;
        // Doğrula butonuna basıldığında ilk yapılacaklar..
        @Override
        protected void onPreExecute() {
            // ProgressDialog oluşturuyoruz.
            progressDialog.setMessage("Kontrol ediliyor...");
            progressDialog.show();
        }
        // Doğrula butonuna basıldığında arka planda yapılacaklar..
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... strings) {
            // Sorgumuzu oluşturuyoruz..
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("ubiNum",strings[0]);
            // SoapEnvelope oluşturduk ve Soap 1.1 kullanacağımız belirttik.
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            // Envelope ile requesti birbiri ile bağladık.
            envelope.setOutputSoapObject(Request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try
            {
                // Web servisi çağırdık
                androidHttpTransport.call(SOAP_ACTION, envelope);
                // Gelen verileri değerlendirmek için objemizi oluşturuyoruz.
                SoapObject response = (SoapObject) envelope.bodyIn;
                //textView.setText(response.getProperty(0).toString());
                resultText = response.getProperty(0).toString();
                Log.i("firat" , response.getProperty(0).toString());
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        // Arka plan işlemleri bittikten sonra yapılacaklar..
        @Override
        protected void onPostExecute(Void aVoid) {
            // ProgressDialog kapatılıyor.
            progressDialog.dismiss();
            // AlertDialog'u gösteriyoruz.
            textView.setText(resultText);
            alert = builder.setMessage(resultText)
                    .setTitle("Sonuç")
                    .setCancelable(true)
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).create();
            alert.show();
        }
    }




}
