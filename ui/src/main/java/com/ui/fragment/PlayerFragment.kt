package com.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.logic.Dealer
import com.logic.Player
import com.ui.R
import com.ui.viewmodel.TotalViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dealer: Dealer
    private lateinit var player: Player
    private val navController: NavController get() = findNavController()
    private val viewModel: TotalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //初期化
        dealer = Dealer()
        player = Player()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playerTvTotal:TextView = view.findViewById(R.id.player_tv_total)
        val playerTvMyCards:TextView = view.findViewById(R.id.player_tv_my_cards)

        //ディーラーの最終的な合計値を取得
        val totalDealer: Int = dealer.dealerTurn()

        //プレイヤーの現在の合計値をと手札の中身をセット
        player.setCardFirst(dealer.dealFirst())
        viewModel.setTotalAndMyCards(player.getTotal(),player.myCards)

        //ヒットボタンをタップした場合
        view.findViewById<Button>(R.id.player_btn_hit).setOnClickListener {

            //ヒットした数がエースの場合、ダイアログ表示
            var hitNumber = dealer.dealHit()
            if(hitNumber == 1) {

                var ace = 1
                var selectedId = 0
                val strList = arrayOf("1","11")
                AlertDialog.Builder(requireActivity())
                    .setTitle(getString(R.string.player_dialog_title))
                    .setSingleChoiceItems(strList, 0) { _, i ->
                        selectedId = i
                    }
                    .setPositiveButton("OK") { _, _ ->
                        when (selectedId) {
                            0 -> ace = 1
                            1 ->  ace = 11
                        }
                        //プレイヤーの現在の合計値をと手札の中身をセット
                        player.setCardHit(ace)
                        viewModel.setTotalAndMyCards(player.getTotalHit(),player.myCards)
                        executeHit(view,totalDealer)
                    }
                    .create().apply {
                        setCancelable(false)
                        setCanceledOnTouchOutside(false)
                        show()
                    }
            } else {

                //プレイヤーの現在の合計値をと手札の中身をセット
                player.setCardHit(hitNumber)
                viewModel.setTotalAndMyCards(player.getTotalHit(),player.myCards)
                executeHit(view,totalDealer)
            }
        }

        //スタンドボタンをタップした場合、結果画面へ遷移
        view.findViewById<Button>(R.id.player_btn_stand).setOnClickListener {

            val totalPlayer:Int? = viewModel.total.value
            totalPlayer?.let { it ->
                val action =
                    PlayerFragmentDirections.actionPlayerFragmentToResultFragment(
                        totalDealer,
                        it
                    )
                navController.navigate(action)
            }
        }

        //LiveDataを監視
        viewModel.total.observe(viewLifecycleOwner, Observer {
                it ->   playerTvTotal.text = "${getString(R.string.player_situation2)}$it"})
        viewModel.myCards.observe(viewLifecycleOwner, Observer {
                it ->   playerTvMyCards.text = "${getString(R.string.player_situation2_2)}$it"})
    }

    override fun onStop() {
        super.onStop()

        //山札と手札をリセット
        dealer.apply {
            clearCards()
            clearMyCards()
        }
        player.clearMyCards()
    }

    //ヒットした際の共通処理
    private fun executeHit(view: View,totalDealer: Int) {

        //スナックバー表示
        Snackbar.make(view.findViewById(R.id.player),
            R.string.player_selection_hit, Snackbar.LENGTH_SHORT).show()

        //プレイヤーの現在の合計値が21以上の場合、結果画面へ遷移
        val totalPlayer: Int? = viewModel.total.value
        totalPlayer?.let {
            if (it >= 21) {
                val action =
                    PlayerFragmentDirections.actionPlayerFragmentToResultFragment(
                        totalDealer,
                        it
                    )
                navController.navigate(action)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
