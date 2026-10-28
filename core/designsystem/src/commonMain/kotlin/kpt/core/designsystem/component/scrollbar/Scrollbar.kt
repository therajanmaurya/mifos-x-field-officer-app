/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.designsystem.component.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kpt.core.designsystem.theme.DesignToken
import kotlin.jvm.JvmInline
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * The delay between scrolls when a user long presses on the scrollbar track to initiate a scroll
 * instead of dragging the scrollbar thumb.
 */
private const val SCROLLBAR_PRESS_DELAY_MS = 10L

/**
 * The percentage displacement of the scrollbar when scrolled by long presses on the scrollbar
 * track.
 */
private const val SCROLLBAR_PRESS_DELTA_PCT = 0.02f

class ScrollbarState {
    private var packedValue by mutableLongStateOf(0L)

    internal fun onScroll(stateValue: ScrollbarStateValue) {
        packedValue = stateValue.packedValue
    }

    /**
     * Returns the thumb size of the scrollbar as a percentage of the total track size
     */
    val thumbSizePercent
        get() = unpackFloat1(packedValue)

    /**
     * Returns the distance the thumb has traveled as a percentage of total track size
     */
    val thumbMovedPercent
        get() = unpackFloat2(packedValue)

    /**
     * Returns the max distance the thumb can travel as a percentage of total track size
     */
    val thumbTrackSizePercent
        get() = 1f - thumbSizePercent
}

/**
 * Returns the size of the scrollbar track in pixels
 */
private val ScrollbarTrack.size
    get() = unpackFloat2(packedValue) - unpackFloat1(packedValue)

/**
 * Returns the position of the scrollbar thumb on the track as a percentage
 */
private fun ScrollbarTrack.thumbPosition(
    dimension: Float,
): Float = max(
    a = min(
        a = dimension / size,
        b = 1f,
    ),
    b = 0f,
)

/**
 * Class definition for the core properties of a scroll bar
 */
@Immutable
@JvmInline
value class ScrollbarStateValue internal constructor(
    internal val packedValue: Long,
)

/**
 * Class definition for the core properties of a scroll bar track
 */
@Immutable
@JvmInline
private value class ScrollbarTrack(
    val packedValue: Long,
) {
    constructor(
        max: Float,
        min: Float,
    ) : this(packFloats(max, min))
}

/**
 * Creates a [ScrollbarStateValue] with the listed properties
 * @param thumbSizePercent the thumb size of the scrollbar as a percentage of the total track size.
 *  Refers to either the thumb width (for horizontal scrollbars)
 *  or height (for vertical scrollbars).
 * @param thumbMovedPercent the distance the thumb has traveled as a percentage of total
 * track size.
 */
fun scrollbarStateValue(
    thumbSizePercent: Float,
    thumbMovedPercent: Float,
) = ScrollbarStateValue(
    packFloats(
        val1 = thumbSizePercent,
        val2 = thumbMovedPercent,
    ),
)

/**
 * Returns the value of [offset] along the axis specified by [this]
 */
internal fun Orientation.valueOf(offset: Offset) = when (this) {
    Orientation.Horizontal -> offset.x
    Orientation.Vertical -> offset.y
}

/**
 * Returns the value of [intSize] along the axis specified by [this]
 */
internal fun Orientation.valueOf(intSize: IntSize) = when (this) {
    Orientation.Horizontal -> intSize.width
    Orientation.Vertical -> intSize.height
}

/**
 * Returns the value of [intOffset] along the axis specified by [this]
 */
internal fun Orientation.valueOf(intOffset: IntOffset) = when (this) {
    Orientation.Horizontal -> intOffset.x
    Orientation.Vertical -> intOffset.y
}

/**
 * A Composable for drawing a scrollbar
 * @param orientation the scroll direction of the scrollbar
 * @param state the state describing the position of the scrollbar
 * @param minThumbSize the minimum size of the scrollbar thumb
 * @param interactionSource allows for observing the state of the scroll bar
 * @param thumb a composable for drawing the scrollbar thumb
 * @param onThumbMoved an function for reacting to scroll bar displacements caused by direct
 * interactions on the scrollbar thumb by the user, for example implementing a fast scroll
 */
@Composable
fun Scrollbar(
    orientation: Orientation,
    state: ScrollbarState,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    minThumbSize: Dp = DesignToken.sizes.minThumbSizeDp40,
    onThumbMoved: ((Float) -> Unit)? = null,
    thumb: @Composable () -> Unit,
) {
    // Using Offset.Unspecified and Float.NaN instead of null
    // to prevent unnecessary boxing of primitives
    var pressedOffset by remember { mutableStateOf(Offset.Unspecified) }
    var draggedOffset by remember { mutableStateOf(Offset.Unspecified) }

    // Used to immediately show drag feedback in the UI while the scrolling implementation
    // catches up
    var interactionThumbTravelPercent by remember { mutableFloatStateOf(Float.NaN) }

    var track by remember { mutableStateOf(ScrollbarTrack(packedValue = 0)) }

    // scrollbar track container
    Box(
        modifier = modifier
            .scrollbarTrackSize(orientation, interactionSource)
            .onGloballyPositioned { coordinates ->
                val scrollbarStartCoordinate = orientation.valueOf(coordinates.positionInRoot())
                track = ScrollbarTrack(
                    max = scrollbarStartCoordinate,
                    min = scrollbarStartCoordinate + orientation.valueOf(coordinates.size),
                )
            }
            // Process scrollbar presses
            .scrollbarPressGestures(interactionSource) { pressedOffset = it }
            // Process scrollbar drags
            .scrollbarDragGestures(
                orientation = orientation,
                interactionSource = interactionSource,
                draggedOffset = { draggedOffset },
                onDraggedOffsetChange = { draggedOffset = it },
            ),
    ) {
        // scrollbar thumb container
        Layout(content = { thumb() }) { measurables, constraints ->
            placeThumb(
                measurable = measurables.first(),
                constraints = constraints,
                orientation = orientation,
                state = state,
                track = track,
                minThumbSize = minThumbSize,
                interactionThumbTravelPercent = interactionThumbTravelPercent,
            )
        }
    }

    if (onThumbMoved != null) {
        ScrollbarPressEffect(
            pressedOffset = { pressedOffset },
            state = state,
            track = { track },
            orientation = orientation,
            onThumbMoved = onThumbMoved,
            onInteractionTravel = { interactionThumbTravelPercent = it },
        )
        ScrollbarDragEffect(
            draggedOffset = { draggedOffset },
            track = { track },
            orientation = orientation,
            onThumbMoved = onThumbMoved,
            onInteractionTravel = { interactionThumbTravelPercent = it },
        )
    }
}

private fun Modifier.scrollbarTrackSize(
    orientation: Orientation,
    interactionSource: MutableInteractionSource?,
): Modifier = run {
    val withHover = interactionSource?.let(::hoverable) ?: this
    when (orientation) {
        Orientation.Vertical -> withHover.fillMaxHeight()
        Orientation.Horizontal -> withHover.fillMaxWidth()
    }
}

private fun Modifier.scrollbarPressGestures(
    interactionSource: MutableInteractionSource?,
    onPressedOffsetChange: (Offset) -> Unit,
): Modifier = pointerInput(Unit) {
    detectTapGestures(
        onPress = { offset ->
            try {
                // Wait for a long press before scrolling
                withTimeout(viewConfiguration.longPressTimeoutMillis) {
                    tryAwaitRelease()
                }
            } catch (e: TimeoutCancellationException) {
                // Start the press triggered scroll
                val initialPress = PressInteraction.Press(offset)
                interactionSource?.tryEmit(initialPress)

                onPressedOffsetChange(offset)
                interactionSource?.tryEmit(
                    when {
                        tryAwaitRelease() -> PressInteraction.Release(initialPress)
                        else -> PressInteraction.Cancel(initialPress)
                    },
                )

                // End the press
                onPressedOffsetChange(Offset.Unspecified)
            }
        },
    )
}

private fun Modifier.scrollbarDragGestures(
    orientation: Orientation,
    interactionSource: MutableInteractionSource?,
    draggedOffset: () -> Offset,
    onDraggedOffsetChange: (Offset) -> Unit,
): Modifier = pointerInput(Unit) {
    var dragInteraction: DragInteraction.Start? = null
    val onDragStart: (Offset) -> Unit = { offset ->
        val start = DragInteraction.Start()
        dragInteraction = start
        interactionSource?.tryEmit(start)
        onDraggedOffsetChange(offset)
    }
    val onDragEnd: () -> Unit = {
        dragInteraction?.let { interactionSource?.tryEmit(DragInteraction.Stop(it)) }
        onDraggedOffsetChange(Offset.Unspecified)
    }
    val onDragCancel: () -> Unit = {
        dragInteraction?.let { interactionSource?.tryEmit(DragInteraction.Cancel(it)) }
        onDraggedOffsetChange(Offset.Unspecified)
    }
    val onDrag: (change: PointerInputChange, dragAmount: Float) -> Unit = { _, delta ->
        val current = draggedOffset()
        if (current != Offset.Unspecified) {
            onDraggedOffsetChange(
                when (orientation) {
                    Orientation.Vertical -> current.copy(y = current.y + delta)
                    Orientation.Horizontal -> current.copy(x = current.x + delta)
                },
            )
        }
    }

    when (orientation) {
        Orientation.Horizontal -> detectHorizontalDragGestures(
            onDragStart = onDragStart,
            onDragEnd = onDragEnd,
            onDragCancel = onDragCancel,
            onHorizontalDrag = onDrag,
        )

        Orientation.Vertical -> detectVerticalDragGestures(
            onDragStart = onDragStart,
            onDragEnd = onDragEnd,
            onDragCancel = onDragCancel,
            onVerticalDrag = onDrag,
        )
    }
}

private fun MeasureScope.placeThumb(
    measurable: Measurable,
    constraints: Constraints,
    orientation: Orientation,
    state: ScrollbarState,
    track: ScrollbarTrack,
    minThumbSize: Dp,
    interactionThumbTravelPercent: Float,
): MeasureResult {
    val thumbSizePx = max(
        a = state.thumbSizePercent * track.size,
        b = minThumbSize.toPx(),
    )

    val trackSizePx = when (state.thumbTrackSizePercent) {
        0f -> track.size
        else -> (track.size - thumbSizePx) / state.thumbTrackSizePercent
    }

    val thumbTravelPercent = max(
        a = min(
            a = when {
                interactionThumbTravelPercent.isNaN() -> state.thumbMovedPercent
                else -> interactionThumbTravelPercent
            },
            b = state.thumbTrackSizePercent,
        ),
        b = 0f,
    )

    val thumbMovedPx = trackSizePx * thumbTravelPercent

    val y = when (orientation) {
        Horizontal -> 0
        Vertical -> thumbMovedPx.roundToInt()
    }
    val x = when (orientation) {
        Horizontal -> thumbMovedPx.roundToInt()
        Vertical -> 0
    }

    val updatedConstraints = when (orientation) {
        Horizontal -> {
            constraints.copy(
                minWidth = thumbSizePx.roundToInt(),
                maxWidth = thumbSizePx.roundToInt(),
            )
        }
        Vertical -> {
            constraints.copy(
                minHeight = thumbSizePx.roundToInt(),
                maxHeight = thumbSizePx.roundToInt(),
            )
        }
    }

    val placeable = measurable.measure(updatedConstraints)
    return layout(placeable.width, placeable.height) {
        placeable.place(x, y)
    }
}

@Composable
private fun ScrollbarPressEffect(
    pressedOffset: () -> Offset,
    state: ScrollbarState,
    track: () -> ScrollbarTrack,
    orientation: Orientation,
    onThumbMoved: (Float) -> Unit,
    onInteractionTravel: (Float) -> Unit,
) {
    LaunchedEffect(Unit) {
        snapshotFlow { pressedOffset() }.collect { pressed ->
            if (pressed == Offset.Unspecified) {
                // Press ended, reset interactionThumbTravelPercent
                onInteractionTravel(Float.NaN)
            } else {
                scrollThumbTowards(
                    destination = track().thumbPosition(dimension = orientation.valueOf(pressed)),
                    start = state.thumbMovedPercent,
                    onThumbMoved = onThumbMoved,
                    onInteractionTravel = onInteractionTravel,
                )
            }
        }
    }
}

private suspend fun scrollThumbTowards(
    destination: Float,
    start: Float,
    onThumbMoved: (Float) -> Unit,
    onInteractionTravel: (Float) -> Unit,
) {
    var current = start
    val isPositive = current < destination
    val delta = SCROLLBAR_PRESS_DELTA_PCT * if (isPositive) 1f else -1f

    while (current != destination) {
        current = when {
            isPositive -> min(a = current + delta, b = destination)
            else -> max(a = current + delta, b = destination)
        }
        onThumbMoved(current)
        onInteractionTravel(current)
        delay(SCROLLBAR_PRESS_DELAY_MS)
    }
}

@Composable
private fun ScrollbarDragEffect(
    draggedOffset: () -> Offset,
    track: () -> ScrollbarTrack,
    orientation: Orientation,
    onThumbMoved: (Float) -> Unit,
    onInteractionTravel: (Float) -> Unit,
) {
    LaunchedEffect(Unit) {
        snapshotFlow { draggedOffset() }.collect { dragged ->
            if (dragged == Offset.Unspecified) {
                onInteractionTravel(Float.NaN)
            } else {
                val currentTravel = track().thumbPosition(
                    dimension = orientation.valueOf(dragged),
                )
                onThumbMoved(currentTravel)
                onInteractionTravel(currentTravel)
            }
        }
    }
}
