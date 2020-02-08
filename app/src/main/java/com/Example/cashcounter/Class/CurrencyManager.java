package com.Example.cashcounter.Class;

import com.Example.cashcounter.Fragments.Dashboard;
import java.util.ArrayList;

public class CurrencyManager {

    public class Currency {
        String code;
        boolean isSelected;
        String name;
        String symbol;

        public String getCode() {
            return this.code;
        }

        public void setCode(String str) {
            this.code = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public void setSymbol(String str) {
            this.symbol = str;
        }

        public boolean isSelected() {
            return this.isSelected;
        }

        public void setSelected(boolean z) {
            this.isSelected = z;
        }
    }

    public ArrayList<Currency> getCurrencyList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(createCurrency("India", "INR", "₹"));
        arrayList.add(createCurrency("United States Dollar", "USD", "$"));
        arrayList.add(createCurrency("Euro", "EUR", "€"));
        arrayList.add(createCurrency("Japanese Yen", "JPY", "¥"));
        arrayList.add(createCurrency("Pound Sterling", "GBP", "£"));
        arrayList.add(createCurrency(" Australian Dollar", "AUD", "$"));
        arrayList.add(createCurrency("Canadian Dollar", "CAD", "$"));
        arrayList.add(createCurrency("Swiss Franc", "CHF", "Fr"));
        arrayList.add(createCurrency("Renminbi", "CNY", "元"));
        arrayList.add(createCurrency("Swedish Krona", "SEK", "kr"));
        arrayList.add(createCurrency("New Zealand Dollar", "NZD", "$"));
        arrayList.add(createCurrency(" Mexican Peso", "MXN", "$"));
        arrayList.add(createCurrency("Singapore Dollar", "SGD ", "$"));
        arrayList.add(createCurrency(" Hong Kong Dollar", "HKD", "$"));
        arrayList.add(createCurrency("Norwegian Krone", "NOK", "kr"));
        arrayList.add(createCurrency("South Korean Won", "KRW", "₩"));
        arrayList.add(createCurrency("Turkish Lira", "TRY", "₺"));
        arrayList.add(createCurrency("Russian Ruble", "RUB", "₽"));
        arrayList.add(createCurrency("Brazilian Real", "BRL", "$"));
        arrayList.add(createCurrency("South African Rand", "ZAR", "R"));
        return arrayList;
    }

    public ArrayList<String> getCurrencyList(String str) {
        ArrayList arrayList = new ArrayList();
        if (str.equals("INR")) {
            arrayList.add(Dashboard.CASH_IN_FRAG);
            arrayList.add(Dashboard.CASH_OUT_FRAG);
            arrayList.add("5");
            arrayList.add("10");
            arrayList.add("20");
            arrayList.add("50");
            arrayList.add("100");
            arrayList.add("200");
            arrayList.add("500");
            arrayList.add("2000");
        } else if (str.equals("USD")) {
            arrayList.add(Dashboard.CASH_IN_FRAG);
            arrayList.add(Dashboard.CASH_OUT_FRAG);
            arrayList.add("5");
            arrayList.add("10");
            arrayList.add("20");
            arrayList.add("50");
            arrayList.add("100");
        } else if (str.equals("EUR")) {
            arrayList.add("5");
            arrayList.add("10");
            arrayList.add("20");
            arrayList.add("50");
            arrayList.add("100");
            arrayList.add("500");
        }
        return arrayList;
    }

    public Currency createCurrency(String str, String str2, String str3) {
        Currency currency = new Currency();
        currency.setName(str);
        currency.setCode(str2);
        currency.setSymbol(str3);
        if (str2.equals("INR")) {
            currency.setSelected(true);
        } else {
            currency.setSelected(false);
        }
        return currency;
    }
}
