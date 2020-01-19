package com.corsage.memory_matching.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corsage.memory_matching.R
import com.corsage.memory_matching.model.Card
import com.corsage.memory_matching.model.api.Product
import com.corsage.memory_matching.util.Utils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_card.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Cards Adapter.
 * RecyclerView adapter for [Product] objects.
 */

class CardsAdapter(var cards: ArrayList<Product>?, private val onClickListener: View.OnClickListener?) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
    private val TAG = "CardsAdapter"

    fun addCards(cards: ArrayList<Product>) {
        if (this.cards == null) {
            this.cards = ArrayList()
        }

        this.cards!!.addAll(cards)

        notifyDataSetChanged()
    }

    fun removeLastItem() {
        val size = cards!!.size - 1
        cards!!.removeAt(size)
        notifyItemRemoved(size)
    }

    override fun getItemCount(): Int {
        if (cards == null || cards!!.isEmpty()) {
            return 0
        }
        return cards!!.size
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_card, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val card = cards?.get(pos)
        holder.bind(card, pos)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        lateinit var url: String

        fun bind(card: Product?, pos: Int) {
            if (card != null) {
                // Ensure the card is "reset".
                item_card_img.setImageURI(null as String?)
                item_card_img.alpha = 1f
                item_card_img.tag = Card(pos, card)
                url = card.image.src
            }
        }

        fun showCard() {
            Utils.performCardTurn(item_card_img, url)
        }

        fun hideCard() {
            Utils.performCardTurn(item_card_img, null)
        }

        fun freezeCard() {
            item_card_img.setOnClickListener(null)
        }

        fun unfreezeCard() {
            item_card_img.setOnClickListener(onClickListener)
        }

        fun completeCard() {
            item_card_img.animate().alpha(0f).setDuration(1000).start()
            item_card_img.setOnClickListener(null)
        }
    }
}