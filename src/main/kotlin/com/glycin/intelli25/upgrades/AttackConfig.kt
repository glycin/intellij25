package com.glycin.intelli25.upgrades

import com.glycin.intelli25.util.UpgradePNG
import java.awt.image.BufferedImage

object AttackConfig {

    val AI_FEATURE = AttackDef(
        title = "Artificial Intelligence",
        description = "Unlocks the power of AI, the latest transformative innovation in tech.",
        effect = "New weapon that adds a drone that flies around and protects Runzo.",
        unlockedByDefault = false,
        image = UpgradePNG.junie,
        boosts = listOf(
            AttackUpgradeDef(
                title = "AI Chat",
                description = "",
                effect = "Increase rotating speed of the AI assistant",
                image = UpgradePNG.junie,
            ),
            AttackUpgradeDef(
                title = "Junie",
                description = "IntelliJ added Junie",
                effect = "Add an additional AI assistant",
                image = UpgradePNG.junie,
            ),
            AttackUpgradeDef(
                title = "K2 Mode",
                description = "IntelliJ added support for the K2 compiler",
                effect = "Add an additional AI assistant and increase rotation speed of the drones",
                image = UpgradePNG.junie,
            ),
        ),
    )

    val STYLE_FEATURE = AttackDef(
        title = "Impeccable style",
        description = "Serving developers with style",
        effect = "New weapon that fires a projectile to the closest enemy",
        unlockedByDefault = false,
        image = UpgradePNG.style,
        boosts = listOf(
            AttackUpgradeDef(
                title = "Dark theme",
                description = "IntelliJ added a dark theme",
                effect = "Decreases weapon cooldown",
                image = UpgradePNG.style,
            ),
            AttackUpgradeDef(
                title = "Jetbrains Monotype",
                description = "IntelliJ added the jb monotype",
                effect = "Adds an extra projectile",
                image = UpgradePNG.style,
            ),
            AttackUpgradeDef(
                title = "New UI",
                description = "IntelliJ got a redesign",
                effect = "Further decreases cooldown",
                image = UpgradePNG.style,
            ),
            AttackUpgradeDef(
                title = "Custom themes",
                description = "Intellij added custom theme support!",
                effect = "Further decreases cooldown and adds two additional projectiles",
                image = UpgradePNG.style,
            ),
        ),
    )

    val BUILD_TOOLS_FEATURE = AttackDef(
        title = "Build & Deployment tools",
        description = "Unlocks the power of build and deployment tools, which are essential to every developer nowadays",
        effect = "New weapon that spawns toxic pools around Runzo that hurt enemies",
        unlockedByDefault = false,
        image = UpgradePNG.buildTools,
        boosts = listOf(
            AttackUpgradeDef(
                title = "Maven 3 Integration",
                description = "Maven 3 Integration was added",
                effect = "Increase spawn rate of the pools.",
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "Gradle Support",
                description = "IntelliJ added gradle support",
                effect = "Increase size of the spawned pools",
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "Docker Support",
                description = "IntelliJ added docker support",
                effect = "Further increase pool spawn rate",
                image = UpgradePNG.buildTools,
            ),
            AttackUpgradeDef(
                title = "Kubernetes support",
                description = "Added k8s support!",
                effect = "Further increase pool size",
                image = UpgradePNG.buildTools,
            ),
        ),
    )

    val KOTLIN_FEATURE = AttackDef(
        title = "Kotlin",
        description = "Unlocks the power of Kotlin, the JVM language made by JetBrains.",
        effect = "New weapon that randomly strikes enemies for heavy damage",
        unlockedByDefault = false,
        image = UpgradePNG.kotlin,
        boosts = listOf(
            AttackUpgradeDef(
                title = "Kotlin support",
                description = "IntelliJ added kotlin support",
                effect = "Decreases lightning strike cooldown",
                image = UpgradePNG.kotlin,
            ),
            AttackUpgradeDef(
                title = "Kotlin multi-platform",
                description = "IntelliJ added support for KMP",
                effect = "Increases lighting strike impact radius",
                image = UpgradePNG.kotlin,
            ),
            AttackUpgradeDef(
                title = "K2 Mode",
                description = "IntelliJ added support for the K2 compiler",
                effect = "Decreases lightning strike cooldown even further",
                image = UpgradePNG.kotlin,
            ),
            AttackUpgradeDef(
                title = "Kotlin notebooks",
                description = "IntelliJ can create and run kotlin notebooks",
                effect = "Decreases cooldown even further and increases impact radius",
                image = UpgradePNG.kotlin,
            ),
        ),
    )

    val VERSIONING_FEATURE = AttackDef(
        title = "Version control",
        description = "New weapon that occasionally freezes all enemies in place",
        effect = "New weapon that occasionally freezes all enemies in place",
        unlockedByDefault = false,
        image = UpgradePNG.git,
        boosts = listOf(
            AttackUpgradeDef(
                title = "CVS & VSS",
                description = "IntelliJ added CVS and VSS support",
                effect = "Increases the time enemies stay frozen",
                image = UpgradePNG.git,
            ),
            AttackUpgradeDef(
                title = "Subversion",
                description = "IntelliJ added Subversion support",
                effect = "Increases how often this weapon activates",
                image = UpgradePNG.git,
            ),
            AttackUpgradeDef(
                title = "Git",
                description = "IntelliJ added git support",
                effect = "Increases how often this weapon activates and how long enemies are frozen",
                image = UpgradePNG.git,
            ),
        ),
    )

    val PRODUCTIVITY_FEATURE = AttackDef(
        title = "Productivity",
        description = "This is already unlocked",
        effect = "This is already unlocked",
        unlockedByDefault = true,
        image = UpgradePNG.laptop,
        boosts = listOf(
            AttackUpgradeDef(
                title = "HTML & CSS Support",
                description = "IntelliJ added html and css support",
                effect = "Increase firing speed of projectiles",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "SQL Support",
                description = "IntelliJ added SQL support",
                effect = "Adds two additional projectile lines",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Search Everywhere",
                description = "IntelliJ added the search everywhere feature",
                effect = "Further increases firing speed of projectiles",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Embedded terminal",
                description = "Added an embedded terminal!",
                effect = "Adds two additional projectile lines",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Debugger",
                description = "Added an embedded debugger!",
                effect = "Increase firing speed of projectiles",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Decompiler",
                description = "Added a decompiler for fast peeking!",
                effect = "Fire additional projectile lines",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "JDK in the IDE",
                description = "Now you can choose your JDK in the IDE!",
                effect = "Increase firing speed to the max!",
                image = UpgradePNG.laptop,
            ),
            AttackUpgradeDef(
                title = "Command completion",
                description = "Added the command completion feature!",
                effect = "Maximum productivity! Projectiles no longer disappear after hitting an enemy",
                image = UpgradePNG.laptop,
            ),
        ),
    )

    val ENTERPRISE_READY_FEATURE = AttackDef(
        title = "Enterprise Ready",
        description = "Unlocks the power of integrations for Enterprise grade production environments",
        effect = "New weapon that adds a damaging area around Runzo.",
        unlockedByDefault = false,
        image = UpgradePNG.leaf,
        boosts = listOf(
            AttackUpgradeDef(
                title = "JUnit Integration",
                description = "IntelliJ added JUnit integration in IntelliJ",
                effect = "Increase size of protective area",
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "J2EE Support",
                description = "IntelliJ added J2EE support",
                effect = "Increase size of protective area",
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Spring Framework",
                description = "IntelliJ support for Spring framework",
                effect = "Increase size of protective area",
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Spring boot",
                description = "Added support for spring boot!",
                effect = "Increase size of protective area",
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Profiler",
                description = "Added a built in profiler",
                effect = "Increase size of protective area",
                image = UpgradePNG.leaf,
            ),
            AttackUpgradeDef(
                title = "Spring debugger",
                description = "Added a spring debugger!",
                effect = "Increase size of protective area",
                image = UpgradePNG.leaf,
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
    val boosts: List<AttackUpgradeDef>,
)

data class AttackUpgradeDef(
    val title: String,
    val description: String,
    val effect: String,
    val image: BufferedImage?,
)