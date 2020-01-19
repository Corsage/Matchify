package com.corsage.memory_matching.network

import com.corsage.memory_matching.model.api.resp.Products
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Shopify API service.
 * Contains endpoints used throughout the app.
 */

interface ShopifyApiService {

    @GET("products.json")
    fun getProducts(@Query("page") page: Int = 1) : Single<Products>
}