package com.logic

//両者の合計値より勝者を判定
fun determineWinner(dealerTotal: Int, playerTotal: Int) :String {

    val winner = when {
        playerTotal == 21 && dealerTotal != 21 -> "プレイヤーのあなた"
        playerTotal != 21 && dealerTotal == 21 -> "ディーラー"
        dealerTotal > 21 && playerTotal > 21 -> "なし"
        dealerTotal < 21 && playerTotal > 21 -> "ディーラー"
        dealerTotal > 21 && playerTotal < 21 -> "プレイヤーのあなた"
        playerTotal < dealerTotal -> "ディーラー"
        playerTotal > dealerTotal -> "プレイヤーのあなた"
        playerTotal == dealerTotal -> "引き分け"
        else -> "なし"
    }
    return winner
}