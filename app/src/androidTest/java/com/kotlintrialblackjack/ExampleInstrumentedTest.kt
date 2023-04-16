package com.kotlintrialblackjack

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModelProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.ui.compose.NavigationTop
import com.ui.compose.Player
import com.ui.viewmodel.TotalViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context:Context
    private lateinit var viewModel:TotalViewModel

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = ViewModelProvider.NewInstanceFactory().create(TotalViewModel::class.java)
        }

    //画面遷移
    @Test
    fun navigation() {
        composeTestRule.setContent {
            NavigationTop()
        }

        //起動時
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.top_overview)
        ).assertExists()

        // Top から Explanation への遷移
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.top_btn_explanation)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.explanation_rule)
        ).assertExists()

        // Explanation から Player への遷移
        //player_situation1は通らない？
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.explanation_btn_fight)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_situation3)
        ).assertExists()

        // Player から Result への遷移
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_btn_stand)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.result_situation1)
        ).assertExists()

        // Result から Player への遷移
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.result_btn_again)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_situation1)
        ).assertExists()

        // Result から Top への遷移
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_btn_stand)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.result_btn_top)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.top_overview)
        ).assertExists()
    }

    //最初の2枚の合計値が表示されたか
    @Test
    fun initialCardsDisplayed() {
        composeTestRule.setContent {
            Player({},viewModel)
        }
        val total = viewModel.total.value!!
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_situation2) + "$total"
        ).assertExists()
    }

    //最初の2枚で合計値21の場合、結果画面へ遷移されるか
    //今回はスタンドボタンをタップできないでエラーになればテスト合格
    @Test
    fun navigatesWhenTotalOver21() {
        composeTestRule.setContent {
            NavigationTop()
        }
        for(i in 1..110) {

            //結果画面へ遷移
            composeTestRule.onNodeWithText(
                context.getString(com.ui.R.string.top_btn_explanation)
            ).performClick()
            composeTestRule.onNodeWithText(
                context.getString(com.ui.R.string.explanation_btn_fight)
            ).performClick()
            composeTestRule.onNodeWithText(
                context.getString(com.ui.R.string.player_btn_stand)
            ).performClick()
            composeTestRule.onNodeWithText(
                context.getString(com.ui.R.string.result_btn_top)
            ).performClick()
        }
    }

    //ヒットボタンをタップした場合、スナックバーが表示されるか
    @Test
    fun snackBarDisplayed() {
        composeTestRule.setContent {
            Player({},viewModel)
        }
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_btn_hit)
        ).performClick()
        composeTestRule.onNodeWithText(
            context.getString(com.ui.R.string.player_selection_hit)
        ).assertExists()
    }
}