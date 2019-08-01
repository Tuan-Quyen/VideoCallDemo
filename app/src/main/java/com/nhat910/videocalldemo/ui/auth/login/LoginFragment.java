package com.nhat910.videocalldemo.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.others.Constant;
import com.nhat910.videocalldemo.ui.main.MainActivity;
import com.nhat910.videocalldemo.ui.auth.signup.SignUpFragment;
import com.nhat910.videocalldemo.ui.base.BaseFragment;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.nhat910.videocalldemo.utils.KeyboardUtils;
import com.nhat910.videocalldemo.utils.SharedPrefUtils;
import com.nhat910.videocalldemo.utils.ValidateUtils;
import com.quickblox.users.model.QBUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginContract.LoginView {
    LoginPresenterImp presenterImp;
    @BindView(R.id.fragSignIn_etUser)
    EditText etUser;
    @BindView(R.id.fragSignIn_etPass)
    EditText etPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        presenterImp = new LoginPresenterImp();
        presenterImp.onAttach(this);
        KeyboardUtils.setupUI(view, getActivity());
        initView();
        return view;
    }

    private void initView() {
        String userId = SharedPrefUtils.getString(getContext(), Constant.USER_ID);
        String pass = SharedPrefUtils.getString(getContext(), Constant.USER_PASS);
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(pass)) {
            presenterImp.getUserWithId(userId, pass);
        }
    }

    @Override
    public void onDestroy() {
        presenterImp.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.fragSignIn_btnLogin, R.id.fragSignIn_btnSignUp})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.fragSignIn_btnLogin:
                if (ValidateUtils.validateLogin(getContext(), etUser.getText().toString().trim(), etPass.getText().toString().trim()))
                    presenterImp.loginAuth(etUser.getText().toString().trim(), etPass.getText().toString().trim());
                break;
            case R.id.fragSignIn_btnSignUp:
                addFragment(new SignUpFragment(), true);
                break;
            default:
                break;
        }
    }

    @Override
    public void loginSuccess(QBUser qbUser) {
        SharedPrefUtils.setString(getContext(), Constant.USER_ID, qbUser.getId().toString());
        SharedPrefUtils.setString(getContext(), Constant.USER_PASS, qbUser.getPassword());
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void loginError(String errorMessage) {
        AppUtils.showDialogMessage(getContext(), getString(R.string.unauthorize), errorMessage, null);
    }
}
