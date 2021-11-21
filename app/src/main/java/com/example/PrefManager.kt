package com.example

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by sohag on 12/6/16.
 */
class PrefManager(_context: Context) {
    internal var pref: SharedPreferences
    internal var editor: SharedPreferences.Editor

    // shared pref mode
    internal var PRIVATE_MODE = 0

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    var isImageUploaded:Boolean
        get() = pref.getBoolean("isImageUploaded", false)
        set(value) {editor.putBoolean("isImageUploaded",value).commit()}



    companion object {
        // Shared preferences file name
        private val PREF_NAME = "PatientAid"

    }

}