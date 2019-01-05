package android.com.cleaner.latlngFromAddressUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class GeocoderHandler extends Handler {

    @Override
    public void handleMessage(Message message) {

        String locationAddress;

        switch (message.what) {

            case 1:

                Bundle bundle = message.getData();
                locationAddress = bundle.getString("address");
                break;

            default:
                locationAddress = null;
        }
//        latLongTV.setText(locationAddress);

        System.out.println("GeocoderHandler.handleMessage " + locationAddress);

    }

}
