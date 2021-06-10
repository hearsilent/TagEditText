package hearsilent.tagedittext.libs;

import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hearsilent.tagedittext.libs.interfaces.TagInterface;
import hearsilent.tagedittext.libs.models.TagIndex;
import hearsilent.tagedittext.libs.utils.TagUtils;
import hearsilent.tagedittext.libs.utils.TextReplacementSpan;
import hearsilent.tagedittext.libs.utils.Utils;


public class TagEditText extends AppCompatAutoCompleteTextView implements TextWatcher {

    private List<TagInterface> mTags = new ArrayList<>();

    private int mFilterStart = -1, mFilterEnd = -1;
    private boolean mTagEnabled = true;

    public TagEditText(Context context) {
        super(context);
        init();
    }

    public TagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        removeTextChangedListener(this);
        addTextChangedListener(this);
        setThreshold(1);
        setDropDownVerticalOffset(Utils.convertDpToPixelSize(-8, getContext()));
        setDropDownBackgroundResource(R.drawable.bg_popup);
    }

    public void setTags(List<? extends TagInterface> tags) {
        mTags = new ArrayList<>(tags);
    }

    public List<Integer> getIds() {
        List<Integer> ids = new ArrayList<>();
        Editable s = getEditableText();
        for (TextReplacementSpan span : s.getSpans(0, s.length(), TextReplacementSpan.class)) {
            if (!ids.contains(span.getId())) {
                ids.add(span.getId());
            }
        }
        return ids;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mTags.size() > 0) {
            TagUtils.parseText(s, mTags);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        performFiltering(getText(), 0);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (!mTagEnabled) {
            return;
        }

        SparseArray<TagIndex> tags = new SparseArray<>();
        Editable s = getEditableText();
        for (TextReplacementSpan span : s.getSpans(0, s.length(), TextReplacementSpan.class)) {
            int start = s.getSpanStart(span);

            TagIndex tagIndex = new TagIndex(start, span.getCount());
            tags.put(start, tagIndex);
        }

        mFilterStart = mFilterEnd = -1;

        for (int i = 0; i < getSelectionEnd(); i++) {
            if (tags.indexOfKey(i) > -1) {
                TagIndex tagIndex = tags.get(i);
                i += tagIndex.getCount() - 1;
                mFilterStart = mFilterEnd = -1;
            } else {
                char c = s.charAt(i);
                if (c == '@') {
                    mFilterStart = i;
                }
                if (mFilterStart > -1) {
                    mFilterEnd = i;
                }
            }
        }

        Filter filter = getFilter();
        if (filter != null) {
            if (mFilterStart != -1 && mFilterEnd != -1) {
                filter.filter(text.subSequence(mFilterStart, mFilterEnd + 1));
                hardcodedFixPopup();
            } else {
                filter.filter(null);
            }
        }
    }

    @Override
    protected void replaceText(CharSequence text) {
        if (mFilterStart != -1 && mFilterEnd != -1) {
            Editable editable = getText();
            if (editable != null) {
                editable.replace(mFilterStart, mFilterEnd + 1, text + " ");
            }
        }
    }

    public void setTagEnabled(boolean enabled) {
        mTagEnabled = enabled;
    }

    @SuppressWarnings({"JavaReflectionMemberAccess", "BusyWait"})
    private void hardcodedFixPopup() {
        try {
            Field field = AutoCompleteTextView.class.getDeclaredField("mPopup");
            field.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) field.get(this);
            if (popupWindow == null) {
                return;
            }
            new Thread(() -> {
                while (!popupWindow.isShowing()) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception ignore) {

                    }
                }
                new Handler(Looper.getMainLooper()).post(() -> {
                    try {
                        Field _field = ListPopupWindow.class.getDeclaredField("mPopup");
                        _field.setAccessible(true);
                        PopupWindow popup = (PopupWindow) _field.get(popupWindow);

                        if (popupWindow.getListView() != null) {
                            popupWindow.getListView().smoothScrollToPosition(0);
                        }

                        if (popup != null) {
                            popup.setElevation(Utils.convertDpToPixel(24, getContext()));
                            Field __field = PopupWindow.class.getDeclaredField("mBackgroundView");
                            __field.setAccessible(true);
                            View backgroundView = (View) __field.get(popup);
                            if (backgroundView != null) {
                                backgroundView.setOutlineProvider(mRoundCornerOutlineProvider);
                                backgroundView.setClipToOutline(true);

                                backgroundView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                    @Override
                                    public void onGlobalLayout() {
                                        int[] point = new int[2];
                                        getLocationInWindow(point);
                                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) backgroundView.getLayoutParams();
                                        int dp16 = Utils.convertDpToPixelSize(16, getContext());
                                        int maxTop = Utils.getStatusBarHeightPixel(getContext()) + dp16;
                                        int height = backgroundView.getHeight();
                                        int top = point[1] - height - Utils.convertDpToPixelSize(8, getContext()) - params.topMargin;
                                        params.topMargin = top < maxTop ? Math.min(maxTop - top, dp16) : 0;
                                        backgroundView.requestLayout();
                                        backgroundView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }
                                });
                            }
                        }
                    } catch (Exception ignore) {

                    }
                });
            }).start();
        } catch (Exception ignore) {

        }
    }

    private final ViewOutlineProvider mRoundCornerOutlineProvider = new ViewOutlineProvider() {

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(),
                    Utils.convertDpToPixel(8, getContext()));
        }
    };

}
