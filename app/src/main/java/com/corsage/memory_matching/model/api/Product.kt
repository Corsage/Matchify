package com.corsage.memory_matching.model.api

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Product data model.
 * This model requires [id], [name], and [image].
 */

data class Product(val id: Long,
                   val name: String,
                   val image: Image) {

    /**
     * Need to only compare by [id].
     */
    override fun equals(other: Any?): Boolean {
        if (other is Product) {
            return this.id == other.id
        }
        return false
    }

    /**
     * Hash code only dependent on [id].
     */
    override fun hashCode(): Int {
        return this.id.toInt()
    }
}