package pro.yueyuan.project_t.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import pro.yueyuan.project_t.R;


public class CrossEditText extends android.support.v7.widget.AppCompatEditText implements TextWatcher, View.OnFocusChangeListener {

    private Drawable cross;

    public CrossEditText(Context context) {
        this(context, null);
    }

    public CrossEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CrossEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if ((cross = getCompoundDrawables()[2]) == null) {
            cross = getResources().getDrawable(R.drawable.ic_search_clear);
        }
        assert cross != null;
        cross.setBounds(0, 0, cross.getIntrinsicWidth(), cross.getIntrinsicHeight());
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() < 20 ? 20 : getPaddingRight(), getPaddingBottom());
        //default hide the cross
        setCrossVisible(false);
        //focus listener
        setOnFocusChangeListener(this);
        //text listener
        addTextChangedListener(this);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        //padding right is at least 5dp
    }

    private void setCrossVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], visible ? cross : null, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (
            //when touch up
                event.getAction() == MotionEvent.ACTION_UP
                        //when cross is showing
                        && getCompoundDrawables()[2] != null
                        //touch loc is on the cross
                        && event.getX() > (getWidth() - getTotalPaddingRight())
                        && event.getX() < (getWidth() - getPaddingRight())
                ) {
            setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setCrossVisible(getText().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setCrossVisible(getText().length() > 0);
        } else {
            setCrossVisible(false);
        }
    }
}
