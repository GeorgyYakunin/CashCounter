package com.Example.cashcounter.Class;

public class ENC {
    public static String encrypttext(String str) {
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            int charAt = str.charAt(i);
            if (charAt > 32 && charAt < 80) {
                charAt += 47;
            } else if (charAt > 80 && charAt < 127) {
                charAt -= 47;
            }
            String ch = Character.toString((char) charAt);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(ch);
            str2 = stringBuilder.toString();
        }
        return str2;
    }

    public static String transaction_encrypttext(String str) {
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            int charAt = str.charAt(i);
            if (charAt != 82) {
                if (charAt > 32 && charAt < 80) {
                    charAt += 47;
                } else if (charAt > 80 && charAt < 127) {
                    charAt -= 47;
                }
            }
            String ch = Character.toString((char) charAt);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(ch);
            str2 = stringBuilder.toString();
        }
        return str2;
    }

    public static String decrypttext(String str) {
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            int charAt = str.charAt(i);
            if (charAt > 32 && charAt < 80) {
                charAt += 47;
            } else if (charAt > 80 && charAt < 127) {
                charAt -= 47;
            }
            String ch = Character.toString((char) charAt);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(ch);
            str2 = stringBuilder.toString();
        }
        return str2;
    }

    public static String transaction_decrypttext(String str) {
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            int charAt = str.charAt(i);
            if (charAt != 82) {
                if (charAt > 32 && charAt < 80) {
                    charAt += 47;
                } else if (charAt > 80 && charAt < 127) {
                    charAt -= 47;
                }
            }
            String ch = Character.toString((char) charAt);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(ch);
            str2 = stringBuilder.toString();
        }
        return str2;
    }
}
