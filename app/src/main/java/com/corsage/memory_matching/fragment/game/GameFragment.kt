package com.corsage.memory_matching.fragment.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.corsage.memory_matching.R
import com.corsage.memory_matching.adapter.CardsAdapter
import com.corsage.memory_matching.ext.runLayoutAnimation
import kotlinx.android.synthetic.main.fragment_game.*
import android.os.Handler
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.corsage.memory_matching.Application
import com.corsage.memory_matching.fragment.BaseFragment
import com.corsage.memory_matching.local.GameManager
import com.corsage.memory_matching.local.GameUpdateObserver
import com.corsage.memory_matching.model.Card
import com.corsage.memory_matching.model.api.Product
import com.corsage.memory_matching.util.Utils
import com.corsage.memory_matching.widget.CountUpTimer
import com.google.android.flexbox.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Game Fragment.
 * This is the main game screen, uses [GameUpdateObserver].
 */

class GameFragment : BaseFragment(), View.OnClickListener, GameUpdateObserver {
    override val TAG = "GameFragment"

    // Cards setup.
    private lateinit var cardsAdapter: CardsAdapter
    private lateinit var layoutManager: FlexboxLayoutManager

    // Timer setup.
    private var timer: CountUpTimer? = null
    private var secondsElapsed: Int? = null

    // Game setup.
    lateinit var gameManager: GameManager

    // Button setup.
    private var canRestart: Boolean = false
    private var pressedBack: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ensure view is focused.
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (pressedBack) {
                    requireActivity().finish()
                } else {
                    pressedBack = true
                    Toast.makeText(requireContext(), "Press back again to exit.", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({ pressedBack = false }, 2000)
                }
            }
            true
        }

        cardsAdapter = CardsAdapter(null, this)

        layoutManager = object: FlexboxLayoutManager(requireContext()) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                val squares = Application.settingsManager.gridSize

                // Offset caused by margin constraints.
                val fixedHeight = height - (game_layout_board.height * 2) - (Utils.convertDpToPx(requireContext(), 16f) * 2)
                val fixedWidth = width - (Utils.convertDpToPx(requireContext(), 16f) * 2)

                val size = Utils.findOptimalSquareSize(fixedWidth.toDouble(), fixedHeight.toDouble(), squares)

                lp?.height = size.toInt()
                lp?.width = size.toInt()

                return true
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        // Setup flex layout manager.
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER

        game_recyclerview?.setHasFixedSize(true)
        game_recyclerview?.adapter = cardsAdapter
        game_recyclerview?.layoutManager = layoutManager

        gameManager = GameManager(this)

        game_btn_restart?.setOnClickListener {
            if (canRestart) gameManager.restartGame()
            else Toast.makeText(requireContext(), resources.getString(R.string.game_cant_restart), Toast.LENGTH_SHORT).show()
        }

        game_btn_quit?.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (secondsElapsed != null) {
            startTimer(Application.settingsManager.timeLimit, secondsElapsed!!)
        } else if (canRestart) {
            startTimer(Application.settingsManager.timeLimit)
        }
    }

    override fun onPause() {
        super.onPause()
        secondsElapsed = timer?.second
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onClick(v: View?) {
        if (v != null && v.id == R.id.item_card_img) {
            val card = v.tag as Card
            gameManager.selectCard(card, timer!!.second)
        }
    }

    /**
     * Game Update Observer methods.
     */

    override fun onCardSelected(pos: Int) {
        val holder = game_recyclerview?.findViewHolderForAdapterPosition(pos) as CardsAdapter.ViewHolder?
        holder?.showCard()
        holder?.freezeCard()
    }

    override fun onCorrectMatch(cards: ArrayList<Card>) {
        for (c: Card in cards) {
            val holder = game_recyclerview?.findViewHolderForAdapterPosition(c.pos) as CardsAdapter.ViewHolder?
            holder?.completeCard()
        }
    }

    override fun onIncorrectMatch(cards: ArrayList<Card>) {
        for (c: Card in cards) {
            val holder = game_recyclerview?.findViewHolderForAdapterPosition(c.pos) as CardsAdapter.ViewHolder?
            holder?.unfreezeCard()
            holder?.hideCard()
        }
    }

    override fun onUpdateScore(score: Int) {
        game_text_score?.text = resources.getString(R.string.game_score, score)
    }

    override fun onGameFinish(score: Int, matches: Int) {
        canRestart = false
        timer?.cancel()

        if (isVisible) {
            WinFragment(score, matches, timer!!.second).show(childFragmentManager, "win")
        }
    }

    override fun onGameLoad(cards: ArrayList<Product>, viewTime: Int, timeLimit: Int) {
        // Add the cards and lay them out.
        cardsAdapter.addCards(cards)
        game_recyclerview?.runLayoutAnimation()

        Handler().postDelayed({
            freezeCards()
            showCards()
        }, (80 * cards.size).toLong())

        Handler().postDelayed({
            hideCards()
            unfreezeCards()
            startTimer(timeLimit)
            canRestart = true
        }, viewTime.toLong() +  (80 * cards.size).toLong())
    }

    override fun onGameRestart(cards: ArrayList<Product>, viewTime: Int, timeLimit: Int) {
        canRestart = false
        removeCards()
        timer?.cancel()
        game_text_timer.text = resources.getString(R.string.game_time_default)
        Handler().postDelayed({
            onGameLoad(cards, viewTime, timeLimit)
        }, (cards.size * 75 + 500).toLong())
    }

    override fun onGameStart() {

    }

    override fun onNetworkFail() {

    }

    /**s
     * Helper methods.
     */

    private fun startTimer(timeLimit: Int, timeElapsed: Int = 0) {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }

        timer = object : CountUpTimer(timeLimit.toLong(), timeElapsed) {
            override fun onTick(second: Int) {
                game_text_timer?.text = resources.getString(R.string.game_time, second)
            }

            override fun onFinish() {
                canRestart = false
                if (isVisible) {
                    LoseFragment().show(childFragmentManager, "lose")
                }
            }
        }

        timer!!.start()
    }


    private fun unfreezeCards() {
        for (i in 0 until cardsAdapter.itemCount) {
            val holder = game_recyclerview?.findViewHolderForAdapterPosition(i) as CardsAdapter.ViewHolder?
            holder?.unfreezeCard()
        }
    }

    private fun freezeCards() {
        for (i in 0 until cardsAdapter.itemCount) {
            val holder = game_recyclerview?.findViewHolderForAdapterPosition(i) as CardsAdapter.ViewHolder?
            holder?.freezeCard()
        }
    }

    private fun showCards() {
        for (i in 0 until cardsAdapter.itemCount) {
            val holder = game_recyclerview?.findViewHolderForAdapterPosition(i) as CardsAdapter.ViewHolder?
            holder?.showCard()
        }
    }

    private fun hideCards() {
        for (i in 0 until cardsAdapter.itemCount) {
            val holder = game_recyclerview?.findViewHolderForAdapterPosition(i) as CardsAdapter.ViewHolder?
            holder?.hideCard()
        }
    }

    private fun removeCards() {
        val size = cardsAdapter.itemCount
        Thread(Runnable {
            for (i in 0 until size) {
                requireActivity().runOnUiThread {
                    cardsAdapter.removeLastItem()
                }
                Thread.sleep(75)
            }
        }).start()
    }

    /**
     * Base Fragment UI Theme
     * Applies to buttons.
     */

    override fun themeUI(isDark: Boolean) {
        if (isDark) {
            game_btn_quit?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkAccent))
            game_btn_restart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkAccent))
        } else {
            game_btn_quit?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            game_btn_restart?.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }
    }
}