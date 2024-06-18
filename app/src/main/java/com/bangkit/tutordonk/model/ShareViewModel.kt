package com.bangkit.tutordonk.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class ShareViewModel(
    val name: MutableLiveData<String> = MutableLiveData("")
) : ViewModel()