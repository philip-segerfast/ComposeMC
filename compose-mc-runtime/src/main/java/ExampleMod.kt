package com.example.examplemod

import androidx.compose.runtime.*
import com.example.examplemod.screen.ComposeScreen
import com.example.examplemod.screen.Text
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.delay
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import kotlin.time.Duration.Companion.milliseconds

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(ExampleMod.ID)
object ExampleMod {
    const val ID = "examplemod"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "Hello world!")

        // Register the KDeferredRegister to the mod-specific event bus
//        ModBlocks.REGISTRY.register(MOD_BUS)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
                FORGE_BUS.addListener(::playerLoggedInEvent)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })

        println(obj)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    val x = atomic(11)

    private fun playerLoggedInEvent(event: PlayerLoggedInEvent) {
        LOGGER.log(Level.INFO, "PlayerLoggedInEvent")

        Minecraft.getInstance().execute {
            val composeScreen = ComposeScreen(Component.empty()) {
                ComposeScreenContent()
            }
            Minecraft.getInstance().setScreen(composeScreen)
        }
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
















