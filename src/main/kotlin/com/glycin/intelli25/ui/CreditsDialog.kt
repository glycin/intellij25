package com.glycin.intelli25.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import javax.swing.Action
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel

class CreditsDialog(project: Project?) : DialogWrapper(project) {
    init {
        title = "Credits"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val rootPanel = JPanel(BorderLayout(JBUI.scale(20), 0))

        val iconLabel = JBLabel(AllIcons.General.InformationDialog)
        val iconPanel = Box.createVerticalBox()
        iconPanel.add(iconLabel)
        iconPanel.add(Box.createVerticalGlue())
        rootPanel.add(iconPanel, BorderLayout.WEST)

        val contentPanel = JPanel()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)

        val standardFont = Fonts.jbMono.deriveFont(14f)

        contentPanel.add(createLabel("This game was created by Alexander Chatzizacharias (https://github.com/glycin)", standardFont))

        contentPanel.add(Box.createVerticalStrut(JBUI.scale(15)))
        contentPanel.add(createLabel("In collaboration with:", standardFont))
        contentPanel.add(createLabel("- Irina Mariasova (IntelliJ IDEA Marketing)", standardFont))
        contentPanel.add(createLabel("- Andrei Kogun (Developer Consultant)", standardFont))
        contentPanel.add(createLabel("- Arina Kovrizhkina, FOREAL® studio (Design)", standardFont))

        contentPanel.add(Box.createVerticalStrut(JBUI.scale(15)))
        contentPanel.add(createLabel("Sponsored by JetBrains s.r.o. for IntelliJ IDEA's 25th birthday.", standardFont))

        rootPanel.add(contentPanel, BorderLayout.CENTER)
        rootPanel.preferredSize = Dimension(600, 150)

        return rootPanel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction)
    }

    fun createLabel(text: String, font: Font): JBLabel {
        return JBLabel(text).apply {
            this.font = font
            alignmentX = Component.LEFT_ALIGNMENT
        }
    }
}