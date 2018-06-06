package com.dev.shehzadi.startupguidance.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shehzadi on 3/23/2018.
 */

public class Util {

    static Pattern emailPattern = Pattern.compile("[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");

    public static String HYPHENATED_PATTERN = "dd-MM-yyyy";
    public static String NON_HYPHENATED_PATTERN = "ddMMyyyy";
    public static String TIMESTAMP_PATTERN = "yyyyMMdd-HHmmss";

    public static boolean isValidEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        return !matcher.matches();
    }

    public static String getFormattedDate(String date, String fromPattern) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern(fromPattern);

        LocalDate localDate = dtf.parseLocalDate(date);
        String d = "" + localDate.getDayOfMonth();
        d = (d.length() == 1) ? "0" + d : d;

        String m = "" + localDate.getMonthOfYear();
        m = (m.length() == 1) ? "0" + m : m;

        String y = "" + localDate.getYear();

        return (fromPattern.equals(HYPHENATED_PATTERN)) ? d + m + y : d + "-" + m + "-" + y;
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(TIMESTAMP_PATTERN).format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(NON_HYPHENATED_PATTERN).format(new Date());
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getFileExtensionFromUri(Context context, Uri fileUri){
        String fileExtension;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11)
            fileExtension = getRealPathFromURI_BelowAPI11(context, fileUri);
            // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19)
            fileExtension = getRealPathFromURI_API11to18(context, fileUri);
            // SDK > 19 (Android 4.4)
        else
            fileExtension = getRealPathFromURI_API19(context, fileUri);

        return fileExtension.substring(fileExtension.lastIndexOf("."));
    }
}
