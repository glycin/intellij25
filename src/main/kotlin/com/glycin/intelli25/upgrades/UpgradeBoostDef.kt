package com.glycin.intelli25.upgrades

import com.glycin.intelli25.util.UpgradePNG
import java.awt.image.BufferedImage

enum class UpgradeBoostDef(
    val title: String,
    val description: String,
    val effect: String,
    val image: BufferedImage?,
) {
    PIZZA(
        title = "Pizza slice",
        description = "Yum",
        effect = "Increases your health regeneration rate",
        image = UpgradePNG.pizza,
    ),
    COFFEE(
        title = "Coffee",
        description = "A tasty sip for a bolt of energy",
        effect = "Increases your movement speed",
        image = UpgradePNG.coffee,
    ),
    PERFORMANCE(
        title = "Performance fix",
        description = "Code running so well!",
        effect = "Increases your total health",
        image = UpgradePNG.performance,
    ),
    FIREWALL(
        title = "Firewall shield",
        description = "The best defense is...",
        effect = "Decreases enemy speed",
        image = UpgradePNG.firewall,
    ),
    AUTO_REFACTORING(
        title = "Auto Refactoring",
        description = "For when you don't want to do things yourself",
        effect = "Increases your damage",
        image = UpgradePNG.refactorings,
    ),
    INTENTION_ACTIONS(
        title = "Intention Actions",
        description = "For when your intentions are clear",
        effect = "Decrease the amount of enemies spawned",
        image = UpgradePNG.intentions,
    ),
    CODE_INSPECTIONS(
        title = "Code Inspections",
        description = "Go go gadget inspections!",
        effect = "Increase your experience pickup range",
        image = UpgradePNG.inspections,
    ),
    DUKE(
        title = "The Duke",
        description = "Every new version of the Duke, IntelliJ is right there",
        effect = "Increase the amount of experience gained",
        image = UpgradePNG.duke,
    ),
    CODE_FREEZE(
        title = "Code freeze",
        description = "For when you need to be sure nothing will break",
        effect = "Decrease the speed in which new enemies appear",
        image = UpgradePNG.freeze,
    ),
    PUSH_TO_PROD(
        title = "Push to Prod",
        description = "For when you feel adventurous",
        effect = "Increase the amount of enemies appearing",
        image = UpgradePNG.push,
    );
}
