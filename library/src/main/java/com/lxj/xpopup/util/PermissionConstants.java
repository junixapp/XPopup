package com.lxj.xpopup.util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/29
 *     desc  : constants of permission
 * </pre>
 */
@SuppressLint("InlinedApi")
public final class PermissionConstants {

    public static final String CALENDAR             = "CALENDAR";
    public static final String CAMERA               = "CAMERA";
    public static final String CONTACTS             = "CONTACTS";
    public static final String LOCATION             = "LOCATION";
    public static final String MICROPHONE           = "MICROPHONE";
    public static final String PHONE                = "PHONE";
    public static final String SENSORS              = "SENSORS";
    public static final String SMS                  = "SMS";
    public static final String STORAGE              = "STORAGE";
    public static final String ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION";

    private static final String[] GROUP_CALENDAR             = {
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    };
    private static final String[] GROUP_CAMERA               = {
            Manifest.permission.CAMERA
    };
    private static final String[] GROUP_CONTACTS             = {
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS
    };
    private static final String[] GROUP_LOCATION             = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    private static final String[] GROUP_MICROPHONE           = {
            Manifest.permission.RECORD_AUDIO
    };
    private static final String[] GROUP_PHONE                = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.ANSWER_PHONE_CALLS
    };
    private static final String[] GROUP_PHONE_BELOW_O        = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS
    };
    private static final String[] GROUP_SENSORS              = {
            Manifest.permission.BODY_SENSORS
    };
    private static final String[] GROUP_SMS                  = {
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS,
    };
    private static final String[] GROUP_STORAGE              = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final String[] GROUP_ACTIVITY_RECOGNITION = {
            Manifest.permission.ACTIVITY_RECOGNITION,
    };

    @StringDef({CALENDAR, CAMERA, CONTACTS, LOCATION, MICROPHONE, PHONE, SENSORS, SMS, STORAGE,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionGroup {
    }

    public static String[] getPermissions(@PermissionGroup final String permission) {
        if (permission == null) return new String[0];
        switch (permission) {
            case CALENDAR:
                return GROUP_CALENDAR;
            case CAMERA:
                return GROUP_CAMERA;
            case CONTACTS:
                return GROUP_CONTACTS;
            case LOCATION:
                return GROUP_LOCATION;
            case MICROPHONE:
                return GROUP_MICROPHONE;
            case PHONE:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    return GROUP_PHONE_BELOW_O;
                } else {
                    return GROUP_PHONE;
                }
            case SENSORS:
                return GROUP_SENSORS;
            case SMS:
                return GROUP_SMS;
            case STORAGE:
                return GROUP_STORAGE;
            case ACTIVITY_RECOGNITION:
                return GROUP_ACTIVITY_RECOGNITION;
        }
        return new String[]{permission};
    }
}