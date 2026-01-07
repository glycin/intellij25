package com.glycin.intelli25.upgrades

import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.UpgradePNG
import com.intellij.ui.JBColor
import java.awt.image.BufferedImage

enum class UpgradeBoostDef(
    val title: String,
    val description: String,
    val effect: String,
    val image: BufferedImage?,
    val color: JBColor,
) {
    PIZZA(
        title = "Pizza slice",
        description = "Yum",
        effect = "Increases your health regeneration rate",
        image = UpgradePNG.pizza,
        color = GameColors.boost1
    ),
    COFFEE(
        title = "Coffee",
        description = "A tasty sip for a bolt of energy",
        effect = "Increases your movement speed",
        image = UpgradePNG.coffee,
        color = GameColors.boost2
    ),
    PERFORMANCE(
        title = "Performance fix",
        description = "Code running so well!",
        effect = "Increases your total health and heals Runzo",
        image = UpgradePNG.performance,
        color = GameColors.boost3
    ),
    FIREWALL(
        title = "Firewall shield",
        description = "The best defense is...",
        effect = "Decreases enemy speed",
        image = UpgradePNG.firewall,
        color = GameColors.boost4
    ),
    AUTO_REFACTORING(
        title = "Refactorings",
        description = "For when you don't want to do things yourself",
        effect = "Increases your damage",
        image = UpgradePNG.refactorings,
        color = GameColors.boost5
    ),
    INTENTION_ACTIONS(
        title = "ALT + ENTER",
        description = "For when your intentions are clear",
        effect = "Decrease the amount of enemies spawned",
        image = UpgradePNG.intentions,
        color = GameColors.boost6
    ),
    CODE_INSPECTIONS(
        title = "Code Inspections",
        description = "Go go gadget inspections!",
        effect = "Increase your experience pickup range",
        image = UpgradePNG.inspections,
        color = GameColors.boost7
    ),
    DUKE(
        title = "The Duke",
        description = "Every new version of the Duke, IntelliJ is right there",
        effect = "Increase the amount of experience gained",
        image = UpgradePNG.duke,
        color = GameColors.boost8
    ),
    CODE_FREEZE(
        title = "Code freeze",
        description = "For when you need to be sure nothing will break",
        effect = "Decrease the speed in which new enemies appear",
        image = UpgradePNG.freeze,
        color = GameColors.boost9
    ),
    PUSH_TO_PROD(
        title = "Push to Prod",
        description = "For when you feel adventurous",
        effect = "Increase the amount of enemies appearing",
        image = UpgradePNG.push,
        color = GameColors.boost10
    );
}
