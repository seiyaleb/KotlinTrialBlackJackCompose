package com.logic

//ディーラとプレイヤーに継承される
open class Human {

    //自身の手札を格納するListを作成
    val myCards: MutableList<Int> = mutableListOf()

    //自分の手札にセット
    //引数のディールした数値は一時的なものなため、削除
    open fun setCardFirst(plusCards: MutableList<Int>) {
        myCards.addAll(plusCards)
        plusCards.clear()
    }

    //自分の手札にセット
    //引数はヒットした数字
    open fun setCardHit(plusCard: Int) = myCards.add(plusCard)

    //自分の手札の合計値を取得
    open fun getTotal(): Int {
        var total = 0
        for (i in myCards.indices) {
            total += when {
                myCards[i] > 10 -> 10
                myCards[i] == 1 -> 11
                else -> myCards[i]
            }
        }
        return total
    }

    //手札をリセット
    open fun clearMyCards() = myCards.clear()
}