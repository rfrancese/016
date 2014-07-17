package it.unisa.mathchallenger.widget;

import it.unisa.mathchallenger.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
	super(context);
	if (isInEditMode()) {
	    return;
	}

	String fontName = "fonts/EraserDust.ttf";
	if (fontName != null) {
	    Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
	    setTypeface(typeface);
	}
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	if (isInEditMode()) {
	    return;
	}

	TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
	String fontName = "fonts/EraserDust.ttf";
	styledAttrs.recycle();

	if (fontName != null) {
	    Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
	    setTypeface(typeface);
	}
    }

    public CustomTextView(Context context, AttributeSet attrs) {
	super(context, attrs);

	// Typeface.createFromAsset doesn't work in the layout editor.
	// Skipping...
	if (isInEditMode()) {
	    return;
	}

	TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
	String fontName = "fonts/EraserDust.ttf";
	styledAttrs.recycle();

	if (fontName != null) {
	    Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
	    setTypeface(typeface);
	}
    }

}
