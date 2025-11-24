package com.yumedev.seijakulist.util

sealed class UserAction {
    data class UpdateProgress(val newProgress: Int) : UserAction()
    data object MarkAsWatching : UserAction()
    data object MarkAsCompleted : UserAction()
    data object MarkAsDropped : UserAction()
    data object MarkAsPlanned : UserAction()
    data object MarkAsPending : UserAction()
    data object None : UserAction()
}