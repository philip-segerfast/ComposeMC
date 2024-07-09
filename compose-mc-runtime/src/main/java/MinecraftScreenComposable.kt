package com.example.examplemod

import androidx.compose.runtime.ComposableTargetMarker

/**
 * An annotation that can be used to mark a composable function as being expected to be used in a
 * composable function that is also marked or inferred to be marked as a [MinecraftScreenComposable].
 *
 * This will produce build warnings when [MinecraftScreenComposable] composable functions are used outside of
 * a [MinecraftScreenComposable] content lambda, and vice versa.
 */
@Retention(AnnotationRetention.BINARY)
@ComposableTargetMarker(description = "Minecraft Screen Composable")
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
)
annotation class MinecraftScreenComposable
