package com.nelyan_live.base;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link BaseFragment is base fragment for all fragments}
 */

public abstract class BaseFragment extends Fragment {

    private DownloadManager downloadManager;

    protected void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void switchFragment(int containerResourceId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerResourceId, fragment);
        fragmentTransaction.commit();
    }

    protected void refreshFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment).attach(fragment).commit();

    }

    protected void showKeyBoard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    protected List<String> getList() {
        List<String> mList = new ArrayList<>();
        mList.add("Option 1");
        mList.add("Option 2");
        mList.add("Option 3");
        mList.add("Option 4");
        mList.add("Option 5");
        mList.add("Option 6");
        mList.add("Option 7");
        mList.add("Option 8");
        mList.add("Option 9");
        mList.add("Option 10");
        return mList;
    }



    protected void shareByMessage(String link) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, link);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else { // For early versions, do what worked for you before.
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("address","phoneNumber");
            smsIntent.putExtra("sms_body", link);
            startActivity(smsIntent);
        }
    }

//    protected void deleteAlert(int pos, final String addressId, final DeleteCallback callback) {
//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
//        alertDialogBuilder.setMessage(getContext().getString(R.string.delete_alert_message))
//                .setCancelable(false)
//                .setPositiveButton(getContext().getString(R.string.delete),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                callback.onYesClicked();
//                         }
//                        });
//
//        alertDialogBuilder.setNegativeButton(getContext().getString(R.string.cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                        callback.onNoClicked();
//                    }
//                });
//        final android.app.AlertDialog alert = alertDialogBuilder.create();
//
//        alert.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface arg0) {
//                alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
//            }
//        });
//        alert.show();
//    }


}
