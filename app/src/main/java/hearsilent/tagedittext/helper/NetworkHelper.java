package hearsilent.tagedittext.helper;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hearsilent.tagedittext.callbacks.UsersCallback;
import hearsilent.tagedittext.models.UserModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkHelper {

    private static OkHttpClient mClient;

    private static void init() {
        mClient = new OkHttpClient().newBuilder().followRedirects(false).followSslRedirects(false)
                .connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
    }

    private static OkHttpClient getClient() {
        if (mClient == null) {
            init();
        }
        return mClient;
    }

    private static final String AVATAR_URL = "https://tinyfac.es/api/users";

    public static void getUsers(@NonNull final UsersCallback callback) {
        OkHttpClient client = getClient();

        Request request = new Request.Builder().url(AVATAR_URL).get().build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFail();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    try {
                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                        String body = responseBodyCopy.string();
                        JSONArray jsonArray = new JSONArray(body);
                        List<UserModel> userModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            UserModel model = new UserModel();
                            model.setUrl(jsonObject.getJSONArray("avatars").getJSONObject(0)
                                    .getString("url"));
                            model.setFirstName(jsonObject.getString("first_name"));
                            model.setLastName(jsonObject.getString("last_name"));
                            userModels.add(model);
                        }
                        callback.onSuccess(userModels);
                    } catch (Exception ignore) {
                        callback.onFail();
                    }
                } else {
                    callback.onFail();
                }
            }
        });
    }

}
