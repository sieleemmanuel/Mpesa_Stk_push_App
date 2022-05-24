package com.siele.mpesastk.utils

import com.androidstudy.daraja.util.TransactionType
import com.siele.mpesastk.BuildConfig

object Constants {
    var CALLBACK_URL ="https://mpesa-requestbin.herokuapp.com/1h2mk4d1"
    var BUSINESS_SHORTCODE = "174379"
    var ACCOUNT_TYPE = TransactionType.CustomerPayBillOnline
    var PASSKEY = BuildConfig.PASSKEY
    }