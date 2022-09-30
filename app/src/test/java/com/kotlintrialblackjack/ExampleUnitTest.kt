package com.kotlintrialblackjack

import android.util.Log
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.logic.Dealer
import com.ui.R
import com.ui.activity.MainActivity
import com.ui.fragment.PlayerFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    private lateinit var dealer: Dealer

    @get:Rule
    val activityScenarioRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        dealer = Dealer()
        ShadowLog.stream = System.out
    }

    @Test
    fun dealerTotalRange() {

        //ディーラーの最終的な合計値を検証
        dealer.dealerTurn()
        val total = dealer.getTotal()
        Log.i("Total","dealer:$total")
        assert(total in 17..27)
    }

    @Test
    fun playerTotalFirstRange() {

        //プレイヤーの最初の合計値を検証
        launchFragmentInContainer<PlayerFragment>().onFragment() {
            val tvTotal:String = it.view?.findViewById<TextView>(R.id.player_tv_total)?.text.toString()
            val total:Int = tvTotal.substring(11).toInt()
            Log.i("Total","player:$total")
            assert(total in 4..22)
        }
    }
}