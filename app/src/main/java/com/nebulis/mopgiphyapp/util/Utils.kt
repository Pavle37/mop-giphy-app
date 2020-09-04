package com.nebulis.mopgiphyapp.util

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*


fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

fun <T> MutableLiveData<T>.forceRefresh() {
    this.postValue(this.value)
}