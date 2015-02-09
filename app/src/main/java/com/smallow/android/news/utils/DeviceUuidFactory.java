package com.smallow.android.news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by smallow on 2015/2/9.
 */
public class DeviceUuidFactory {
    protected static final String PREFS_FILE = "device_id";
    protected static final String PREFS_DEVICE_ID = "device_id";

    protected static UUID uuid;
    public DeviceUuidFactory(Context context) {

        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE,
                                    Context.MODE_PRIVATE);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);

                    if (id != null) {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id);

                    } else {

                        final String androidId = Secure
                                .getString(context.getContentResolver(),
                                        Secure.ANDROID_ID);


                        try {
                            if (androidId != null
                                    && !"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId
                                        .getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context
                                        .getSystemService(Context.TELEPHONY_SERVICE))
                                        .getDeviceId();
                                uuid = deviceId != null ? UUID
                                        .nameUUIDFromBytes(deviceId
                                                .getBytes("utf8")) : UUID
                                        .randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit()
                                .putString(PREFS_DEVICE_ID, uuid.toString())
                                .commit();

                    }

                }
            }
        }

    }


    public UUID getDeviceUuid() {
        return uuid;
    }
}
