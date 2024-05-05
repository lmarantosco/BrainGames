package com.example.coroutinestest2.ui.HelperClasses

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class NewPolygonShape(val sides: Int, val radius: Float) : Shape {
    init {
        require(sides >= 3) { "A polygon must have at least 3 sides." }
    }


    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path()
        val angleIncrement = 2 * PI / sides // Angle between vertices

        // Calculate the starting point (top point of the polygon)
        val startX = (size.width / 2) + (radius * cos(-PI / 2)).toFloat()
        val startY = (size.height / 2) + (radius * sin(-PI / 2)).toFloat()

        path.moveTo(startX, startY)

        // Calculate the position of each vertex and add it to the path
        for (i in 1 until sides) {
            val angle = -PI / 2 + i * angleIncrement
            val x = (size.width / 2) + (radius * cos(angle)).toFloat()
            val y = (size.height / 2) + (radius * sin(angle)).toFloat()
            path.lineTo(x, y)
        }

        path.close() // Close the path to complete the polygon
        return Outline.Generic(path)    }
}