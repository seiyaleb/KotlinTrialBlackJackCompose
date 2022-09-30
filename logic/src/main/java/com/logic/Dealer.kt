package com.logic

class Dealer: Human() {

    //山札全体を格納するList・引いたカードを一時的に格納するListを作成
    private val cards: MutableList<Int> = mutableListOf()
    private val plusCards: MutableList<Int> = mutableListOf()

    //ディーラーの最終的な合計値を取得
    fun dealerTurn() :Int {

        //山札の全シャッフル
        shuffle()

        //ディールした2枚を自分の手札にセット
        setCardFirst(dealFirst())

        //16未満の場合のみ、ヒットを繰り返し
        while (getTotal() < 17) {
            setCardHit(dealHit())
        }
        return getTotal()
    }

    //山札の全シャッフル
    private fun shuffle() {
        for(count in 1..4) {
            for (i in 1..13) {
                cards.add(i)
            }
        }
        cards.shuffle()
    }

    //ディールした2枚を返す
    fun dealFirst(): MutableList<Int> {
        plusCards.apply {
            add(cards[0])
            add(cards[1])
        }
        cards.removeAll(plusCards)
        return plusCards
    }

    //ヒットした1枚を返す
    fun dealHit(): Int {
        var card = cards[0]
        cards.removeAt(0)
        return card
    }

    //山札をリセット
    fun clearCards() = cards.clear()
}