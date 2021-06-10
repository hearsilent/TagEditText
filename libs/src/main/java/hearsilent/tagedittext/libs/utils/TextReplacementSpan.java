package hearsilent.tagedittext.libs.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextReplacementSpan extends ReplacementSpan {

    private final String mTag;
    private final String mLabel;

    public TextReplacementSpan(String tag, String label) {
        super();

        mTag = tag;
        mLabel = label;
    }

    public int getCount() {
        return mTag.length();
    }

    public int getId() {
        return Integer.parseInt(mTag.substring(3, mTag.length() - 2));
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        super.updateMeasureState(p);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                       @Nullable Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(mLabel));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        paint.setColor(0xff007aff);
        canvas.drawText(mLabel, x, y, paint);
    }

}
