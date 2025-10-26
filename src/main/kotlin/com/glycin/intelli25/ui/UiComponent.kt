package com.glycin.intelli25.ui

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.observable.util.addComponent
import com.intellij.openapi.ui.popup.JBPopupFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.swing.JComponent
import javax.swing.JPanel

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

    fun showUpgradePopup(
        options: List<UpgradeOption>
    ) {
        val panel = UpgradeMenu(options)

        val popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(panel, null)
            .setRequestFocus(true)
            .setFocusable(true)
            .setModalContext(true)
            .setCancelOnClickOutside(false)
            .setCancelOnOtherWindowOpen(false)
            .setCancelKeyEnabled(false)
            .setTitle("LEVEL UP!")
            .createPopup()

        // Pass popup reference to cards so they can close it
        panel.components.forEach { comp ->
            if (comp is JPanel) {
                comp.components.forEach { innerComp ->
                    if (innerComp is UpgradeMenu.UpgradeCard) {
                        innerComp.setPopupReference(popup)
                    }
                }
            }
        }

        scope.launch(Dispatchers.EDT) { popup.showInCenterOf(parent) }
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}