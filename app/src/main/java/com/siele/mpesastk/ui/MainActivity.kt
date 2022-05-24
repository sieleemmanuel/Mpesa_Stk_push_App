package com.siele.mpesastk.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidstudy.daraja.Daraja
import com.androidstudy.daraja.DarajaListener
import com.androidstudy.daraja.model.AccessToken
import com.androidstudy.daraja.model.LNMExpress
import com.androidstudy.daraja.model.LNMResult
import com.androidstudy.daraja.util.Env
import com.siele.mpesastk.BuildConfig
import com.siele.mpesastk.databinding.ActivityMainBinding
import com.siele.mpesastk.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var daraja: Daraja
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        daraja = initDaraja()

        binding.apply {
            btnPay.setOnClickListener {
                val phoneNumber = edPhone.text.toString().trim()
                val amount = edAmount.text.toString().trim()
                Toast.makeText(this@MainActivity, """Phone $phoneNumber
                    |Amount $amount
                """.trimMargin(), Toast.LENGTH_SHORT).show()

                val lnmExpress = LNMExpress(
                    Constants.BUSINESS_SHORTCODE,
                    Constants.PASSKEY,
                    Constants.ACCOUNT_TYPE,
                    amount,
                    phoneNumber,
                    Constants.BUSINESS_SHORTCODE,
                    phoneNumber,
                    Constants.CALLBACK_URL,
                    "Mpesa Integration",
                    "Goods Payment"
                )

                daraja.requestMPESAExpress(lnmExpress, object : DarajaListener<LNMResult> {
                    override fun onResult(lnmResult: LNMResult) {
                        Toast.makeText(
                            this@MainActivity,
                            "Response here ${lnmResult.ResponseDescription}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(error: String?) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error here: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onError: $error")
                    }

                })
            }
        }
    }

    private fun initDaraja(): Daraja {
        return Daraja.with(
            BuildConfig.CONSUMER_KEY,
            BuildConfig.CONSUMER_SECRET,
            Env.SANDBOX,
            object : DarajaListener<AccessToken> {
                override fun onResult(result: AccessToken) {
                    Toast.makeText(this@MainActivity, result.access_token, Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, "onResult: ${result.access_token}")
                }

                override fun onError(error: String?) {
                    Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onError: $error")

                }

            }
        )

    }


 /*   OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, {
        "BusinessShortCode": 174379,
        "Password": "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjIwNTEzMTQyNzU0",
        "Timestamp": "20220513142754",
        "TransactionType": "CustomerPayBillOnline",
        "Amount": 1,
        "PartyA": 254740594418,
        "PartyB": 174379,
        "PhoneNumber": 254740594418,
        "CallBackURL": "https://mydomain.com/path",
        "AccountReference": "CompanyXLTD",
        "TransactionDesc": "Payment of X"
    });
    Request request = new Request.Builder()
    .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
    .method("POST", body)
    .addHeader("Content-Type", "application/json")
    .addHeader("Authorization", "Bearer g7ThsKGeAFpXrRAb4TQzGO5mGLh7")
    .build();
    Response response = client.newCall(request).execute()*/
}