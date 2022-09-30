package com.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.logic.determineWinner
import com.ui.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var totalP:Int = 0
    private var totalD:Int = 0

    private val navController: NavController get() = findNavController()
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //プレイヤー画面から値二つを取得
        totalP = args.totalPlayer
        totalD = args.totalDealer

        //テキストに設定
        view.findViewById<TextView?>(R.id.result_tv_player).apply {
            text = "${getString(R.string.result_situation_player)}${totalP}"}
        view.findViewById<TextView?>(R.id.result_tv_dealer).apply {
            text = "${getString(R.string.result_situation_dealer)}${totalD}"
        }
        view.findViewById<TextView?>(R.id.result_tv_winner).apply {
            text = "${getString(R.string.result_situation_winner)}${determineWinner(totalD,totalP)}"
        }

        //再度プレイボタンをタップした場合、プレイヤー画面へ遷移
        view.findViewById<Button>(R.id.result_btn_again).setOnClickListener{
            navController.popBackStack(R.id.playerFragment,false)
        }

        //トップボタンをタップした場合、トップ画面へ遷移
        view.findViewById<Button>(R.id.result_btn_top).setOnClickListener{
            navController.popBackStack(R.id.topFragment,false)
        }

        //バックボタンをタップした場合、トップ画面へ遷移
        requireActivity().onBackPressedDispatcher.addCallback(this@ResultFragment) {
            navController.popBackStack(R.id.topFragment,false)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}