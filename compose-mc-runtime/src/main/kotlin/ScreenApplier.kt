package com.example.examplemod

import androidx.compose.runtime.AbstractApplier
import com.example.examplemod.screen.ComposeScreen

private object ScreenNodeRoot : ScreenNode

interface ScreenNode {
    // Unsure if this is needed or not. We'll find out...
//    /** The node should be completely cleared. Remove everything. */
//    fun onCleared() {}
    /** Node has been attached to the hierarchy. Add it to the screen. */
    fun onAttached() {}
    /** Node has been removed from the hierarchy. Remove it from screen. */
    fun onRemoved() {}
}

class ScreenApplier(
    val screen: ComposeScreen
) : AbstractApplier<ScreenNode>(ScreenNodeRoot) {

    private val decorations = mutableListOf<ScreenNode>()

    override fun onClear() {
//        decorations.forEach { it.onCleared() } // Not needed?
        decorations.clear()
    }

    override fun insertBottomUp(index: Int, instance: ScreenNode) {
        decorations.add(index, instance)
        instance.onAttached()
    }

    override fun insertTopDown(index: Int, instance: ScreenNode) {
        // insertBottomUp is preferred
    }

    override fun move(from: Int, to: Int, count: Int) {
        decorations.move(from, to, count)
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            decorations[index + it].onRemoved()
        }
        decorations.remove(index, count)
    }

}