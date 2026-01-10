package com.glycin.intelli25.ui

import com.glycin.intelli25.model.Vec2
import java.util.ArrayList

class SpatialGrid<T>(
    private var width: Int,
    private var height: Int,
    private val cellSize: Int
) {
    private var cols = (width / cellSize) + 1
    private var rows = (height / cellSize) + 1

    private var buckets = Array(cols * rows) { ArrayList<T>() }
    private val queryBuffer = ArrayList<T>()

    fun clear() {
        for (bucket in buckets) {
            bucket.clear()
        }
    }

    fun insert(item: T, pos: Vec2) {
        val col = (pos.x.toInt() / cellSize).coerceIn(0, cols - 1)
        val row = (pos.y.toInt() / cellSize).coerceIn(0, rows - 1)
        buckets[row * cols + col].add(item)
    }

    fun retrieve(pos: Vec2): List<T> {
        queryBuffer.clear()

        val col = (pos.x.toInt() / cellSize)
        val row = (pos.y.toInt() / cellSize)

        for (y in (row - 1)..(row + 1)) {
            for (x in (col - 1)..(col + 1)) {
                if (x in 0 until cols && y in 0 until rows) {
                    queryBuffer.addAll(buckets[y * cols + x])
                }
            }
        }
        return queryBuffer
    }

    fun updateBounds(newW: Int, newH: Int) {
        width = newW
        height = newH

        val newCols = (width / cellSize) + 1
        val newRows = (height / cellSize) + 1
        val requiredSize = newCols * newRows

        if (requiredSize  > buckets.size) {
            resizeBuckets(requiredSize)
        }

        cols = newCols
        rows = newRows
    }

    @Suppress("UNCHECKED_CAST")
    private fun resizeBuckets(newSize: Int) {
        val newBuckets = arrayOfNulls<ArrayList<T>>(newSize)
        for (i in buckets.indices) {
            newBuckets[i] = buckets[i]
        }

        for (i in buckets.size until newSize) {
            newBuckets[i] = ArrayList<T>()
        }

        buckets = newBuckets as Array<ArrayList<T>>
    }
}