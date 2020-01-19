package com.corsage.memory_matching.model.api.resp

import com.corsage.memory_matching.model.api.Product

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Products data model.
 * This model requires an [ArrayList] of [Product].
 */

data class Products(val products: ArrayList<Product>)