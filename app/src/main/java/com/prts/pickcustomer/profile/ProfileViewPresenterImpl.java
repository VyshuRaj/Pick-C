package com.prts.pickcustomer.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.helpers.ValidationHelper;
import com.prts.pickcustomer.login.Customer;
import com.prts.pickcustomer.restapi.RestClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.helpers.ValidationHelper.isValidEmail;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;
import static com.prts.pickcustomer.restapi.RestClient.getRestService;

/**
 * Created by satya on 21-Dec-17.
 */

public class ProfileViewPresenterImpl implements ProfileViewPresenter {
    private ProfileView mProfileView;
    private Context mContext;
    private DialogBox mDialogBox;

    ProfileViewPresenterImpl(Context profileActivity, ProfileView profileActivity1) {
        mProfileView = profileActivity1;
        mContext = profileActivity;
        mDialogBox = mProfileView.getDialogBox();
    }

    @Override
    public void updateUserData(String name, String mobileNo, String emailID) {


        mProfileView.askPassworddToUpdateUserData();

    }

    private boolean validateAllFileds(String name, String mobileNo, String emailID) {
        if (!validateName(name))
            return false;

        if (!validateMobileNumber(mobileNo))
            return false;

        if (!validateEmail(emailID))
            return false;

        return true;
    }

    @Override
    public boolean validateName(String name) {

        if (name.isEmpty()) {
            mProfileView.enterName(mContext.getString(R.string.enter_name));
            return false;
        }
        return true;
    }

    @Override
    public boolean validateMobileNumber(String number) {

        if (number.isEmpty()) {
            mProfileView.enterMobileNumber(mContext.getString(R.string.enter_phone_number));
            return false;
        }

        if (number.length() != 10) {
            mProfileView.enterMobileNumber(mContext.getString(R.string.enter_10_digit_phone_number));
            return false;
        }

        return true;
    }

    @Override
    public boolean validateEmail(String email) {

        if (email.isEmpty()) {
            mProfileView.enterValidEmail(mContext.getString(R.string.enter_email));
            return false;
        }
        if (!ValidationHelper.isValidEmail(email)) {
            mProfileView.enterValidEmail(mContext.getString(R.string.valid_email));
            return false;
        }

        return true;
    }


    @Override
    public void getUserDataFromServer(String mobileNO) {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mProfileView.showInternet();
            return;
        }

        mDialogBox.showDialog("Getting your details");
        getCustomerDetails(mobileNO);


    }

    @Override
    public void validatePassword(final String password, final String mobileNo, final String name, final String emailID) {

        if (password.isEmpty()) {
            mProfileView.enterName(mContext.getString(R.string.enter_password));
            return;
        }

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mProfileView.showInternet();
            return;
        }

        mDialogBox.showDialog(mContext.getString(R.string.validate_pwd));

        RestClient.getRestService(BASE_URL).validateYourPwd(getHeaders(mContext), mobileNo, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
                        if (s) {
                            try {

                                if (!NetworkHelper.hasNetworkConnection(mContext)) {
                                    mProfileView.showInternet();
                                    return;
                                }

                                updateUserDetailsInServer(password, mobileNo, name, emailID);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mProfileView.enterName("Please enter correct password");
                            mDialogBox.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            mDialogBox.dismissDialog();
                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                validatePassword(password, mobileNo,name, emailID);
                            }else{
                                ActivityHelper.handleException(mContext,message);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

   public class data {
        public String Name;
        public String EmailId;
        public String Password;

    }

    private void updateUserDetailsInServer(@NonNull final String password, @NonNull final String mobileNo, @NonNull final String name, @NonNull final String emailId) {

        String deviceId = FirebaseInstanceId.getInstance().getToken();
      /*  Customer customer = new Customer();
        customer.setName(name);
        customer.setMobileNo(mobileNo);
        customer.setEmailID(emailId);*/
        data da=new data();
        da.EmailId=emailId;
        da.Name=name;
        da.Password=password;


        Call<Boolean> caa = RestClient.getRestService(BASE_URL).updateUserDataInServer(getHeaders(mContext), mobileNo, da);
        caa.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                mDialogBox.dismissDialog();

                Log.e("TAG", "response " + response.body());

                if (response.body()) {
                    CredentialManager.setName(mContext, name);
                    CredentialManager.setEmailId(mContext, emailId);
                    CredentialManager.setMobileNumber(mContext, mobileNo);
                    mProfileView.updateSuccessfully("User data updated successfully");
                    mProfileView.navigateToHomePage();
                } else {
                    mProfileView.updateSuccessfully("User data not updated");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable e) {

                try {
                    mDialogBox.dismissDialog();

                    ActivityHelper.handleException(mContext, String.valueOf(e.getMessage()));
                /*    if (){
                        updateUserDetailsInServer(password, mobileNo, name, emailId);
                    }*/
                  /*  String message=e.getMessage();
                    if (message.contains(TIME_OUT)) {

                    }else{
                        ActivityHelper.handleException(mContext,message);
                    }*/
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
    }

    private void getCustomerDetails(final String mobile) {
        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mProfileView.showInternet();
            return;
        }

        getRestService(BASE_URL).getCustomerDetails(getHeaders(mContext), mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Customer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Customer customer) {
                        mDialogBox.dismissDialog();

                        if (customer != null && customer.getStatus().equalsIgnoreCase("True")) {
                            CredentialManager.setName(mContext, customer.getName());
                            CredentialManager.setEmailId(mContext, customer.getEmailID());
                            mProfileView.setDataToViews(customer);
                        } else {
                            mProfileView.errorMessage("User details not available");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mDialogBox.dismissDialog();

                            ActivityHelper.handleException(mContext, String.valueOf(e.getMessage()));
                           /* if (ActivityHelper.handleException(mContext, String.valueOf(e.getMessage())))
                            {
                                getCustomerDetails(mobile);
                            }*/
                           /* String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {

                            }else{
                                ActivityHelper.handleException(mContext,message);
                            }*/
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
