package hearsilent.tagedittext.models;

import androidx.annotation.NonNull;

import java.util.Locale;

import hearsilent.tagedittext.libs.interfaces.TagInterface;

public class SuggestionModel implements TagInterface {

    private final String avatar;
    private final int id;
    @NonNull
    private final String name;
    @NonNull
    private final String username;

    public SuggestionModel(String avatar, int id, @NonNull String name, @NonNull String username) {
        this.avatar = avatar;
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @Override
    public String getTag() {
        return String.format(Locale.getDefault(), "@{{%d}}", id);
    }

    @Override
    public String getLabel() {
        return String.format(Locale.getDefault(), "@%s", name);
    }
}
