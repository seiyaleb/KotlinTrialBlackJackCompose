package com.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel: ViewModel() {

    private val _total: MutableLiveData<Int> = MutableLiveData(0)
    private val _totalDealer: MutableLiveData<Int> = MutableLiveData(0)
    private val _myCads: MutableLiveData<MutableList<Int>> = MutableLiveData()
    val total: LiveData<Int> = _total
    val totalDealer: LiveData<Int> = _totalDealer
    val myCards: LiveData<MutableList<Int>> = _myCads

    //プレイヤーの合計値と手札の中身をセット
    fun setTotalAndMyCards(numTotal: Int,myCardsContent: MutableList<Int>) {
        _total.value = numTotal
        _myCads.value = myCardsContent
    }

    //ディーラーの合計値をセット
    fun setTotal(numTotal: Int) {
        _totalDealer.value = numTotal
    }
}