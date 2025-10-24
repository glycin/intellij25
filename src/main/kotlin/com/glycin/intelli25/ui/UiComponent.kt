package com.glycin.intelli25.ui

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.util.addComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.swing.JComponent

class UiComponent(
    private val player: Player,
    private val ggState: GameGlobalState,
    private val scope: CoroutineScope,
): JComponent(), Disposable {

    private var active = true
    private var dialogComponent: DialogComponent? = null
    private var gameUiComponent: InGameComponent? = null

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                repaint()
                delay(ggState.deltaTime)
            }
        }
    }

    fun showDialogBox(text: String) {
        showDialogBox(listOf(text))
    }

    fun showDialogBox(texts: List<String>) {
        if(dialogComponent != null) { return }
        dialogComponent = DialogComponent(texts, ggState, scope)
        addComponent(dialogComponent!!)
        revalidate()
        repaint()
    }

    fun showGameUi() {
        if(gameUiComponent != null) { return }
        gameUiComponent = InGameComponent(player, ggState)
        addComponent(gameUiComponent!!)
        revalidate()
        repaint()
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}