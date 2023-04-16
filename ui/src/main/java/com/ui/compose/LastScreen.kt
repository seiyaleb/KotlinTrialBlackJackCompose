package com.ui.compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.logic.Dealer
import com.logic.Player
import com.logic.determineWinner
import com.ui.R
import com.ui.viewmodel.TotalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//プレイヤー画面_ロジック
@Composable
fun Player(
    onNavigateToResult: () -> Unit,
    viewModel: TotalViewModel
) {
    val dealer by remember { mutableStateOf(Dealer()) }
    val player by remember { mutableStateOf(Player()) }

    val total by viewModel.total.observeAsState()
    val myCards by viewModel.myCards.observeAsState()

    LaunchedEffect(Unit) {
        //ディーラーの最終的な合計値をViewModelにセット
        val totalDealer: Int = dealer.dealerTurn()
        viewModel.setTotal(totalDealer)

        //プレイヤーの現在の合計値をと手札の中身をViewModelにセット
        player.setCardFirst(dealer.dealFirst())
        viewModel.setTotalAndMyCards(player.getTotal(), player.myCards)

        //最初の2枚で21以上の場合、結果画面へ遷移
        if (total!! >= 21) {
            onNavigateToResult()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            //山札と手札をリセット
            dealer.apply {
                clearCards()
                clearMyCards()
            }
            player.clearMyCards()
        }
    }

    PlayerContent(
        onNavigateToResult,
        viewModel,
        dealer,
        player,
        total,
        myCards
    )
}

//プレイヤー画面_UI全体
@Composable
fun PlayerContent(
    onNavigateToResult: () -> Unit,
    viewModel: TotalViewModel,
    dealer: Dealer,
    player: Player,
    total: Int?,
    myCards: MutableList<Int>?
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = stringResource(id = R.string.player_situation1),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 91.dp, end = 10.dp)
        )
        Text(
            //合計値を表示
            text = stringResource(id = R.string.player_situation2) + total.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .testTag("currentTotal")
        )
        Text(
            //手札状況を表示
            text = stringResource(id = R.string.player_situation2_2) + myCards.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.player_situation3),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 40.dp, end = 10.dp)
        )
        Button(
            onClick = {
                onHitButtonClick(
                    dealer,
                    player,
                    viewModel,
                    context,
                    snackBarHostState,
                    coroutineScope,
                    onNavigateToResult
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, top = 92.dp, end = 50.dp)
        ) {
            Text(
                text = stringResource(id = R.string.player_btn_hit),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { onNavigateToResult() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, top = 32.dp, end = 50.dp)
        ) {
            Text(
                text = stringResource(id = R.string.player_btn_stand),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.padding(top = 150.dp),
            snackbar = {
                Snackbar {
                    Text(text = it.message)
                }
            }
        )
    }
}

//プレイヤー画面_ヒットボタンをタップした場合の処理
private fun onHitButtonClick(
    dealer: Dealer,
    player: Player,
    viewModel: TotalViewModel,
    context: Context,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onNavigateToResult: () -> Unit
) {
    //1が出た場合、ダイアログで選択
    val hitNumber = dealer.dealHit()
    if (hitNumber == 1) {
        showAceSelectionDialog(player, viewModel, context)
    } else {
        player.setCardHit(hitNumber)
        viewModel.setTotalAndMyCards(player.getTotalHit(), player.myCards)
    }
    //スナックバー表示
    showSnackBar(context, coroutineScope, snackBarHostState)

    //21以上の場合、結果画面へ遷移
    val total = player.getTotalHit()
    if (total >= 21) {
        onNavigateToResult()
    }
}

//プレイヤー画面_1が出た場合のダイアログ表示
private fun showAceSelectionDialog(
    player: Player,
    viewModel: TotalViewModel,
    context: Context
) {
    var ace = 1
    var selectedId = 0
    val strList = arrayOf("1", "11")
    val dialogTitle = "Select Ace Value"

    MaterialAlertDialogBuilder(context)
        .setTitle(dialogTitle)
        .setSingleChoiceItems(strList, 0) { _, i ->
            selectedId = i
        }
        .setPositiveButton("OK") { _, _ ->
            ace = if (selectedId == 0) 1 else 11

            player.setCardHit(ace)
            viewModel.setTotalAndMyCards(player.getTotalHit(), player.myCards)
        }
        .create().apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
}

//プレイヤー画面_ヒットしたい場合のスナックバー表示
private fun showSnackBar(
    context: Context,
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState
) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(context.getString(R.string.player_selection_hit))
    }
}

//結果画面_ロジック
@Composable
fun Result(
    onNavigateToPlayer: () -> Unit,
    onNavigateToTop: () -> Unit,
    viewModel: TotalViewModel
) {

    //ディーラーとプレイヤーの合計値、勝者を取得
    val total by viewModel.total.observeAsState()
    val totalDealer by viewModel.totalDealer.observeAsState()
    val winner = determineWinner(totalDealer!!, total!!)

    ResultContent(
        winner = winner,
        total = total!!,
        totalDealer = totalDealer!!,
        onNavigateToPlayer = onNavigateToPlayer,
        onNavigateToTop = onNavigateToTop
    )
}

//結果画面_UI
@Composable
fun ResultContent(
    winner: String,
    total: Int,
    totalDealer: Int,
    onNavigateToPlayer: () -> Unit,
    onNavigateToTop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.result_situation1),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            //勝者を表示
            text = stringResource(id = R.string.result_situation_winner) + winner,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 36.dp)
        )
        Text(
            //プレイヤーの合計値を表示
            text = stringResource(id = R.string.result_situation_player) + total,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 36.dp)
                .testTag("playerTotal")
        )
        Text(
            //ディーラーの合計値を表示
            text = stringResource(id = R.string.result_situation_dealer) + totalDealer,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 12.dp)
                .testTag("dealerTotal")
        )
        Button(
            onClick = { onNavigateToPlayer() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 16.dp)
                .padding(top = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.result_btn_again),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = { onNavigateToTop() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp, vertical = 16.dp)
                .padding(top = 32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.result_btn_top),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    Player({}, viewModel())
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    Result({}, {}, viewModel())
}

