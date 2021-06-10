package hearsilent.tagedittext.libs.utils;

import android.text.Editable;
import android.text.Spanned;
import android.util.SparseArray;

import java.util.List;

import hearsilent.tagedittext.libs.interfaces.TagInterface;
import hearsilent.tagedittext.libs.models.TagIndex;

public class TagUtils {

    public static void parseText(Editable s, List<TagInterface> tags) {
        SparseArray<TagIndex> removeTags = new SparseArray<>();
        for (TextReplacementSpan span : s.getSpans(0, s.length(), TextReplacementSpan.class)) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span) - 1;
            if (end - start + 1 != span.getCount()) {
                TagIndex tagIndex = new TagIndex(start, end - start + 1);
                removeTags.put(start, tagIndex);
            }
        }

        if (removeTags.size() > 0) {
            for (int i = 0; i < s.length(); i++) {
                if (removeTags.indexOfKey(i) > -1) {
                    TagIndex tagIndex = removeTags.get(i);
                    s.replace(i, i + tagIndex.getCount(), "");
                    i += tagIndex.getCount() - 1;
                }
            }
        }

        SparseArray<TagIndex> list = new SparseArray<>();
        for (int i = 0; i < tags.size(); i++) {
            TagInterface tag = tags.get(i);
            int index = -1;
            while ((index = s.toString().indexOf(tag.getTag(), index + 1)) > -1) {
                TagIndex tagIndex = new TagIndex(index, tag);
                list.put(index, tagIndex);
            }
        }
        for (int i = 0; i < s.length(); i++) {
            if (list.indexOfKey(i) > -1) {
                TagIndex tagIndex = list.get(i);
                s.setSpan(new TextReplacementSpan(tagIndex.getTag(), tagIndex.getLabel()), tagIndex.getStart(), tagIndex.getStart() + tagIndex.getCount(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                i += tagIndex.getCount() - 1;
            }
        }
    }
}
