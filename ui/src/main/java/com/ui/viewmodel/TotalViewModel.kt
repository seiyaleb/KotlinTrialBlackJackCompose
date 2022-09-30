package com.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel: ViewModel() {

    private val _total: MutableLiveData<Int> = MutableLiveData(0)
    private val _myCads: MutableLiveData<MutableList<Int>> = MutableLiveData()
    val total: LiveData<Int> = _total
    val myCards: LiveData<MutableList<Int>> = _myCads

    //合計値と手札の中身をセット
    fun setTotalAndMyCards(numTotal: Int,myCardsContent: MutableList<Int>) {
        _total.value = numTotal
        _myCads.value = myCardsContent
    }
}