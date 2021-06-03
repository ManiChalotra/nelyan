package com.nelyanlive.utils

import android.content.Context

object  AllSharedPref {

    internal var KEY = "Shared_Preferences"


    fun save(context: Context, key: String, value: String): Boolean {
        val editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
        editor.putString(key, value)

        return editor.commit()
    }
    fun setToken(context: Context, key: String, value: String):Boolean{
        val editor=context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
        editor.putString(key,value)
        return editor.commit()
    }

    fun getToken(context: Context, key: String):String{

        val savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        val default_value = ""
        return savedSession.getString(key, default_value)!!

    }


    fun save(context: Context, key: String, value: Int): Boolean {
        val editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)

        return editor.commit()
    }

    fun save(context: Context, key: String, value: Long): Boolean {
        val editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
        editor.putLong(key, value)

        return editor.commit()
    }

    fun save(context: Context, key: String, value: Boolean): Boolean {
        val editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
        editor.putBoolean(key, value)

        return editor.commit()
    }


    fun restoreString(context: Context, key: String): String {
        val savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        val default_value = ""
        return savedSession.getString(key, default_value)!!

    }

    fun restoreInt(context: Context, key: String): Int {
        val savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        val defaultValue = -1
        return savedSession.getInt(key, defaultValue)
    }

    fun restoreLong(context: Context, key: String): Long {
        val savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        val defaultValue: Long = -1
        return savedSession.getLong(key, defaultValue)
    }

    fun restoreBoolean(context: Context, key: String): Boolean {
        val savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
        val defaultValue = false
        return savedSession.getBoolean(key, defaultValue)
    }


    fun clear(context: Context) {
        val editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.commit()
    }


}
