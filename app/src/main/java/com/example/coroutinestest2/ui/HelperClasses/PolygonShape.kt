package com.example.coroutinestest2.ui.HelperClasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.view.View

// Custom shape class for drawing a polygon with n sides
class PolygonShape(
    private val sides: Int, // Number of sides
    private val radius: Float, // Radius from the center to vertices
    private val rotationAngle: Float = 0f, // Rotation angle in degrees,
    private val paint2: Paint
) : Shape() {
    override fun draw(canvas: Canvas, paint: Paint) {
        if (sides < 3) return // A polygon must have at least 3 sides

        val path = Path()
        val angleIncrement = (2 * Math.PI / sides).toFloat() // Angle between vertices
        val rotationRadians = Math.toRadians(rotationAngle.toDouble()).toFloat() // Rotation in radians

        for (i in 0 until sides) {
            val angle = i * angleIncrement + rotationRadians
            val x = (radius * Math.cos(angle.toDouble())).toFloat()
            val y = (radius * Math.sin(angle.toDouble())).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close() // Complete the polygon

        // Move the path to the center of the canvas
        val bounds = RectF()
        path.computeBounds(bounds, true)
        val dx = (canvas.width / 2 - bounds.centerX())
        val dy = (canvas.height / 2 - bounds.centerY())
        path.offset(dx, dy)

        // Draw the polygon
        canvas.drawPath(path, paint2)
    }
}

// Custom view to display a polygon with a specific number of sides
