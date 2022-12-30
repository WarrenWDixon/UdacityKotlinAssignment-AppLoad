package com.udacity


sealed class ButtonState {
    object Start   : ButtonState()
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}