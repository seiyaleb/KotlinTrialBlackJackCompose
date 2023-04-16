package com.kotlintrialblackjack

import com.logic.Dealer
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    private lateinit var dealer: Dealer

    @Before
    fun setup() {
        dealer = Dealer()
    }

    @Test
    fun dealerTotalRange() {
        for(i in 0..100) {
            //ディーラーの最終的な合計値を検証
            dealer.dealerTurn()
            val total = dealer.getTotal()
            assert(total in 17..27)
            dealer.clearCards()
            dealer.clearMyCards()
        }
    }
}