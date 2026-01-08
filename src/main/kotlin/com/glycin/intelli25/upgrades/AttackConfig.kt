package com.glycin.intelli25.upgrades

import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.UpgradePNG
import com.intellij.ui.JBColor
import java.awt.image.BufferedImage

object AttackConfig {

    val AI_FEATURE = AttackDef(
        title = "Drone Weapon",
        description = "Unlocks the power of AI, the latest transformative innovation in tech.",
        effect = "Unlock a weapon that spawns a drone that circles around and damages enemies",
        unlockedByDefault = false,
        image = UpgradePNG.junie,
        availableAtLevel = 3,
        color = GameColors.jbPink,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "AI Chat",
                description = "",
                effect = "Upgrade the drone weapon by increasing the rotating speed of the drone",
                availableAtLevel = 3,
                image = UpgradePNG.aiChat,
            ),
            AttackUpgradeDef(
                title = "Junie",
                description = "IntelliJ added Junie",
                effect = "Upgrade the drone weapon by adding an additional drone",
                availableAtLevel = 3,
                image = UpgradePNG.junie,
            ),
            AttackUpgradeDef(
                title = "Full line autocomplete",
                description = "IntelliJ added blazing fast full line autocomplete",
                effect = "Upgrade the drone weapon to it's maximum by adding an additional drone and increasing the rotation speed of the drones",
                availableAtLevel = 3,
                image = UpgradePNG.tab,
            ),
        ),
    )

    val STYLE_FEATURE = AttackDef(
        title = "Firework Weapon",
        description = "Serving developers with style",
        effect = "Unlock a new weapon that fires a firework to the closest enemy and explodes on contact",
        unlockedByDefault = false,
        image = UpgradePNG.style,
        availableAtLevel = 2,
        color = GameColors.jbPurpleLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "Dark theme",
                description = "IntelliJ added a dark theme",
                effect = "Upgrade the homing firework weapon by increasing the rate at which fireworks are fired",
                availableAtLevel = 2,
                image = UpgradePNG.darkTheme,
            ),
            AttackUpgradeDef(
                title = "Jetbrains Monotype",
                description = "IntelliJ added the jb monotype",
                effect = "Upgrade the homing firework weapon by adding an extra firework",
                availableAtLevel = 3,
                image = UpgradePNG.monotype,
            ),
            AttackUpgradeDef(
                title = "New UI",
                description = "IntelliJ got a redesign",
                effect = "Upgrade the homing firework weapon by increasing the rate at which fireworks are fired",
                availableAtLevel = 3,
                image = UpgradePNG.communityEdition,
            ),
            AttackUpgradeDef(
                title = "Custom themes",
                description = "Intellij added custom theme support!",
                effect = "Upgrade the homing firework weapon to it's maximum by by increasing the rate at which fireworks are fired and add two additional fireworks",
                availableAtLevel = 3,
                image = UpgradePNG.customThemes,
            ),
        ),
    )

    val BUILD_TOOLS_FEATURE = AttackDef(
        title = "Toxic Pools Weapon",
        description = "Unlocks the power of build and deployment tools, which are essential to every developer nowadays",
        effect = "Unlock a new weapon that spawns toxic pools on a random position that hurt enemies when walked over",
        unlockedByDefault = false,
        image = UpgradePNG.buildTools,
        availableAtLevel = 2,
        color = GameColors.jbBlueLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "Maven 3 Integration",
                description = "Maven 3 Integration was added",
                effect = "Upgrade the toxic pool weapon by increasing how fast new pools spawn",
                availableAtLevel = 2,
                image = UpgradePNG.maven3,
            ),
            AttackUpgradeDef(
                title = "Gradle Support",
                description = "IntelliJ added gradle support",
                effect = "Upgrade the toxic pool weapon by increasing the size of the pools",
                availableAtLevel = 2,
                image = UpgradePNG.gradle,
            ),
            AttackUpgradeDef(
                title = "Docker Support",
                description = "IntelliJ added docker support",
                effect = "Upgrade the toxic pool weapon by increasing how fast new pools spawn",
                availableAtLevel = 2,
                image = UpgradePNG.docker,
            ),
            AttackUpgradeDef(
                title = "Kubernetes support",
                description = "Added k8s support!",
                effect = "Upgrade the toxic pool weapon to it's maximum by increasing size of the pools",
                availableAtLevel = 3,
                image = UpgradePNG.kubernetes,
            ),
        ),
    )

    val KOTLIN_FEATURE = AttackDef(
        title = "Lightning Strike Weapon",
        description = "Unlocks the power of Kotlin, the JVM language made by JetBrains.",
        effect = "Unlock a new weapon that randomly strikes enemies for heavy damage in a set interval",
        unlockedByDefault = false,
        availableAtLevel = 2,
        image = UpgradePNG.kotlin,
        color = GameColors.jbCyanLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "Kotlin support",
                description = "IntelliJ added kotlin support",
                effect = "Upgrade the lightning strike weapon by increasing how often it activates",
                availableAtLevel = 2,
                image = UpgradePNG.kotlin,
            ),
            AttackUpgradeDef(
                title = "Kotlin multi-platform",
                description = "IntelliJ added support for KMP",
                effect = "Upgrade the lightning strike weapon by increasing it's impact radius",
                availableAtLevel = 3,
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "K2 Mode",
                description = "IntelliJ added support for the K2 compiler",
                effect = "Upgrade the lightning strike weapon by increasing how often it activates",
                availableAtLevel = 3,
                image = UpgradePNG.k2,
            ),
            AttackUpgradeDef(
                title = "Kotlin notebooks",
                description = "IntelliJ can create and run kotlin notebooks",
                effect = "Upgrade the lightning strike weapon to it's maximum by decreasing it's cooldown and increasing it's impact radius",
                availableAtLevel = 3,
                image = UpgradePNG.kotlinNotebook,
            ),
        ),
    )

    val VERSIONING_FEATURE = AttackDef(
        title = "Freeze Weapon",
        description = "Weapon that occasionally freezes all enemies in place",
        effect = "Unlock a new weapon that can occasionally freeze all enemies in place",
        unlockedByDefault = false,
        image = UpgradePNG.git,
        availableAtLevel = 1,
        color = GameColors.jbYellow,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "CVS & VSS",
                description = "IntelliJ added CVS and VSS support",
                effect = "Upgrade the freeze weapon by increasing the time enemies stay frozen",
                availableAtLevel = 1,
                image = UpgradePNG.cvs,
            ),
            AttackUpgradeDef(
                title = "Subversion",
                description = "IntelliJ added Subversion support",
                effect = "Upgrade the freeze weapon by increasing how often it activates",
                availableAtLevel = 1,
                image = UpgradePNG.subversion,
            ),
            AttackUpgradeDef(
                title = "Git",
                description = "IntelliJ added git support",
                effect = "Upgrade the freeze weapon to it's maximum by increasing how often it activates and how long enemies stay frozen",
                availableAtLevel = 2,
                image = UpgradePNG.git,
            ),
        ),
    )

    val PRODUCTIVITY_FEATURE = AttackDef(
        title = "Side Shot Weapon",
        description = "Your companion for maximum productivity",
        effect = "Unlock a new weapon that launches two fast side shots simultaneously",
        unlockedByDefault = true,
        image = UpgradePNG.laptop,
        availableAtLevel = 1,
        color = GameColors.jbRedLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "HTML & CSS",
                description = "IntelliJ added html and css support",
                effect = "Upgrade the side shot weapon by increasing the firing speed of the projectiles",
                availableAtLevel = 1,
                image = UpgradePNG.htmlCss,
            ),
            AttackUpgradeDef(
                title = "SQL Support",
                description = "IntelliJ added SQL support",
                effect = "Upgrade the side shot weapon by adding two additional projectile lines",
                availableAtLevel = 1,
                image = UpgradePNG.sqlSupport,
            ),
            AttackUpgradeDef(
                title = "Search Everywhere",
                description = "IntelliJ added the search everywhere feature",
                effect = "Upgrade the side shot weapon by increasing the firing speed of projectiles",
                availableAtLevel = 2,
                image = UpgradePNG.searchAnywhere,
            ),
            AttackUpgradeDef(
                title = "Embedded terminal",
                description = "Added an embedded terminal!",
                effect = "Upgrade the side shot weapon by adding two additional projectile lines",
                availableAtLevel = 2,
                image = UpgradePNG.terminal,
            ),
            AttackUpgradeDef(
                title = "Debugger",
                description = "Added an embedded debugger!",
                effect = "Upgrade the side shot weapon by increasing the firing speed of projectiles",
                availableAtLevel = 2,
                image = UpgradePNG.duck,
            ),
            AttackUpgradeDef(
                title = "Decompiler",
                description = "Added a decompiler for fast peeking!",
                effect = "Upgrade the side shot weapon by adding two additional projectile lines",
                availableAtLevel = 2,
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "JDK in the IDE",
                description = "Now you can choose your JDK in the IDE!",
                effect = "Upgrade the side shot weapon by increasing the firing speed of projectiles to the maximum",
                availableAtLevel = 3,
                image = UpgradePNG.duke,
            ),
            AttackUpgradeDef(
                title = "Command completion",
                description = "Added the command completion feature!",
                effect = "Fully upgrade the side shot weapon by making projectiles penetrate through enemies",
                availableAtLevel = 3,
                image = UpgradePNG.commandCompletion,
            ),
        ),
    )

    val ENTERPRISE_READY_FEATURE = AttackDef(
        title = "Protective Force Field Weapon",
        description = "Unlocks the power of integrations for Enterprise grade production environments",
        effect = "Unlock a new weapon that adds a protective force field that damages enemies on contact",
        unlockedByDefault = false,
        availableAtLevel = 1,
        image = UpgradePNG.leaf,
        color = GameColors.jbOrangeLight,
        textColor = GameColors.white,
        upgrades = listOf(
            AttackUpgradeDef(
                title = "JUnit Integration",
                description = "IntelliJ added JUnit integration in IntelliJ",
                effect = "Upgrade the protective force field weapon by increasing the size of the force field",
                availableAtLevel = 1,
                image = UpgradePNG.junit,
            ),
            AttackUpgradeDef(
                title = "J2EE Support",
                description = "IntelliJ added J2EE support",
                effect = "Upgrade the protective force field weapon by increasing the size of the force field",
                availableAtLevel = 1,
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Spring Framework",
                description = "IntelliJ support for Spring framework",
                effect = "Upgrade the protective force field weapon by increasing the size of the force field",
                availableAtLevel = 1,
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Spring boot",
                description = "Added support for spring boot!",
                effect = "Upgrade the protective force field weapon by increasing the size of the force field",
                availableAtLevel = 2,
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Profiler",
                description = "Added a built in profiler",
                effect = "Upgrade the protective force field weapon by increasing the size of the force field",
                availableAtLevel = 3,
                image = UpgradePNG.profiler,
            ),
            AttackUpgradeDef(
                title = "Spring debugger",
                description = "Added a spring debugger!",
                effect = "Fully upgrade the protective force field weapon by increasing the size of the force field to it's maximum!",
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