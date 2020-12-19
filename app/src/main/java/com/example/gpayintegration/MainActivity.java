package com.example.gpayintegration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gpayintegration.databinding.ActivityMainBinding;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private static final int GOOGLE_PAY_REQUEST_CODE = 1;
    private ActivityMainBinding binding;
    private String amount="5";
    private String name="Vaibhav Singhal";
    private String upiId="vaibhavsinghal5702@oksbi";
    private String transactionNote="pay test";
    private String status;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btngooglepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri=getUpiPaymentUri(amount,name,upiId,transactionNote);
                payWithGpay();
            }
        });
    }

    private void payWithGpay() {
        if(isAppInstalled(this,GOOGLE_PAY_PACKAGE_NAME))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent,GOOGLE_PAY_REQUEST_CODE);
        }
        else
            Toast.makeText(this, "Please Install Gpay!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
            status=data.getStringExtra("Status");
            if(resultCode==RESULT_OK && status.equals("success"))
            {
                Toast.makeText(this, "Payment Sucessful!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Uri getUpiPaymentUri(String amount, String name, String upiId, String transactionNote) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",transactionNote)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();
    }
}