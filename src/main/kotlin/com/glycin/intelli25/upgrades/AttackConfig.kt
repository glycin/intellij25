package com.glycin.intelli25.upgrades

import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.UpgradePNG
import com.intellij.ui.JBColor
import java.awt.image.BufferedImage

object AttackConfig {

    val AI_FEATURE = AttackDef(
        title = "Artificial Intelligence",
        description = "Unlocks the power of AI, the latest transformative innovation in tech.",
        effect = "Weapon that adds a drone that flies around and protects Runzo.",
        unlockedByDefault = false,
        image = UpgradePNG.junie,
        availableAtLevel = 3,
        color = GameColors.jbPink,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "AI Chat",
                description = "",
                effect = "Increase rotating speed of the AI assistant",
                availableAtLevel = 3,
                image = UpgradePNG.aiChat,
            ),
            AttackUpgradeDef(
                title = "Junie",
                description = "IntelliJ added Junie",
                effect = "Add an additional AI assistant",
                availableAtLevel = 3,
                image = UpgradePNG.junie,
            ),
            AttackUpgradeDef(
                title = "Full line autocomplete",
                description = "IntelliJ added blazing fast full line autocomplete",
                effect = "Add an additional AI assistant and increase rotation speed of the drones",
                availableAtLevel = 3,
                image = UpgradePNG.tab,
            ),
        ),
    )

    val STYLE_FEATURE = AttackDef(
        title = "Impeccable style",
        description = "Serving developers with style",
        effect = "Weapon that fires a projectile to the closest enemy",
        unlockedByDefault = false,
        image = UpgradePNG.style,
        availableAtLevel = 2,
        color = GameColors.jbPurpleLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "Dark theme",
                description = "IntelliJ added a dark theme",
                effect = "Decreases weapon cooldown",
                availableAtLevel = 2,
                image = UpgradePNG.darkTheme,
            ),
            AttackUpgradeDef(
                title = "Jetbrains Monotype",
                description = "IntelliJ added the jb monotype",
                effect = "Adds an extra projectile",
                availableAtLevel = 3,
                image = UpgradePNG.monotype,
            ),
            AttackUpgradeDef(
                title = "New UI",
                description = "IntelliJ got a redesign",
                effect = "Further decreases cooldown",
                availableAtLevel = 3,
                image = UpgradePNG.communityEdition,
            ),
            AttackUpgradeDef(
                title = "Custom themes",
                description = "Intellij added custom theme support!",
                effect = "Further decreases cooldown and adds two additional projectiles",
                availableAtLevel = 3,
                image = UpgradePNG.customThemes,
            ),
        ),
    )

    val BUILD_TOOLS_FEATURE = AttackDef(
        title = "Build & Deployment tools",
        description = "Unlocks the power of build and deployment tools, which are essential to every developer nowadays",
        effect = "Weapon that spawns toxic pools around Runzo that hurt enemies",
        unlockedByDefault = false,
        image = UpgradePNG.buildTools,
        availableAtLevel = 2,
        color = GameColors.jbBlueLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "Maven 3 Integration",
                description = "Maven 3 Integration was added",
                effect = "Increase spawn rate of the pools.",
                availableAtLevel = 2,
                image = UpgradePNG.maven3,
            ),
            AttackUpgradeDef(
                title = "Gradle Support",
                description = "IntelliJ added gradle support",
                effect = "Increase size of the spawned pools",
                availableAtLevel = 2,
                image = UpgradePNG.gradle,
            ),
            AttackUpgradeDef(
                title = "Docker Support",
                description = "IntelliJ added docker support",
                effect = "Further increase pool spawn rate",
                availableAtLevel = 2,
                image = UpgradePNG.docker,
            ),
            AttackUpgradeDef(
                title = "Kubernetes support",
                description = "Added k8s support!",
                effect = "Further increase pool size",
                availableAtLevel = 3,
                image = UpgradePNG.kubernetes,
            ),
        ),
    )

    val KOTLIN_FEATURE = AttackDef(
        title = "Kotlin",
        description = "Unlocks the power of Kotlin, the JVM language made by JetBrains.",
        effect = "Weapon that randomly strikes enemies for heavy damage",
        unlockedByDefault = false,
        availableAtLevel = 2,
        image = UpgradePNG.kotlin,
        color = GameColors.jbCyanLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "Kotlin support",
                description = "IntelliJ added kotlin support",
                effect = "Decreases lightning strike cooldown",
                availableAtLevel = 2,
                image = UpgradePNG.kotlin,
            ),
            AttackUpgradeDef(
                title = "Kotlin multi-platform",
                description = "IntelliJ added support for KMP",
                effect = "Increases lighting strike impact radius",
                availableAtLevel = 3,
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "K2 Mode",
                description = "IntelliJ added support for the K2 compiler",
                effect = "Decreases lightning strike cooldown even further",
                availableAtLevel = 3,
                image = UpgradePNG.k2,
            ),
            AttackUpgradeDef(
                title = "Kotlin notebooks",
                description = "IntelliJ can create and run kotlin notebooks",
                effect = "Decreases cooldown even further and increases impact radius",
                availableAtLevel = 3,
                image = UpgradePNG.kotlinNotebook,
            ),
        ),
    )

    val VERSIONING_FEATURE = AttackDef(
        title = "Version control",
        description = "Weapon that occasionally freezes all enemies in place",
        effect = "Weapon that occasionally freezes all enemies in place",
        unlockedByDefault = false,
        image = UpgradePNG.git,
        availableAtLevel = 1,
        color = GameColors.jbYellow,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "CVS & VSS",
                description = "IntelliJ added CVS and VSS support",
                effect = "Increases the time enemies stay frozen",
                availableAtLevel = 1,
                image = UpgradePNG.cvs,
            ),
            AttackUpgradeDef(
                title = "Subversion",
                description = "IntelliJ added Subversion support",
                effect = "Increases how often this weapon activates",
                availableAtLevel = 1,
                image = UpgradePNG.subversion,
            ),
            AttackUpgradeDef(
                title = "Git",
                description = "IntelliJ added git support",
                effect = "Increases how often this weapon activates and how long enemies are frozen",
                availableAtLevel = 2,
                image = UpgradePNG.git,
            ),
        ),
    )

    val PRODUCTIVITY_FEATURE = AttackDef(
        title = "Productivity",
        description = "Your companion for maximum productivity",
        effect = "Shoots two projectiles to the left and right of Runzo.",
        unlockedByDefault = true,
        image = UpgradePNG.laptop,
        availableAtLevel = 1,
        color = GameColors.jbRedLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "HTML & CSS",
                description = "IntelliJ added html and css support",
                effect = "Increase firing speed of projectiles",
                availableAtLevel = 1,
                image = UpgradePNG.htmlCss,
            ),
            AttackUpgradeDef(
                title = "SQL Support",
                description = "IntelliJ added SQL support",
                effect = "Adds two additional projectile lines",
                availableAtLevel = 1,
                image = UpgradePNG.sqlSupport,
            ),
            AttackUpgradeDef(
                title = "Search Everywhere",
                description = "IntelliJ added the search everywhere feature",
                effect = "Further increases firing speed of projectiles",
                availableAtLevel = 2,
                image = UpgradePNG.searchAnywhere,
            ),
            AttackUpgradeDef(
                title = "Embedded terminal",
                description = "Added an embedded terminal!",
                effect = "Adds two additional projectile lines",
                availableAtLevel = 2,
                image = UpgradePNG.terminal,
            ),
            AttackUpgradeDef(
                title = "Debugger",
                description = "Added an embedded debugger!",
                effect = "Increase firing speed of projectiles",
                availableAtLevel = 2,
                image = UpgradePNG.duck,
            ),
            AttackUpgradeDef(
                title = "Decompiler",
                description = "Added a decompiler for fast peeking!",
                effect = "Fire additional projectile lines",
                availableAtLevel = 2,
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "JDK in the IDE",
                description = "Now you can choose your JDK in the IDE!",
                effect = "Increase firing speed to the max!",
                availableAtLevel = 3,
                image = UpgradePNG.duke,
            ),
            AttackUpgradeDef(
                title = "Command completion",
                description = "Added the command completion feature!",
                effect = "Maximum productivity! Projectiles no longer disappear after hitting an enemy",
                availableAtLevel = 3,
                image = UpgradePNG.commandCompletion,
            ),
        ),
    )

    val ENTERPRISE_READY_FEATURE = AttackDef(
        title = "Enterprise Ready",
        description = "Unlocks the power of integrations for Enterprise grade production environments",
        effect = "Weapon that adds a damaging area around Runzo.",
        unlockedByDefault = false,
        availableAtLevel = 1,
        image = UpgradePNG.leaf,
        color = GameColors.jbOrangeLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "JUnit Integration",
                description = "IntelliJ added JUnit integration in IntelliJ",
                effect = "Increase size of protective area",
                availableAtLevel = 1,
                image = UpgradePNG.junit,
            ),
            AttackUpgradeDef(
                title = "J2EE Support",
                description = "IntelliJ added J2EE support",
                effect = "Increase size of protective area",
                availableAtLevel = 1,
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Spring Framework",
                description = "IntelliJ support for Spring framework",
                effect = "Increase size of protective area",
                availableAtLevel = 1,
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Spring boot",
                description = "Added support for spring boot!",
                effect = "Increase size of protective area",
                availableAtLevel = 2,
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Profiler",
                description = "Added a built in profiler",
                effect = "Increase size of protective area",
                availableAtLevel = 3,
                image = UpgradePNG.profiler,
            ),
            AttackUpgradeDef(
                title = "Spring debugger",
                description = "Added a spring debugger!",
                effect = "Increase size of protective area",
                availableAtLevel = 3,
                image = UpgradePNG.springDebugger,
            ),
        ),
    )

    val ALL_FEATURES: List<AttackDef> = listOf(
        AI_FEATURE,
        STYLE_FEATURE,
        BUILD_TOOLS_FEATURE,
        KOTLIN_FEATURE,
        VERSIONING_FEATURE,
        PRODUCTIVITY_FEATURE,
        ENTERPRISE_READY_FEATURE,
    )
}

data class AttackDef(
    val title: String,
    val description: String,
    val effect: String,
    val unlockedByDefault: Boolean,
    val image: BufferedImage?,
    val availableAtLevel: Int,
    val upgrades: List<AttackUpgradeDef>,
    val color: JBColor,
    val textColor: JBColor,
)

data class AttackUpgradeDef(
    val title: String,
    val description: String,
    val effect: String,
    val image: BufferedImage?,
    val availableAtLevel: Int,
)