package io.rybalkinsd.kotlinbootcamp.geometry

import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.max
import kotlin.math.min

/**
 * Entity that can physically intersect, like flame and player
 */
interface Collider {
    fun isColliding(other: Collider): Boolean
}

/**
 * 2D point with integer coordinates
 */
class Point(val x: Int, val y: Int) : Collider {
    override fun isColliding(other: Collider): Boolean {
        if (other is Point) return (x == other.x) and (y == other.y)
        else if (other is Bar) return other.isInBar(this)
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (other is Point) return (x == other.x) and (y == other.y)
        return false
    }
}

/**
 * Bar is a rectangle, which borders are parallel to coordinate axis
 * Like selection bar in desktop, this bar is defined by two opposite corners
 * Bar is not oriented
 * (It does not matter, which opposite corners you choose to define bar)
 */
class Bar(firstCornerX: Int, firstCornerY: Int, secondCornerX: Int, secondCornerY: Int) : Collider {
    private val leftLowerX = min(firstCornerX, secondCornerX)
    private val leftLowerY = min(firstCornerY, secondCornerY)
    private val rightUpperX = max(firstCornerX, secondCornerX)
    private val rightUpperY = max(firstCornerY, secondCornerY)

    override fun equals(other: Any?): Boolean {
        if (other is Bar) return (leftLowerX == other.leftLowerX) and
                (leftLowerY == other.leftLowerY) and
                (rightUpperX == other.rightUpperX) and
                (rightUpperY == other.rightUpperY)
        return false
    }

    fun isInBar(point: Point) :Boolean {
        return ((leftLowerX - point.x) * (rightUpperX - point.x) <= 0) and
                ((leftLowerY - point.y) * (rightUpperY - point.y) <= 0)
    }

    override fun isColliding(other: Collider): Boolean {
        if (other is Point) return isInBar(other)
        else if (other is Bar) return isInBar(Point(other.leftLowerX, other.leftLowerY)) or
                isInBar(Point(other.rightUpperX, other.rightUpperY)) or
                other.isInBar(Point(leftLowerX, leftLowerY)) or
                other.isInBar(Point(rightUpperX, rightUpperY))
        return false
    }
}