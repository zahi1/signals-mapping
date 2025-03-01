package com.example.project_mis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Custom MapView class to display a grid and plot points with coordinates.
 * This view plots the points on a grid, and each point is represented with its ID label.
 */
class MapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Paint object for drawing points (in red)
    private val pointPaint = Paint().apply {
        color = Color.RED // Set point color to red
        style = Paint.Style.FILL // Fill style for circles
        isAntiAlias = true // Smooth edges
    }

    // Paint object for drawing text labels for each point
    private val textPaint = Paint().apply {
        color = Color.BLACK // Set text color to black
        textSize = 30f // Set text size
        isAntiAlias = true // Smooth text edges
    }

    // Paint object for drawing grid lines
    private val gridPaint = Paint().apply {
        color = Color.LTGRAY // Light gray color for the grid
        strokeWidth = 2f // Set grid line thickness
    }

    // Paint object for drawing axes (x and y axes)
    private val axisPaint = Paint().apply {
        color = Color.BLACK // Black color for axes
        strokeWidth = 3f // Thicker lines for axes
        textSize = 30f // Set text size for axis labels
        isAntiAlias = true // Smooth text and line edges
    }

    // List of points (coordinates) to be plotted on the map
    var coordinates: List<StrengthPoint> = emptyList()
        set(value) {
            field = value // Update the list of coordinates
            invalidate() // Redraw the view whenever coordinates are updated
        }

    // Overridden onDraw method to handle custom drawing
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas) // Draw the grid
        drawCoordinates(canvas) // Draw the coordinates on top of the grid
    }

    /**
     * Draws a grid on the canvas to represent x and y axes.
     */
    private fun drawGrid(canvas: Canvas) {
        val padding = 100f // Padding around the grid
        val xRange = 20 // Horizontal range of the grid (for x-axis)
        val yRange = 70 // Vertical range of the grid (for y-axis)
        val xStep = (width - 2 * padding) / xRange // Calculate step size for x-axis
        val yStep = (height - 2 * padding) / yRange // Calculate step size for y-axis

        // Draw vertical lines and x-axis labels
        for (i in -10..10) {
            val x = padding + (i + 10) * xStep // Calculate x position for the line
            canvas.drawLine(x, padding, x, height - padding, gridPaint) // Draw vertical grid line
            canvas.drawText(i.toString(), x, height - padding + 30f, axisPaint) // Label x-axis
        }

        // Draw horizontal lines and y-axis labels
        for (j in -35..35 step 5) {
            val y = height - padding - (j + 35) * yStep // Calculate y position for the line
            canvas.drawLine(padding, y, width - padding, y, gridPaint) // Draw horizontal grid line
            if (j != 0) canvas.drawText(j.toString(), padding - 50f, y + 10f, axisPaint) // Label y-axis, excluding 0
        }

        // Draw x-axis and y-axis lines in the center
        canvas.drawLine(padding, height / 2f, width - padding, height / 2f, axisPaint) // Draw horizontal axis
        canvas.drawLine(width / 2f, padding, width / 2f, height - padding, axisPaint) // Draw vertical axis
    }

    /**
     * Draws points on the canvas for each coordinate in the list.
     * Each point is labeled with its ID.
     */
    private fun drawCoordinates(canvas: Canvas) {
        val padding = 100f // Padding around the grid
        val xStep = (width - 2 * padding) / 20 // Calculate step size for x-axis based on grid scale
        val yStep = (height - 2 * padding) / 70 // Calculate step size for y-axis based on grid scale

        // Iterate through each point in coordinates and draw on the canvas
        coordinates.forEach { point ->
            // Calculate the actual x position for the point on the canvas
            val x = padding + (point.x + 10) * xStep
            // Calculate the actual y position for the point on the canvas
            val y = height - padding - (point.y + 35) * yStep
            canvas.drawCircle(x, y, 10f, pointPaint) // Draw the point as a small circle
            canvas.drawText("ID: ${point.id}", x + 15f, y, textPaint) // Draw the ID label next to the point
        }
    }
}
