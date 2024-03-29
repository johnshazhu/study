package com.study.doc.test

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.study.doc.BR

class TestData : BaseObservable() {
    @get: Bindable
    var text: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.text)
    }

    @get:Bindable
    var color: Int = 0
    set(value) {
        field = value
        notifyPropertyChanged(BR.text)
    }

    @get: Bindable
    var imageUrl: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.imageUrl)
    }
}