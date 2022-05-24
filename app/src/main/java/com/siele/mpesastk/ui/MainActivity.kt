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

        daraja = initializeDaraja()

        binding.apply {
            btnPay.setOnClickListener {
                val phoneNumber = edPhone.text.toString().trim()
                val amount = edAmount.text.toString().trim()
                Toast.makeText(
                    this@MainActivity, """Phone $phoneNumber
                    |Amount $amount
                """.trimMargin(), Toast.LENGTH_SHORT
                ).show()

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
                        resetValues()
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

    private fun ActivityMainBinding.resetValues() {
        edPhone.setText("")
        edAmount.setText("")
    }

    private fun initializeDaraja(): Daraja {
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

}