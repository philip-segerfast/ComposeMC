package com.example.examplemod

import androidx.compose.runtime.*
import com.example.examplemod.screen.Text
import com.mojang.blaze3d.platform.InputConstants
import kotlinx.coroutines.delay
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import kotlin.jvm.internal.Intrinsics
import kotlin.time.Duration.Companion.milliseconds

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(MinecraftCompose.ID)
object MinecraftCompose {

    const val ID = "minecraftcompose"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        // Register the KDeferredRegister to the mod-specific event bus
//        ModBlocks.REGISTRY.register(MOD_BUS)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(MinecraftCompose::onClientSetup)
                FORGE_BUS.addListener(MinecraftCompose::onLeftClickEvent)
                FORGE_BUS.addListener(MinecraftCompose::composeEvent)
            },
            serverTarget = {
                MOD_BUS.addListener(MinecraftCompose::onServerSetup)
                "test"
            })

        println(obj)

        println("Initializing screen...")

//        val x: @Composable () -> Unit = {}
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    private fun composeEvent(event: InputEvent.Key) {
        println("Key: ${event.key}")
        println("Action: ${event.action}")
        if(event.key == InputConstants.KEY_C) {
            setCompose()
        }
    }

    private fun setCompose() {
        println("BLAH!!!!!!!")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    private fun onLeftClickEvent(event: PlayerInteractEvent.LeftClickEmpty) {
        LOGGER.log(Level.INFO, "PlayerLoggedInEvent - BEFORE. Current thread: ${ Thread.currentThread() }")
        Intrinsics.checkNotNullParameter("", "")
        LOGGER.log(Level.INFO, "EXECUTE - AFTER")

//        val composeScreen = ComposeScreen(Component.empty()) {
//            ComposeScreenContent()
//        }

        val screen = MyOtherScreen()

        Minecraft.getInstance().setScreen(screen)

        screen.setContent {  }

        LOGGER.log(Level.INFO, "AFTER THE CRASH??")
    }

}

class MyOtherScreen : Screen(Component.literal("Whatever")) {
    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        StringWidget(Component.literal("This is a String"), Minecraft.getInstance().font).render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    fun setContent(content: @Composable () -> Unit) {

    }
}

@Composable
fun ComposeScreenContent() {
    var text by remember { mutableStateOf("Hello World!") }

    LaunchedEffect(Unit) {
        repeat(100) { num ->
            text = "Hello World: $num"
            delay(500.milliseconds)
        }
    }

    Text(text)
}
