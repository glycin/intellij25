package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.UpgradeAtlasFeature
import com.glycin.intelli25.model.UpgradeAtlasBoost
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.util.UpgradePNG

class UpgradeAtlasRepository(
    private val saveState: GameSaveState,
) {

    fun getAllFeatures(): List<UpgradeAtlasFeature> {
        return listOf(aiFeature(), styleFeature(), buildTools(), kotlin(), versioning(), productivity(), enterpriseReady())
    }

    fun getAllBoosts(): List<UpgradeAtlasBoost> {
        val pizzaBoost = UpgradeAtlasBoost(
            title = "Pizza slice",
            description = "Yum",
            effect = "Increases your health regeneration rate",
            image = UpgradePNG.pizza,
            unlocked = false,
        )

        val coffeeBoost = UpgradeAtlasBoost(
            title = "Coffee",
            description = "A tasty sip for a bolt of energy",
            effect = "Increases your movement speed",
            image = UpgradePNG.coffee,
            unlocked = false,
        )

        val performanceBoost = UpgradeAtlasBoost(
            title = "Performance fix",
            description = "Code running so well!",
            effect = "Increases your total health",
            image = UpgradePNG.performance,
            unlocked = false,
        )

        val firewallBoost = UpgradeAtlasBoost(
            title = "Firewall shield",
            description = "The best defense is...",
            effect = "Decreases enemy speed",
            image = UpgradePNG.firewall,
            unlocked = false,
        )

        val autoRefactoringBoost = UpgradeAtlasBoost(
            title = "Auto Refactoring",
            description = "For when you don't want to do things yourself",
            effect = "Increases your damage",
            image = UpgradePNG.duck,
            unlocked = false,
        )

        val intentionActionsBoost = UpgradeAtlasBoost(
            title = "Intention Actions",
            description = "For when your intentions are clear",
            effect = "Decrease the amount of enemies spawned",
            image = UpgradePNG.intentions,
            unlocked = false,
        )

        val codeInspectionsBoost = UpgradeAtlasBoost(
            title = "Code Inspections",
            description = "Go go gadget inspections!",
            effect = "Increase your experience pickup range",
            image = UpgradePNG.inspections,
            unlocked = false,
        )

        val dukeBoost = UpgradeAtlasBoost(
            title = "The Duke",
            description = "Every new version of the Duke, IntelliJ is right there",
            effect = "Increase the amount of experience gained",
            image = UpgradePNG.duke,
            unlocked = false,
        )

        val codeFreezeBoost = UpgradeAtlasBoost(
            title = "Code freeze",
            description = "For when you need to be sure nothing will break",
            effect = "Decrease the speed in which new enemies appear",
            image = UpgradePNG.freeze,
            unlocked = false,
        )

        val pushToProdBoost = UpgradeAtlasBoost(
            title = "Push to Prod",
            description = "For when you feel adventurous",
            effect = "Increase the amount of enemies appearing",
            image = UpgradePNG.push,
            unlocked = false,
        )

        return listOf(
            pizzaBoost,
            coffeeBoost,
            performanceBoost,
            firewallBoost,
            autoRefactoringBoost,
            intentionActionsBoost,
            codeInspectionsBoost,
            dukeBoost,
            codeFreezeBoost,
            pushToProdBoost,
        )
    }

    private fun aiFeature() : UpgradeAtlasFeature {
        return UpgradeAtlasFeature(
            title = "Artificial Intelligence",
            description = "Unlocks the power of AI, the latest transformative innovation in tech.",
            effect = "New weapon that adds a drone that flies around and protects Runzo.",
            unlocked = false,
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "AI Chat",
                    description = "",
                    effect = "Increase rotating speed of the AI assistant",
                    image = UpgradePNG.junie,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Junie",
                    description = "IntelliJ added Junie",
                    effect = "Add an additional AI assistant",
                    image = UpgradePNG.junie,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "K2 Mode",
                    description = "IntelliJ added support for the K2 compiler",
                    effect = "Add an additional AI assistant and increase rotation speed of the drones",
                    image = UpgradePNG.junie,
                    unlocked = false,
                ),
            )
        )
    }

    private fun styleFeature() : UpgradeAtlasFeature {
        return UpgradeAtlasFeature(
            title = "Impeccable style",
            description = "Serving developers with style",
            effect = "New weapon that fires a projectile to the closest enemy",
            unlocked = false,
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "Dark theme",
                    description = "IntelliJ added a dark theme",
                    effect = "Decreases weapon cooldown",
                    image = UpgradePNG.style,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Jetbrains Monotype",
                    description = "IntelliJ added the jb monotype",
                    effect = "Adds an extra projectile",
                    image = UpgradePNG.style,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "New UI",
                    description = "IntelliJ got a redesign",
                    effect = "Further decreases cooldown",
                    image = UpgradePNG.style,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Custom themes",
                    description = "Intellij added custom theme support!",
                    effect = "Further decreases cooldown and adds two additional projectiles",
                    image = UpgradePNG.style,
                    unlocked = false,
                ),
            ),
        )
    }

    private fun buildTools() : UpgradeAtlasFeature {
        return UpgradeAtlasFeature(
            title = "Build & Deployment tools",
            description = "Unlocks the power of build and deployment tools, which are essential to every developer nowadays",
            effect = "New weapon that spawns toxic pools around Runzo that hurt enemies",
            unlocked = false,
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "Maven 3 Integration",
                    description = "Maven 3 Integration was added",
                    effect = "Increase spawn rate of the pools.",
                    image = UpgradePNG.buildTools,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Gradle Support",
                    description = "IntelliJ added gradle support",
                    effect = "Increase size of the spawned pools",
                    image = UpgradePNG.buildTools,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Docker Support",
                    description = "IntelliJ added docker support",
                    effect = "Further increase pool spawn rate",
                    image = UpgradePNG.buildTools,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Kubernetes support",
                    description = "Added k8s support!",
                    effect = "Further increase pool size",
                    image = UpgradePNG.buildTools,
                    unlocked = false,
                ),
            ),
        )
    }

    private fun kotlin() : UpgradeAtlasFeature {
        return UpgradeAtlasFeature(
            title = "Kotlin",
            description = "Unlocks the power of Kotlin, the JVM language made by JetBrains.",
            effect = "New weapon that randomly strikes enemies for heavy damage",
            unlocked = false,
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "Kotlin support",
                    description = "IntelliJ added kotlin support",
                    effect = "Decreases lightning strike cooldown",
                    image = UpgradePNG.kotlin,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Kotlin multi-platform",
                    description = "IntelliJ added support for KMP",
                    effect = "Increases lighting strike impact radius",
                    image = UpgradePNG.kotlin,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "K2 Mode",
                    description = "IntelliJ added support for the K2 compiler",
                    effect = "Decreases lightning strike cooldown even further",
                    image = UpgradePNG.kotlin,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Kotlin notebooks",
                    description = "IntelliJ can create and run kotlin notebooks",
                    effect = "Decreases cooldown even further and increases impact radius",
                    image = UpgradePNG.kotlin,
                    unlocked = false,
                ),
            ),
        )
    }

    private fun versioning() : UpgradeAtlasFeature {
        return UpgradeAtlasFeature(
            title = "Version control",
            description = "New weapon that occasionally freezes all enemies in place",
            effect = "New weapon that occasionally freezes all enemies in place",
            unlocked = false,
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "CVS & VSS",
                    description = "IntelliJ added CVS and VSS support",
                    effect = "Increases the time enemies stay frozen",
                    image = UpgradePNG.git,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Subversion",
                    description = "IntelliJ added Subversion support",
                    effect = "Increases how often this weapon activates",
                    image = UpgradePNG.git,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Git",
                    description = "IntelliJ added git support",
                    effect = "Increases how often this weapon activates and how long enemies are frozen",
                    image = UpgradePNG.git,
                    unlocked = false,
                ),
            ),
        )
    }

    private fun productivity() : UpgradeAtlasFeature {
        return  UpgradeAtlasFeature(
            title = "Productivity",
            description = "This is already unlocked",
            effect = "This is already unlocked",
            unlocked = true, // since this one starts unlocked
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "HTML & CSS Support",
                    description = "IntelliJ added html and css support",
                    effect = "Increase firing speed of projectiles",
                    image = UpgradePNG.laptop, // or UpgradePNG.laptop.image
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "SQL Support",
                    description = "IntelliJ added SQL support",
                    effect = "Adds two additional projectile lines",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Search Everywhere",
                    description = "IntelliJ added the search everywhere feature",
                    effect = "Further increases firing speed of projectiles",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Embedded terminal",
                    description = "Added an embedded terminal!",
                    effect = "Adds two additional projectile lines",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Debugger",
                    description = "Added an embedded debugger!",
                    effect = "Increase firing speed of projectiles",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Decompiler",
                    description = "Added a decompiler for fast peeking!",
                    effect = "Fire additional projectile lines",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "JDK in the IDE",
                    description = "Now you can choose your JDK in the IDE!",
                    effect = "Increase firing speed to the max!",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Command completion",
                    description = "Added the command completion feature!",
                    effect = "Maximum productivity! Projectiles no longer disappear after hitting an enemy",
                    image = UpgradePNG.laptop,
                    unlocked = false,
                ),
            ),
        )
    }

    private fun enterpriseReady(): UpgradeAtlasFeature {
        return UpgradeAtlasFeature(
            title = "Enterprise Ready",
            description = "Unlocks the power of integrations for Enterprise grade production environments",
            effect = "New weapon that adds a damaging area around Runzo.",
            unlocked = false,
            upgrades = listOf(
                UpgradeAtlasBoost(
                    title = "JUnit Integration",
                    description = "IntelliJ added JUnit integration in IntelliJ",
                    effect = "Increase size of protective area",
                    image = UpgradePNG.leaf,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "J2EE Support",
                    description = "IntelliJ added J2EE support",
                    effect = "Increase size of protective area",
                    image = UpgradePNG.leaf,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Spring Framework",
                    description = "IntelliJ support for Spring framework",
                    effect = "Increase size of protective area",
                    image = UpgradePNG.leaf,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Spring boot",
                    description = "Added support for spring boot!",
                    effect = "Increase size of protective area",
                    image = UpgradePNG.leaf,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Profiler",
                    description = "Added a built in profiler",
                    effect = "Increase size of protective area",
                    image = UpgradePNG.leaf,
                    unlocked = false,
                ),
                UpgradeAtlasBoost(
                    title = "Spring debugger",
                    description = "Added a spring debugger!",
                    effect = "Increase size of protective area",
                    image = UpgradePNG.leaf,
                    unlocked = false,
                ),
            ),
        )
    }
}