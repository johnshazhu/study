package com.study.doc.model

import androidx.databinding.Bindable
import com.study.doc.BR

class LoginViewModel : ObservableViewModel() {
    val data = TestViewModel()

    var checked : Boolean
        @Bindable get() {
            return data.checked
        }
        set(value) {
            if (data.checked != value) {
                data.checked = value

                notifyPropertyChanged(BR.checked)
            }
        }
}