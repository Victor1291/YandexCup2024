package ru.kartollika.yandexcup.canvas.compose

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventPass.Main
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.PI
import kotlin.math.abs

suspend fun PointerInputScope.detectPointerTransformGestures(
  panZoomLock: Boolean = false,
  numberOfPointers: Int = 1,
  pass: PointerEventPass = Main,
  consume: Boolean = true,
  onDragStart: (Offset) -> Unit = {},
  onDrag: (Offset) -> Unit = {},
  onDragCancelled: () -> Unit = {},
  onTransform:
    (centroid: Offset, pan: Offset, zoom: Float, rotation: Float, mainPointer: PointerInputChange, changes: List<PointerInputChange>) -> Unit,
  onDragEnd: (PointerInputChange) -> Unit = {},
) {

  require(numberOfPointers > 0) {
    "Number of minimum pointers should be greater than 0"
  }

  awaitEachGesture {
    var rotation = 0f
    var zoom = 1f
    var pan = Offset.Zero
    var pastTouchSlop = false
    val touchSlop = viewConfiguration.touchSlop
    var lockedToPanZoom = false

    var gestureStarted = false
    var dragStarted = false

    // Wait for at least one pointer to press down, and set first contact position
    val down: PointerInputChange = awaitFirstDown(
      requireUnconsumed = false,
      pass = pass
    )

    var pointer = down
    // Main pointer is the one that is down initially
    var pointerId = down.id
    var unforgivingChange = 0f
    val unforgivingChangeThreshold = 50f

    do {
      val event = awaitPointerEvent(pass = pass)

      val downPointerCount = event.changes.map {
        it.pressed
      }.size

      val requirementFulfilled = downPointerCount > numberOfPointers

      // If any position change is consumed from another PointerInputChange
      // or pointer count requirement is not fulfilled
      val canceled =
        event.changes.any { it.isConsumed }

      if (!canceled && requirementFulfilled && unforgivingChangeThreshold > unforgivingChange) {
        gestureStarted = true
        onDragCancelled()
        // Get pointer that is down, if first pointer is up
        // get another and use it if other pointers are also down
        // event.changes.first() doesn't return same order
        val pointerInputChange =
          event.changes.firstOrNull { it.id == pointerId }
            ?: event.changes.first()

        // Next time will check same pointer with this id
        pointerId = pointerInputChange.id
        pointer = pointerInputChange

        val zoomChange = event.calculateZoom()
        val rotationChange = event.calculateRotation()
        val panChange = event.calculatePan()

        if (!pastTouchSlop) {
          zoom *= zoomChange
          rotation += rotationChange
          pan += panChange

          val centroidSize = event.calculateCentroidSize(useCurrent = false)
          val zoomMotion = abs(1 - zoom) * centroidSize
          val rotationMotion =
            abs(rotation * PI.toFloat() * centroidSize / 180f)
          val panMotion = pan.getDistance()

          if (zoomMotion > touchSlop ||
            rotationMotion > touchSlop ||
            panMotion > touchSlop
          ) {
            pastTouchSlop = true
            lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
          }
        }

        if (pastTouchSlop) {
          val centroid = event.calculateCentroid(useCurrent = false)
          val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
          if (effectiveRotation != 0f ||
            zoomChange != 1f ||
            panChange != Offset.Zero
          ) {
            onTransform(
              centroid,
              panChange,
              zoomChange,
              effectiveRotation,
              pointer,
              event.changes
            )
          }

          if (consume) {
            event.changes.forEach {
              if (it.positionChanged()) {
                it.consume()
              }
            }
          }
        }
      } else {
        val change = event.changes.first()
        if (!dragStarted) {
          dragStarted = true
          onDragStart(change.position)
        }
        if (gestureStarted) return@awaitEachGesture
        onDrag(change.positionChange())
        unforgivingChange += change.positionChange().getDistance()
      }
    } while (!canceled && event.changes.any { it.pressed })

    onDragEnd(pointer)
  }
}