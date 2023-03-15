package com.muamuathu.app.presentation.event

import com.muamuathu.app.presentation.graph.NavTarget

sealed class NavEvent {
    object None : NavEvent()
    class NavigateUp : NavEvent() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class Action(val target: NavTarget) : NavEvent()

    class ActionWithValue(val target: NavTarget, val value: Pair<String, String>) : NavEvent()

    class ActionWithPopUp(
        val target: NavTarget,
        val popupTarget: NavTarget,
        val inclusive: Boolean = true
    ) : NavEvent()

    class ActionInclusive(val target: NavTarget, val inclusiveTarget: NavTarget) : NavEvent()

    class PopBackStackWithTarget(val target: NavTarget, val inclusive: Boolean = false) : NavEvent()
    class PopBackStack(val inclusive: Boolean = false) : NavEvent()
    class ActionWeb(val url: String) : NavEvent()
}
