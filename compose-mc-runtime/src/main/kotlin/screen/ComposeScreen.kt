package com.example.examplemod.screen

import androidx.compose.runtime.*
import com.example.examplemod.ExampleMod
import com.example.examplemod.MinecraftScreenComposable
import com.example.examplemod.ScreenApplier
import com.example.examplemod.ScreenNode
import kotlinx.coroutines.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.apache.logging.log4j.Level

class ComposeScreen(
    title: Component,
    private val content: @Composable @MinecraftScreenComposable () -> Unit
): Screen(title) {
    // The composition will run inside of this scope.
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var compositionJob: Job

    override fun init() {
        compositionJob = scope.launchComposition(this@ComposeScreen, content)
    }

    // T extends GuiEventListener & Renderable & NarratableEntry

    internal fun <T> doAddRenderableWidget(widget: T) where T : GuiEventListener, T : Renderable, T : NarratableEntry {
        addRenderableWidget(widget)
        clearWidgets()
    }

    internal fun doClearWidgets() {
        super.clearWidgets()
    }

    internal fun doRemoveWidget(widget: Renderable) {
//        removeWidget()
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        ExampleMod.LOGGER.log(Level.INFO, "Rendering menu")
    }

    override fun renderBackground(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    override fun onClose() {
        // Stop any handlers here
        scope.cancel()

        // Call last in case it interferes with the override
        super.onClose()
    }

    override fun removed() {
        // Reset initial states here

        // Call last in case it interferes with the override
        super.removed()
    }
}

private fun CoroutineScope.launchComposition(
    screen: ComposeScreen,
    content: @Composable () -> Unit
): Job = launch(start = CoroutineStart.UNDISPATCHED) {
    val composition = Composition(
        applier = ScreenApplier(screen),
        parent = Recomposer(coroutineContext)
    )

    try {
        composition.setContent(content)
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}

@Composable
fun Text(text: String) {
    TextImpl(text)
}

object Fonts {
    val DEFAULT: Font = Minecraft.getInstance().font
}

@Composable
internal fun TextImpl(text: String) {
    val applier = currentComposer.applier as? ScreenApplier

    ComposeNode<TextNode, ScreenApplier>(
        factory = {
            val stringWidget = StringWidget(Component.literal(text), Fonts.DEFAULT)
            applier?.screen?.doAddRenderableWidget(stringWidget)
            TextNode(
                stringWidget,
                onAttachedCallback = { applier?.screen?.doAddRenderableWidget(stringWidget) },
                onRemovedCallback = {
                    applier?.screen?.doAddRenderableWidget(stringWidget)
                }
            )
        },
        update = {
            update(text) { stringWidget.message = Component.literal(text) }
        }
    )
}

internal class TextNode(
    val stringWidget: StringWidget,
    private val onAttachedCallback: () -> Unit,
    private val onRemovedCallback: () -> Unit
) : ScreenNode {
    override fun onAttached() {
        onAttachedCallback
    }

    override fun onRemoved() {
        onRemovedCallback()
    }
}
