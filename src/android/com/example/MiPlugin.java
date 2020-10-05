/**
 */
package com.example;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import java.util.ArrayList;
import java.util.Date;

public class MiPlugin extends CordovaPlugin {
    private static final String TAG = "MiPlugin";
    private Context ctx;
    static private CallbackContext callbackContext;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        this.ctx = webView.getContext();


        Log.d(TAG, "Inicializando MiPlugin");
    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("pagar")) {

            this.callbackContext = callbackContext;

            ArrayList<String> stringArray = new ArrayList<String>();

            JSONArray jsonArray = new JSONArray(args.getString(0));

            for (int i = 0; i < jsonArray.length(); i++) {
                stringArray.add(jsonArray.getString(i));
            }


            // An example of returning data back to the web layer
            String publickey = stringArray.get(0);
            String preferenceid = stringArray.get(1);
            // Echo back the first argument


            final MercadoPagoCheckout checkout = new MercadoPagoCheckout.Builder(publickey, preferenceid)
                    .build();

            checkout.startPayment(this.ctx, 112233);
        }
        return true;
    }


    public static void activityResult(int requestCode, int resultCode, Intent data) {
        PluginResult result;
        if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {

            final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
            result = new PluginResult(PluginResult.Status.OK, payment.getPaymentStatusDetail());
            callbackContext.sendPluginResult(result);
        } else {

            final MercadoPagoError mercadoPagoError =
                    (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
            result = new PluginResult(Status.ERROR, mercadoPagoError.getMessage());

            callbackContext.sendPluginResult(result);
        }

    }

}
