package com.neutrino.ui.views

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.ui.attributes.TextureAttributeView
import com.neutrino.ui.elements.TitleView
import com.neutrino.ui.views.util.Callback
import com.neutrino.util.UiManagerFactory

class AddNewTextureView(
    private val atlasName: String,
    override val callback: (data: Boolean) -> Unit
): VisTable(), Callback<Boolean> {

    private val title: TitleView
    private val textureView = TextureAttributeView("$atlasName.atlas")

    init {
        TableUtils.setSpacingDefaults(this)
        columnDefaults(0).left()
        padTop(0f)
        top()

        title = TitleView("Add new texture", true, "save") {
            saveTexture()
            callback.invoke(true)
            UiManagerFactory.getUI().previousPanel()
        }
        title.rightButton.isDisabled = true

        add(title).growX().padTop(16f).row()
        add(textureView).growX().row()

        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                validateSave()
                super.clicked(event, x, y)
            }
        })
    }

    private fun validateSave() {
        var enabled = true
        if (!textureView.validateAttribute())
            enabled = false
        if (textureView.texturesExist())
            enabled = false
        title.rightButton.isDisabled = !enabled
    }

    private fun saveTexture() {
        textureView.onSaveAction()
    }
}
