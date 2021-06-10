package hearsilent.tagedittext.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hearsilent.tagedittext.R;
import hearsilent.tagedittext.adapter.SuggestionAdapter;
import hearsilent.tagedittext.callbacks.UsersCallback;
import hearsilent.tagedittext.databinding.ActivityMainBinding;
import hearsilent.tagedittext.helper.NetworkHelper;
import hearsilent.tagedittext.models.SuggestionModel;
import hearsilent.tagedittext.models.UserModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mMainBinding;

    private final List<SuggestionModel> mTagList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());

        fetchUsers();
    }


    /**
     * Avatar from TinyFaces (https://github.com/maximedegreve/TinyFaces)
     */
    private void fetchUsers() {
        NetworkHelper.getUsers(new UsersCallback() {

            @Override
            public void onSuccess(final List<UserModel> userModels) {
                if (isFinishing()) {
                    return;
                }
                runOnUiThread(() -> {
                    mTagList.clear();

                    if (userModels.size() == 0) {
                        Toast.makeText(MainActivity.this, "Fetch users failed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (UserModel user : userModels) {
                        int id = user.hashCode();
                        String name = user.getFirstName() + " " + user.getLastName();
                        SuggestionModel model = new SuggestionModel(user.getUrl(), id, name, (user.getFirstName() + user.getLastName()).toLowerCase());
                        mTagList.add(model);
                    }

                    mMainBinding.tagEditText.setTags(mTagList);
                    mMainBinding.tagEditText.setAdapter(new SuggestionAdapter(MainActivity.this, R.layout.item_user_suggestion, new ArrayList<>(mTagList)));

                    Toast.makeText(MainActivity.this, "Fetch users succeed: " + userModels.size(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onFail() {
                if (isFinishing()) {
                    return;
                }
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Fetch users failed", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
