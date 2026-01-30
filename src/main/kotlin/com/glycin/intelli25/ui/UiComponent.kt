package com.glycin.intelli25.ui

import com.glycin.intelli25.model.Animation
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.observable.util.addComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.Rectangle
import javax.swing.JComponent
import javax.swing.JPanel
import kotlin.math.roundToInt

class UiComponent(
    private val project: Project,
    private val player: Player,
    private val ggState: GameGlobalState,
    private val scope: CoroutineScope,
): JComponent(), Disposable {

    private val confetti = SpriteSheetImageLoader.loadSprites("/sprites/effects/confetti.png", 512, 512, 64)

    private var gameUiComponent: InGameComponent? = null
    private val upgradeAnimations: MutableList<Animation> = mutableListOf()

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
            .createComponentPopupBuilder(panel, panel)
            .setRequestFocus(true)
            .setFocusable(true)
            .setModalContext(true)
            .setCancelOnClickOutside(false)
            .setCancelOnOtherWindowOpen(false)
            .setCancelOnWindowDeactivation(false)
            .setCancelKeyEnabled(false)
            .setTitle("LEVEL UP!")
            .setMovable(true)
            .createPopup()

        panel.components.forEach { comp ->
            if (comp is JPanel) {
                comp.components.forEach { innerComp ->
                    if (innerComp is UpgradeMenu.UpgradeCard) {
                        innerComp.setPopupReference(popup)
                    }
                }
            }
        }

        val animCount = 4
        val segmentWidth = ggState.maxX / animCount
        (0 until animCount).forEach { i ->
            val centerX = (segmentWidth * i) + (segmentWidth / 2) - 256f
            val animation = Animation(
                position = Vec2(centerX, ggState.maxY - 256f),
                sprites = confetti,
                loop = false,
                frameDelay = 1,
            )
            upgradeAnimations.add(animation)
        }

        scope.launch(Dispatchers.EDT) { popup.showInCenterOf(parent) }
    }

    fun updateBounds(newBounds: Rectangle) {
        this.bounds = newBounds
        gameUiComponent?.bounds = newBounds
    }

    fun showGameQuitConfirmationDialog(
        onQuit: () -> Unit
    ) {
        if (ggState.inUpgradeMenu) {
            return
        }

        ggState.inUpgradeMenu = true

        val dialog = GameQuitConfirmationDialog(
            project = project,
            onResume = {
                ggState.inUpgradeMenu = false
            },
            onQuit = {
                ggState.inUpgradeMenu = false
                onQuit()
            }
        )

        dialog.show()
    }

    /**
     * Shows a blocking game quit confirmation dialog for editor close confirmation.
     * This method blocks execution until the user makes a choice.
     *
     * @return true if user wants to quit, false if user wants to resume
     */
    fun showGameQuitConfirmationDialogBlocking(): Boolean {
        if (ggState.inUpgradeMenu) {
            return false
        }

        ggState.inUpgradeMenu = true

        val dialog = GameQuitConfirmationDialog(
            project = project,
            title = "CLOSE EDITOR?",
            quitButtonLabel = "CLOSE & QUIT GAME",
            onResume = {
                ggState.inUpgradeMenu = false
            },
            onQuit = {
                ggState.inUpgradeMenu = false
            }
        )
        val result = dialog.showAndGetResult()

        ggState.inUpgradeMenu = false

        return result
    }

    override fun paintComponent(g: Graphics?) {
        if(g is Graphics2D) {
            if(upgradeAnimations.any { it.done }) {
                upgradeAnimations.clear()
            }
            upgradeAnimations.forEach { anim ->
                val sprite = anim.getCurrentSprite()
                g.drawImage(sprite.getScaledInstance(512, 512, Image.SCALE_SMOOTH), anim.position.x.roundToInt(), anim.position.y.roundToInt(), this)
                anim.doAnimation()
            }
        }

        super.paintComponent(g)
    }
    override fun dispose() {
        remove(gameUiComponent)
        revalidate()
        repaint()
        gameUiComponent = null
    }
}