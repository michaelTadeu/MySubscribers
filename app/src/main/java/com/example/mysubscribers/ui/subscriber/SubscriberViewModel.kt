package com.example.mysubscribers.ui.subscriber

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysubscribers.R
import com.example.mysubscribers.repository.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repositoy: SubscriberRepository) : ViewModel() {
    private val _subscriberStateEventData = MutableLiveData<SubscriberState>()
    val subscriberStateEventData: LiveData<SubscriberState>
        get() = _subscriberStateEventData

    private val _mensageEventData = MutableLiveData<Int>()
    val mensageEventData: LiveData<Int>
        get() = _mensageEventData

    fun addOrUpdateSubscriber(name: String, email: String, id: Long = 0) = viewModelScope.launch {
        if (id > 0) {
            updateSuscriber(id, name, email)
        } else {
            insertSuscriber(name, email)
        }
    }

    private fun updateSuscriber(id: Long, name: String, email: String) = viewModelScope.launch {
        try {
            repositoy.updateSubscriber(id, name, email)
            _subscriberStateEventData.value = SubscriberState.Updated
            _mensageEventData.value = R.string.subscriber_updated_successfully
        } catch (ex: Exception) {
            _mensageEventData.value = R.string.subscriber_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

    private fun insertSuscriber(name: String, email: String) = viewModelScope.launch {
        try {
            val id = repositoy.insertSubscriber(name, email)
            if (id > 0) {
                _subscriberStateEventData.value = SubscriberState.Inserted
                _mensageEventData.value = R.string.subscriber_inserted_successfully
            }
        } catch (ex: Exception) {
            _mensageEventData.value = R.string.subscriber_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }

    fun removeSuscriber(id: Long) = viewModelScope.launch {
        try {
            if (id > 0) {
                repositoy.deleteSubscriber(id)
                _subscriberStateEventData.value = SubscriberState.Deleted
                _mensageEventData.value = R.string.subscriber_deleted_successfully
            }
        } catch (ex: Exception) {
            _mensageEventData.value = R.string.subscriber_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    sealed class SubscriberState {
        object Inserted : SubscriberState()
        object Updated : SubscriberState()
        object Deleted : SubscriberState()
    }

    companion object {
        private val TAG = SubscriberViewModel::class.java.simpleName
    }

}