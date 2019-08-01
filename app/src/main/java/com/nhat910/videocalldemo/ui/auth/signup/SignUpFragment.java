package com.nhat910.videocalldemo.ui.auth.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nhat910.videocalldemo.R;
import com.nhat910.videocalldemo.ui.base.BaseFragment;
import com.nhat910.videocalldemo.utils.AppUtils;
import com.nhat910.videocalldemo.utils.KeyboardUtils;
import com.nhat910.videocalldemo.utils.ValidateUtils;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends BaseFragment implements SignUpContract.SignUpView {
    SignUpPresenterImp presenterImp;
    @BindView(R.id.fragSignUp_etUser)
    EditText etUser;
    @BindView(R.id.fragSignUp_etPass)
    EditText etPass;
    @BindView(R.id.fragSignUp_etPassConfirm)
    EditText etConfirmPass;
    @BindView(R.id.fragSignUp_etEmail)
    EditText etEmail;
    @BindView(R.id.fragSignUp_etFullName)
    EditText etFullName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        presenterImp = new SignUpPresenterImp();
        presenterImp.onAttach(this);
        KeyboardUtils.setupUI(view, getActivity());
        return view;
    }

    @Override
    public void onDestroy() {
        presenterImp.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.fragSignUp_btnSignUp, R.id.fragSignUp_btnBack})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.fragSignUp_btnSignUp:
                if (ValidateUtils.validateSignUp(getContext(),
                        etFullName.getText().toString(),
                        etUser.getText().toString().trim(),
                        etPass.getText().toString().trim(),
                        etConfirmPass.getText().toString().trim(),
                        etEmail.getText().toString().trim()))
                    presenterImp.signUpAuth(etFullName.getText().toString(),
                            etUser.getText().toString().trim(),
                            etPass.getText().toString().trim(),
                            etEmail.getText().toString().trim());
                break;
            case R.id.fragSignUp_btnBack:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void signUpSuccess(QBUser qbUser) {
        AppUtils.showDialogMessage(getContext(), getString(R.string.success), getString(R.string.signup_success_content), null);
    }

    @Override
    public void signUpError(String errorMessage) {
        AppUtils.showDialogMessage(getContext(), getString(R.string.unauthorize), errorMessage, null);
    }
}
