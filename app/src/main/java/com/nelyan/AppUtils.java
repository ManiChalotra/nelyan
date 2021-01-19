package com.nelyan;
import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class AppUtils {

  @SuppressLint("NewApi")
  public static void gotoFragment(Context mContext, Fragment fragment, int frame_container, boolean isBackStack) {
    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.addToBackStack(null);
    if (isBackStack) {
      Fragment oldFragment=((AppCompatActivity)mContext).getSupportFragmentManager().findFragmentById(frame_container);
      transaction.replace(frame_container, fragment);
      transaction.addToBackStack(null);
      transaction.hide(Objects.requireNonNull(oldFragment));
    }
    else {
      transaction.replace(frame_container, fragment);
    }
    transaction.commit();
  }

}
