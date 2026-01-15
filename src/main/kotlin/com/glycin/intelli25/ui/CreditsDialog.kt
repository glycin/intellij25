package com.glycin.intelli25.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
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
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        val font = Fonts.jbMono.deriveFont(16f)

        panel.add(createLabel("This game was created by Alexander Chatzizacharias", font))

        panel.add(Box.createVerticalStrut(JBUI.scale(15)))

        panel.add(createLabel("In collaboration with:", font))
        panel.add(createLabel("     Irina Mariasova – IntelliJ IDEA Marketing", font))
        panel.add(createLabel("     Andrei Kogun – Developer Consultant", font))
        panel.add(createLabel("     Arina Kovrizhkina, FOREAL® studio – Design", font))

        panel.add(Box.createVerticalStrut(JBUI.scale(15)))

        panel.add(createLabel("Sponsored by JetBrains s.r.o. for IntelliJ IDEA's 25th birthday.", font))

        panel.preferredSize = Dimension(650, 200)

        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction)
    }


    private fun createLabel(text: String, font: Font): JBLabel {
        return JBLabel(text).apply {
            this.font = font
            alignmentX = Component.LEFT_ALIGNMENT
        }
    }
}
