package com.corsage.memory_matching.model

import com.corsage.memory_matching.model.api.Product

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Card data model.
 * This model requires a [pos] and [product].
 */

data class Card(val pos: Int, val product: Product)