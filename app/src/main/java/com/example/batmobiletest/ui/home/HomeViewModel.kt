package com.example.batmobiletest.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.batmobiletest.network.ApiClient
import com.example.batmobiletest.models.YoubikeStop
import com.example.batmobiletest.models.YoubikeStopItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "站 點 資 訊"
    }
    val text: LiveData<String> = _text

    private val _buttonText = MutableLiveData<String>().apply {
        value = "Click"
    }
    val buttonText: LiveData<String> = _buttonText

    private val _youbikeStopData = MutableLiveData<List<YoubikeStopItem>>()
    val youbikeStopData: LiveData<List<YoubikeStopItem>> get() = _youbikeStopData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    var errorMessage: String = ""
        private set

    fun getData() {
        _isLoading.value = true
        _isError.value = false

        val call = ApiClient.apiService.getYoubikeStop()

        call.enqueue(object : Callback<YoubikeStop> {
            override fun onResponse(call: Call<YoubikeStop>, response: Response<YoubikeStop>) {
                val responseBody = response.body()
                if (!response.isSuccessful || responseBody == null) {
                    onError("Data Processing Error")
                    return
                }

                _isLoading.value = false
                _youbikeStopData.postValue(responseBody!!)
            }

            override fun onFailure(call: Call<YoubikeStop>, t: Throwable) {
                onError(t.message)
                t.printStackTrace()
            }
        })
    }

    private fun onError(inputMsg: String?) {
        val msg = if (inputMsg.isNullOrBlank() or inputMsg.isNullOrEmpty()) "Unknown Error"
        else inputMsg

        errorMessage = StringBuilder("Ryan ERROR: ")
            .append("$msg some data may not display properly").toString()

        _isError.value = true
        _isLoading.value = false
    }
}