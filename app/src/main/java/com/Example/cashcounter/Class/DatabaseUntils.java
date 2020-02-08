package com.Example.cashcounter.Class;

public class DatabaseUntils {
    public static String selectQuery(String str, String str2, String str3, String str4) {
        if (str2 == null && str3 == null && str4 == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT * FROM ");
            stringBuilder.append(str);
            return stringBuilder.toString();
        } else if (str4 == null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("SELECT * FROM ");
            stringBuilder2.append(str);
            stringBuilder2.append(" WHERE ");
            stringBuilder2.append(str2);
            stringBuilder2.append(" = '");
            stringBuilder2.append(str3);
            stringBuilder2.append("'");
            return stringBuilder2.toString();
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("SELECT * FROM ");
            stringBuilder3.append(str);
            stringBuilder3.append(" WHERE ");
            stringBuilder3.append(str2);
            stringBuilder3.append(" = '");
            stringBuilder3.append(str3);
            stringBuilder3.append("' Limit ");
            stringBuilder3.append(str4);
            return stringBuilder3.toString();
        }
    }
}
