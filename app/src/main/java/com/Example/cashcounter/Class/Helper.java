package com.Example.cashcounter.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.net.Uri;
import android.os.Vibrator;

import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Helper {
    public static String JOB_NO_FORMATE = "0000";
    public static String NUMBER_FORMATE = "##.##";
    public static String SHOW_NUMBER_FORMATE = "#,##,##,###.##";

    public static void Vibrate(Context context, int i) {
        ((Vibrator) context.getSystemService("vibrator")).vibrate((long) i);
    }

    public static void LOG(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-");
        stringBuilder.append(str2);
        Log.e(str, stringBuilder.toString());
    }

    public static Bitmap decodeUri(Uri uri, Context context) throws FileNotFoundException {
        Options options = new Options();
        int i = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        while (true) {
            i2 /= 2;
            if (i2 < 512) {
                break;
            }
            i3 /= 2;
            if (i3 < 512) {
                break;
            }
            i *= 2;
        }
        options = new Options();
        options.inSampleSize = i;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
    }

    public static String ConverIntoBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
    }

    public static String FileIntoBase64(String str) {
        byte[] bArr = new byte[0];
        try {
            return Base64.encodeToString(loadFile(new File(str)), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] loadFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[((int) file.length())];
        int i = 0;
        while (i < bArr.length) {
            int read = fileInputStream.read(bArr, i, bArr.length - i);
            if (read < 0) {
                break;
            }
            i += read;
        }
        if (i < bArr.length) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not completely read file ");
            stringBuilder.append(file.getName());
            throw new IOException(stringBuilder.toString());
        }
        fileInputStream.close();
        return bArr;
    }

    public static String getFormattedDate(Context context, String str, String str2, String str3) {
        Calendar instance = Calendar.getInstance();
        instance.set(2, Integer.parseInt(str2));
        instance.set(5, Integer.parseInt(str3));
        instance.set(1, Integer.parseInt(str));
        Calendar instance2 = Calendar.getInstance();
        if (instance2.get(Calendar.DATE) == instance.get(5)) {
            return "Today";
        }
        if (instance2.get(5) - instance.get(5) == 1) {
            return "Yesterday";
        }
        if (instance2.get(1) == instance.get(1)) {
            return DateFormat.format("EEEE, MMMM d, h:mm aa", instance).toString();
        }
        return DateFormat.format("dd MMM yyyy", instance).toString();
    }

    public static String getRandomColorCode() {
        int HSVToColor = Color.HSVToColor(new float[]{(float) (new Random().nextInt(364) + 1), 58.0f, 90.0f});
        return String.format("#%06X", new Object[]{Integer.valueOf(HSVToColor &  16777215 )});
    }

    public static String changeDateFormate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Database.defaultFormate);
        try {
            return new SimpleDateFormat("dd MMM yyyy h:mm a").format(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean dateIsLessThenToCurrent(String str) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Database.defaultDateFormate);
            if (simpleDateFormat.parse(str).before(simpleDateFormat.parse(getCurrentDateTime(Database.defaultFormate)))) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String changeDateFormate(String str, String str2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Database.defaultFormate);
        try {
            return new SimpleDateFormat(str2).format(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String changeDateFormate(String str, String str2, String str3) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str2);
        try {
            return new SimpleDateFormat(str3).format(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateTime(String str) {
        return new SimpleDateFormat(str, Locale.getDefault()).format(new Date());
    }

    public static double getEdtToDouble(EditText editText) {
        return editText.getText().toString().trim().isEmpty() ? 0.0d : Double.parseDouble(editText.getText().toString().trim());
    }

    public static void hideSoftInput(Context context, EditText editText) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftInput(Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(1, 0);
    }
}
