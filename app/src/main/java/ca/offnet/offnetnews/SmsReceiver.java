package ca.offnet.offnetnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import static android.content.Context.MODE_PRIVATE;

public class SmsReceiver extends BroadcastReceiver {
    //interface
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        //Get Phone Number
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.settings), MODE_PRIVATE);
        String phone = sharedPreferences.getString(context.getString(R.string.settings_phone), context.getString(R.string.offnet_number));
        int credits = sharedPreferences.getInt(context.getString(R.string.settings_credits), 0);

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //TODO Only from offnet number
            //Check the sender to filter messages which we require to read
            if (sender.contains(phone)) {
                String messageBody = smsMessage.getMessageBody();
                //Pass the message text to interface
                mListener.messageReceived(messageBody);
                // Hide message from user
                //this.abortBroadcast();
                credits++;
            }
        }

        //Update Credits
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.settings_credits), credits);
        editor.apply();

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
