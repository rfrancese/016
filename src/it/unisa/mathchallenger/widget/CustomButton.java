package it.unisa.mathchallenger.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

    public CustomButton(Context context) {
	super(context);
	String fontName = "fonts/EraserDust.ttf";
	Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
	setTypeface(typeface);
    }

    public CustomButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	String fontName = "fonts/EraserDust.ttf";
	Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
	setTypeface(typeface);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	String fontName = "fonts/EraserDust.ttf";
	Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
	setTypeface(typeface);
    }

}
