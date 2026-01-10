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
        return retrieve(pos, cellSize)
    }

    fun retrieve(pos: Vec2, range: Int): List<T> {
        queryBuffer.clear()

        val minX = (pos.x.toInt() - range)
        val maxX = (pos.x.toInt() + range)
        val minY = (pos.y.toInt() - range)
        val maxY = (pos.y.toInt() + range)

        val minCol = (minX / cellSize).coerceIn(0, cols - 1)
        val maxCol = (maxX / cellSize).coerceIn(0, cols - 1)
        val minRow = (minY / cellSize).coerceIn(0, rows - 1)
        val maxRow = (maxY / cellSize).coerceIn(0, rows - 1)

        for (y in minRow..maxRow) {
            for (x in minCol..maxCol) {
                val index = y * cols + x
                if (index < buckets.size) {
                    queryBuffer.addAll(buckets[index])
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