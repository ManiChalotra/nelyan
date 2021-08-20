package com.nelyanlive.utils

import android.app.Activity
import com.nelyanlive.R

object Validation {

     fun checkName(name: String, activity: Activity): Boolean {

        if(name.isNotEmpty()){
            if(name.length>2){
                if(isUserNameValid(name)){
                    return true
                }else{
                   activity.myCustomToast(activity.getString(R.string.enter_only_alphabets))
                }
            }else{
                activity.myCustomToast(activity.getString(R.string.enter_min_chars))
            }
        }else{
           activity.myCustomToast(activity.getString(R.string.please_enter_name))
        }
        return false

    }

     fun checkEmail(email: String, activity: Activity): Boolean {
        if (!email.isEmpty()) {
            if (isEmailValid(email)) {
                return true
            } else {
                activity.myCustomToast( activity.getString(R.string.invalid_email_address))
                // toast("Please enter a valid email")
            }
        } else {
            activity.myCustomToast( activity.getString(R.string.please_enter_email))
            // toast("Please enter email")
        }
        return false
    }

     fun checkPassword(password: String, activity: Activity): Boolean {
        if (password.isNotEmpty()) {
            if (password.length < 8) {
                activity.myCustomToast(
                    activity.getString(R.string.password_min_8_to_30)
                )
                //toast("Password should be minimum of 8 characters and maximum of 30 characters")
            } else {
                return true

                /* this inner if-else loop is for strong password validation
                if (isPasswordValid(password)) {
                    return true

                } else {
                    toast(
                        "Please enter some strong password having atleast " + "\n" +
                                "One Capital and small letter and one special Symbol" + "\n" +
                                "One numeric keyword..."
                    )

                }*/

            }

        } else {
            activity.myCustomToast(activity.getString(R.string.please_enter_password))

            // toast("Please enter password")
        }
        return false

    }
     fun checkEmptyPassword(password: String, activity: Activity): Boolean {
        if (password.isNotEmpty()) {
                return true
        } else {
            activity.myCustomToast( activity.getString(R.string.please_enter_password))

        }
        return false

    }
     fun checkoldPassword(password: String, activity: Activity): Boolean {
        if (!password.isEmpty()) {
            if (password.length < 3) {
                activity.myCustomToast(
                        activity.getString(R.string.minimum_old_password)
                )
            } else {
                return true


            }

        } else {
            activity.myCustomToast( activity.getString(R.string.enter_old_password))

            // toast("Please enter password")
        }
        return false

    }
     fun checkNewPassword(password: String, activity: Activity): Boolean {
        if (!password.isEmpty()) {
            if (password.length < 8) {
                activity.myCustomToast(
                        activity.getString(R.string.password_min_8_to_30)
                )
            } else {
                return true
            }

        } else {
            activity.myCustomToast( activity.getString(R.string.please_enter_new_password))

            // toast("Please enter password")
        }
        return false

    }

}