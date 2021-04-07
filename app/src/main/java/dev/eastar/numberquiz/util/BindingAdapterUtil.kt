package dev.eastar.numberquiz.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("select")
fun View.isSelect(isSelect: Boolean) {
    isSelected = isSelect
}