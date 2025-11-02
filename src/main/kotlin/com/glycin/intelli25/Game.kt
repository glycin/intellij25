package com.glycin.intelli25

import com.glycin.intelli25.input.GameKeyListener
import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.CollisionsManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.getDeltaTime
import com.glycin.intelli25.ui.UiComponent
import com.glycin.intelli25.upgrades.AreaAttack
import com.glycin.intelli25.upgrades.LightningAttack
import com.glycin.intelli25.upgrades.RotatingAttack
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.util.IconUtil
import com.intellij.util.PlatformIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.KeyboardFocusManager

private const val FPS = 120L

class Game(
    private val project: Project,
    private val editor: Editor,
    private val scope: CoroutineScope,
): Disposable {

    private var gameComponent: GameComponent? = null
    private var uiComponent: UiComponent? = null
    private var ggState: GameGlobalState? = null
    private var keyListener: GameKeyListener? = null
    private lateinit var attackManager: AttackManager

    init {
        scope.launch(Dispatchers.EDT) {
            val maxX = editor.scrollingModel.visibleArea.width
            val maxY = editor.scrollingModel.visibleArea.height
            ggState = GameGlobalState(0, 0, maxX, maxY, FPS.getDeltaTime())
            val player = Player(Vec2((ggState!!.maxX / 2f) - 25, (ggState!!.maxY / 2f) + 25), ggState = ggState!!, width = 75, height = 75) {
                uiComponent?.showUpgradePopup(generateUpgradeOptions(it))
            }

            keyListener = GameKeyListener(player).also {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(it)
            }

            attackManager = AttackManager(scope, player, ggState!!)
            val enemyManager = EnemyManager(player, ggState!!, scope)
            val collisionsManager = CollisionsManager(player, enemyManager, attackManager, scope, ggState!!) //TODO: Create one update manager that handles all updating in the game
            gameComponent = GameComponent(ggState!!, player, attackManager, enemyManager, scope).also { ec ->
                ec.bounds = editor.contentComponent.bounds
                ec.isOpaque = false
                ec.isFocusable = true
                ec.requestFocusInWindow()
            }

            uiComponent = UiComponent(player, ggState!!, scope).also { uic ->
                uic.bounds = editor.contentComponent.bounds
                uic.isOpaque = false
            }

            editor.contentComponent.let { c ->
                c.add(gameComponent)
                c.add(uiComponent)
                c.setComponentZOrder(uiComponent, 0)
                c.setComponentZOrder(gameComponent, 1)
                c.repaint()
                c.revalidate()
            }

            //uiComponent?.showDialogBox(listOf("Test1", "Test2", "Test3", "Test4", "Test5"))
            initUpgrades(attackManager, player)
            uiComponent?.showGameUi()
        }
    }

    private fun generateUpgradeOptions(player: Player): List<UpgradeOption> {
        val upgrades = attackManager.getUpgrades()
        val otherUpgrades = listOf(
            UpgradeOption(IconUtil.scale(PlatformIcons.INTERFACE_ICON, null, 2.5f), "test1", "Testsatsatastastast") { ggState?.inUpgradeMenu = false },
            UpgradeOption(IconUtil.scale(PlatformIcons.INTERFACE_ICON, null, 2.5f), "test1", "Testsatsatastastast") { ggState?.inUpgradeMenu = false },
            //UpgradeOption(IconUtil.scale(PlatformIcons.INTERFACE_ICON, null, 2.5f), "test1", "Testsatsatastastast") { ggState?.inUpgradeMenu = false },
            //UpgradeOption(IconUtil.scale(PlatformIcons.INTERFACE_ICON, null, 2.5f), "test1", "Testsatsatastastast") { ggState?.inUpgradeMenu = false },
            //UpgradeOption(IconUtil.scale(PlatformIcons.INTERFACE_ICON, null, 2.5f), "test1", "Testsatsatastastast") { ggState?.inUpgradeMenu = false },
            //UpgradeOption(IconUtil.scale(PlatformIcons.INTERFACE_ICON, null, 2.5f), "test1", "Testsatsatastastast") { ggState?.inUpgradeMenu = false }
        )
        return (upgrades + otherUpgrades).shuffled().take(3)
    }

    private fun initUpgrades(attackManager: AttackManager, player: Player) {
        //attackManager.addAttack(BasicAttack(ggState!!, player, scope))
        attackManager.addAttack(AreaAttack(ggState!!, player))
        //attackManager.addAttack(RotatingAttack(ggState!!, player))
        attackManager.addAttack(LightningAttack(ggState!!, player, scope))
    }

    override fun dispose() {
        editor.contentComponent.remove(uiComponent)
        editor.contentComponent.remove(gameComponent)
        editor.contentComponent.revalidate()
        editor.contentComponent.repaint()
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyListener)
        ggState?.gameActive = false
    }
}