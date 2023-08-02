package com.neutrino.ui.views

import com.kotcrab.vis.ui.building.StandardTableBuilder
import com.kotcrab.vis.ui.widget.VisWindow
import com.neutrino.generation.NameOrIdentity
import com.neutrino.ui.elements.RulePickerButton
import com.neutrino.ui.views.util.Callback
import com.neutrino.ui.views.util.RulesTable

class AddRulesView(
    ruleList: MutableList<NameOrIdentity?>? = null,
    override val callback: (data: Pair<List<NameOrIdentity?>, RulePickerButton>) -> Unit
): VisWindow(""), Callback<Pair<List<NameOrIdentity?>, RulePickerButton>> {

    val builder = StandardTableBuilder()
    private val rulesTable = RulesTable(ruleList)

    init {
        isModal = true
        closeOnEscape()
        addCloseButton()
        builder.append(rulesTable)

        add(builder.build()).expand().fill()
        pack()
        centerWindow()
    }

    override fun close() {
        super.close()
        callback.invoke(rulesTable.getRules())
    }
}
