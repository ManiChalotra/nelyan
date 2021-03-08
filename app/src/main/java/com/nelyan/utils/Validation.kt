package com.nelyan.utils

import android.app.Activity

object Validation {

     fun checkName(name: String, activity: Activity): Boolean {

        if(!name.isNullOrEmpty()){
            if(name.length>2){
                if(isUserNameValid(name)){
                    return true
                }else{
                   activity.myCustomToast("please enter only Alphabets")
                }
            }else{
                activity.myCustomToast("please enter name minimum of  3 characters Long ")
            }
        }else{
           activity.myCustomToast("Please enter name ")
        }
        return false

    }

     fun checkEmail(email: String, activity: Activity): Boolean {
        if (!email.isNullOrEmpty()) {
            if (isEmailValid(email)) {
                return true
            } else {
                activity.myCustomToast( "Please enter a valid email")
                // toast("Please enter a valid email")
            }
        } else {
            activity.myCustomToast( "Please enter email")
            // toast("Please enter email")
        }
        return false
    }

     fun checkPassword(password: String, activity: Activity): Boolean {
        if (!password.isEmpty()) {
            if (password.length < 8) {
                activity.myCustomToast(

                        "Password should be minimum of 8 characters and maximum of 30 characters"
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
            activity.myCustomToast( "Please enter password")

            // toast("Please enter password")
        }
        return false

    }

}