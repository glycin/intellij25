package com.glycin.intelli25.ui

import java.awt.Point
import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent

/**
 * Utility for making windows draggable by a specific component
 */
object DragWindowUtil {
    /**
     * Makes the given window draggable by dragging the specified component.
     * This is useful for creating draggable title bars or headers in custom dialogs.
     *
     * @param component The component that should act as a drag handle
     * @param window The window to be moved when dragging the component
     */
    fun setupDragListeners(component: JComponent, window: Window) {
        val mouseAdapter = MyMouseListener(window)
        component.addMouseListener(mouseAdapter)
        component.addMouseMotionListener(mouseAdapter)
    }

    private class MyMouseListener(private val window: Window) : MouseAdapter() {
        private var dragOffset: Point? = null

        override fun mousePressed(e: MouseEvent) {
            // Store the mouse position on screen when drag starts
            dragOffset = e.locationOnScreen
        }

        override fun mouseDragged(e: MouseEvent) {
            dragOffset?.let { startPos ->
                val currentPos = e.locationOnScreen
                val windowLocation = window.location

                // Calculate new window location based on drag delta
                window.location = Point(
                    windowLocation.x + (currentPos.x - startPos.x),
                    windowLocation.y + (currentPos.y - startPos.y)
                )

                // Update drag offset for next iteration
                dragOffset = currentPos
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            dragOffset = null
        }
    }
}
