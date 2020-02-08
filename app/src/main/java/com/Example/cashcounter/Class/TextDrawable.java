package com.Example.cashcounter.Class;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;


public class TextDrawable extends Drawable {
    private final Paint paint = new Paint();
    private final String text;

    public int getOpacity() {
        return -3;
    }

    public TextDrawable(String str) {
        this.text = str;
        this.paint.setColor(16777215);
        this.paint.setTextSize(100.0f);
        this.paint.setAntiAlias(true);
        this.paint.setTextAlign(Align.CENTER);
    }

    public void draw(Canvas canvas) {
        canvas.drawText(this.text, 0.0f, 35.0f, this.paint);
    }

    public void setAlpha(int i) {
        this.paint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
}
