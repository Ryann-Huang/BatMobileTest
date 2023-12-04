package com.example.batmobiletest.inputhistory

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.batmobiletest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import kotlin.coroutines.coroutineContext

class InputHistoryManager(
    val context: Context?,
    var historyMaxCount: Int  = 6,
    var listInputHistory: ArrayList<String> = ArrayList<String>()
) {
    companion object {
        private const val NAME_PREF_BATMOBILE = "name_pref_batmobile"
        private const val KEY_PREF_BATMOBILE_INPUT_HISTORY = "key_batmobile_input_history"
    }

    fun getInputHistoryList(): ArrayList<String> {
        return listInputHistory
    }

    fun setInputHistoryList(list: ArrayList<String>) {
        listInputHistory = list
    }

    suspend fun addInputHistory(strInput: String) {
        if (strInput.isNullOrBlank() || strInput.isNullOrEmpty())
            return
        if (listInputHistory.size == historyMaxCount) {
            listInputHistory.removeAt(0)
        }
        listInputHistory.add(strInput)

        withContext(Dispatchers.IO) {
            setInputHistoryPrefData()
        }
    }

    fun getInputHistoryPrefData() {
        val pref = context?.getSharedPreferences(NAME_PREF_BATMOBILE, Context.MODE_PRIVATE)
        var strHistory = pref?.getString(KEY_PREF_BATMOBILE_INPUT_HISTORY, "")

        strHistory?.removeSuffix(";")
        Log.d("Ryan", "getPrefData(): $strHistory")
        var tmpList = ArrayList<String>(strHistory?.split(";"))
        setInputHistoryList(tmpList)
    }

    fun setInputHistoryPrefData() {
        val pref = context?.getSharedPreferences(NAME_PREF_BATMOBILE, Context.MODE_PRIVATE)

        var strTmp = ""
        for (item in listInputHistory) {
            strTmp = StringBuilder().append("$item;").toString()
        }

        pref?.edit()?.putString(KEY_PREF_BATMOBILE_INPUT_HISTORY, strTmp)
    }
}