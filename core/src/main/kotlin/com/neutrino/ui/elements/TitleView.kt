package com.neutrino.ui.elements

import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisImageButton
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.util.UiManagerFactory
import ktx.actors.onChange

class TitleView(title: String,
                showBackButton: Boolean = true,
                rightButtonName: String? = null,
                private var onRightButtonClick: () -> Unit = {}
): VisTable() {

    private var onBackButtonClick: () -> Unit = { UiManagerFactory.getUI().previousPanel() }

    val backButton = VisImageButton(VisUI.getSkin().getDrawable("icon-arrow-left"))
    val rightButton = VisTextButton(rightButtonName ?: "")

    init {
        backButton.onChange { onBackButtonClick.invoke() }
        rightButton.onChange { onRightButtonClick.invoke() }

        add(backButton).left()
        if (!showBackButton)
            backButton.isVisible = false

        add(VisLabel(title)).padLeft(32f).padRight(32f).expandX().center()

        add(rightButton).right()
        if (rightButtonName == null) {
            rightButton.width = backButton.width
            rightButton.isVisible = false
        }
    }

    fun onBackButtonClick(onClick: () -> Unit) {
        onBackButtonClick = onClick
    }

    fun onRightButtonClick(onClick: () -> Unit) {
        onRightButtonClick = onClick
    }
}
