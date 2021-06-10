package hearsilent.tagedittext.callbacks;

import java.util.List;

import hearsilent.tagedittext.models.UserModel;

public abstract class UsersCallback {

    public abstract void onSuccess(List<UserModel> userModels);

    public abstract void onFail();
}