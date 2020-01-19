package com.corsage.memory_matching.local

import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.corsage.memory_matching.Application
import com.corsage.memory_matching.ext.length
import com.corsage.memory_matching.model.Card
import com.corsage.memory_matching.model.api.Product
import com.corsage.memory_matching.model.api.resp.Products
import com.corsage.memory_matching.model.collection.GameMode
import com.corsage.memory_matching.network.OkHttpInterceptor
import com.corsage.memory_matching.network.ShopifyApiService
import com.corsage.memory_matching.util.ApiConstants
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Game Manager.
 * This class controls the entirety of the game from start to finish.
 */

/**
 * Game Update Observer.
 * Multiple methods used to connect game updates to the UI.
 */
interface GameUpdateObserver {
    // Set of basic game procedures.
    fun onGameLoad(cards: ArrayList<Product>, viewTime: Int, timeLimit: Int)
    fun onGameStart()
    fun onGameFinish(score: Int, matches: Int)
    fun onGameRestart(cards: ArrayList<Product>, viewTime: Int, timeLimit: Int)
    fun onNetworkFail()

    // Card related procedures.
    fun onCardSelected(pos: Int)
    fun onIncorrectMatch(cards: ArrayList<Card>)
    fun onCorrectMatch(cards: ArrayList<Card>)

    // Score related procedures.
    fun onUpdateScore(score: Int)
}

class GameManager(private val observer: GameUpdateObserver) {
    private val TAG = "GameManager"

    private val settings = Application.settingsManager

    // Tracking products and selected cards.
    private lateinit var products: ArrayList<Product>
    private var selectedCards: ArrayList<Card>? = null

    // Tracking score and matches.
    private var score: Int = 0
    private var matches: Int = 0

    // Setup Retrofit with RxJava.
    private lateinit var shopifyApiService: ShopifyApiService
    private lateinit var compositeDisposable: CompositeDisposable

    init {
        setupApiService()
        getProducts()
    }

    fun selectCard(card: Card, timePassed: Int) {
        if (selectedCards == null) {
            selectedCards = ArrayList()
        }

        observer.onCardSelected(card.pos)
        selectedCards!!.add(card)

        for (c: Card in selectedCards!!) {
            if (c.product != card.product) {
                // Incorrect match.
                val temp = ArrayList(selectedCards!!)
                resetSelectedCards()

                Handler().postDelayed({
                    observer.onIncorrectMatch(temp)
                    subtractScore()
                }, 1000)
                return
            }
        }

        if (selectedCards!!.size == settings.matchCount) {
            val temp = ArrayList(selectedCards!!)
            resetSelectedCards()
            Handler().postDelayed({
                addScore(timePassed)
                observer.onCorrectMatch(temp)
            }, 600)
        }
    }

    fun restartGame() {
        score = 0
        matches = 0
        products.shuffle()
        observer.onGameRestart(ArrayList(products), settings.viewTime, settings.timeLimit)
        observer.onUpdateScore(score)
    }

    private fun addScore(timePassed: Int) {
        matches++

        when (GameMode.from(settings.gameMode)) {
            GameMode.NORMAL -> score += 100
            GameMode.HARD -> score += 150
            GameMode.TIME_TRIAL -> score += Math.max(10, 100 - timePassed)
        }

        // Finished game.
        if (matches * settings.matchCount == products.size) {
            observer.onGameFinish(score, matches)
        }

        observer.onUpdateScore(score)
    }

    private fun subtractScore() {
        when (GameMode.from(settings.gameMode)) {
            GameMode.HARD -> score -= 25
            else -> { }
        }

        observer.onUpdateScore(score)
    }

    private fun resetSelectedCards() {
        selectedCards = null
    }

    private fun getProducts() {
        compositeDisposable.add(
            shopifyApiService.getProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(response: Products) {
        // Apply settings for number of cards.
        val temp = response.products.take(settings.gridSize / settings.matchCount)
        products = ArrayList()

        for (i in 1..settings.matchCount) {
            products.addAll(temp)
        }

        products.shuffle()

        observer.onGameLoad(ArrayList(products), settings.viewTime, settings.timeLimit)
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, error.localizedMessage!!)
        error.printStackTrace()
    }

    private fun setupApiService() {
        // Create retrofit client.
        shopifyApiService = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(OkHttpInterceptor()).build()).baseUrl(ApiConstants.API_URL)
            .build().create(ShopifyApiService::class.java)

        compositeDisposable = CompositeDisposable()
    }
}