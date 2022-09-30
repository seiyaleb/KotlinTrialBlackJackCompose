package com.kotlintrialblackjack

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ui.R
import com.ui.activity.MainActivity
import com.ui.fragment.ResultFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityScenarioRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigationTransitionOnBtn() {

        //ルール説明ボタンをタップし、遷移状態を検証
        onView(withId(R.id.top_btn_explnation)).perform(click())
        onView(withId(R.id.explanation)).check(matches(isDisplayed()))

        //ゲーム開始ボタンをタップし、遷移状態を検証
        onView(withId(R.id.explanation_btn_fight)).perform(click())
        onView(withId(R.id.player)).check(matches(isDisplayed()))

        //スタンドボタンをタップし、遷移状態を検証
        onView(withId(R.id.player_btn_stand)).perform(click())
        onView(withId(R.id.result)).check(matches(isDisplayed()))

        //再度プレイボタンをタップし、遷移状態を検証
        onView(withId(R.id.result_btn_again)).perform(click())
        onView(withId(R.id.player)).check(matches(isDisplayed()))

        //トップボタンをタップし、遷移状態を検証
        onView(withId(R.id.player_btn_stand)).perform(click())
        onView(withId(R.id.result_btn_top)).perform(click())
        onView(withId(R.id.top)).check(matches(isDisplayed()))
    }

    @Test
    fun navigationTransitionOnBack() {

        //説明画面からバックキーをタップし、遷移状態を検証
        onView(withId(R.id.top_btn_explnation)).perform(click())
        pressBack()
        onView(withId(R.id.top)).check(matches(isDisplayed()))

        //プレイヤー画面からバックキーをタップし、遷移状態を検証
        onView(withId(R.id.top_btn_explnation)).perform(click())
        onView(withId(R.id.explanation_btn_fight)).perform(click())
        pressBack()
        onView(withId(R.id.explanation)).check(matches(isDisplayed()))

        //結果画面からバックキーをタップし、遷移状態を検証
        onView(withId(R.id.explanation_btn_fight)).perform(click())
        onView(withId(R.id.player_btn_stand)).perform(click())
        pressBack()
        onView(withId(R.id.top)).check(matches(isDisplayed()))
    }

    @Test
    fun displayResult() {

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        //乱数を生成し、Bundleに埋め込む
        val randomPlayer = (4..31).random()
        val randomDealer = (18..27).random()
        val bundle = bundleOf("totalPlayer" to randomPlayer,"totalDealer" to randomDealer)

        //引数を受け取り、ResultFragment起動
        //ResultFragmentにTestNavHostを割り当て
        launchFragmentInContainer<ResultFragment>(bundle).onFragment { resultFragment ->
                navController.setGraph(R.navigation.nav_graph)
                Navigation.setViewNavController(resultFragment.requireView(), navController)
        }

        //TextViewのテキストを検証
        onView(withId(R.id.result_tv_player)).check(matches(
            withText("プレイヤーのあなたの合計値：${bundle.getInt("totalPlayer")}")))
        onView(withId(R.id.result_tv_dealer)).check(matches(
            withText("ディーラーさんの合計値：${bundle.getInt("totalDealer")}")))
    }
}