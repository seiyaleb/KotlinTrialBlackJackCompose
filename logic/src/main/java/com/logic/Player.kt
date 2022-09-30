package com.logic

class Player: Human() {

    //ヒットした場合、自分の手札の合計値を取得
    //最初の2枚とそれ以降の手札で場合分け
    fun getTotalHit(): Int {
        var total = 0
        for (i in myCards.indices) {
            total += if (i < 2) {
                when {
                    myCards[i] > 10 -> 10
                    myCards[i] == 1 -> 11
                    else -> myCards[i]
                }
            } else {
                when{
                    myCards[i] == 12 || myCards[i] == 13  -> 10
                    else -> myCards[i]
                }
            }
        }
        return total
    }
}