package com.neutrino.ui.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.neutrino.textures.LevelDrawer
import com.neutrino.textures.Shaders
import squidpony.squidmath.Coord
import java.lang.Integer.max
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.properties.Delegates

class EditorStage(
    private var levelDrawer: LevelDrawer
): Stage(ExtendViewport(1920f, 1080f),
    SpriteBatch(10, Shaders.fragmentAlphas)) {

    private var map = levelDrawer.map
    private var startXPosition by Delegates.notNull<Float>()
    private var startYPosition by Delegates.notNull<Float>()
    private var x: Int = 0
    private var y: Int = 0

    init {
        setLevelDrawer(levelDrawer)
    }

    var lookingAround: Boolean = false

    fun setLevelDrawer(levelDrawer: LevelDrawer) {
        map = levelDrawer.map
        startXPosition = 0f
        startYPosition = map.size * 48f
    }

    fun setCameraPosition(xPos: Int, yPos: Int) {
        camera.position.lerp(Vector3(xPos * 48f, startYPosition - yPos * 48f, camera.position.z), 0.03f)
    }

    fun getCameraPosition(): Pair<Int, Int> {
        val gameCamera = camera as OrthographicCamera
        val yPos = (levelDrawer.height - gameCamera.position.y) / 48
        val xPos = (gameCamera.position.x / 48)

        return Pair(xPos.roundToInt(), yPos.roundToInt())
    }

    fun isInCamera(tileX: Int, tileY: Int): Boolean {
        val gameCamera = camera as OrthographicCamera

        var yBottom = MathUtils.ceil((levelDrawer.height - (gameCamera.position.y - gameCamera.viewportHeight * gameCamera.zoom / 2f)) / 48) + 2
        var yTop = MathUtils.floor((levelDrawer.height - (gameCamera.position.y + gameCamera.viewportHeight * gameCamera.zoom / 2f)) / 48) + 1
        var xLeft: Int =
            MathUtils.floor((gameCamera.position.x - gameCamera.viewportWidth * gameCamera.zoom / 2f) / 48)
        var xRight =
            MathUtils.ceil((gameCamera.position.x + gameCamera.viewportWidth * gameCamera.zoom / 2f) / 48)

        // Make sure that values are in range
        yBottom = if (yBottom <= 0) 0 else if (yBottom > map.size) map.size else yBottom
        yTop = if (yTop <= 0) 0 else if (yTop > map.size) map.size else yTop
        xLeft = if (xLeft <= 0) 0 else if (xLeft > map[0].size) map[0].size else xLeft
        xRight = if (xRight <= 0) 0 else if (xRight > map[0].size) map[0].size else xRight

        return (tileX in xLeft..xRight) && (tileY in yTop..yBottom)
    }


    // Input processor

    private var dragging = false
    private var touchDownCoords: Pair<Int, Int> = Pair(0, 0)
    private var calledFromLongpress: Boolean = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!(button != Input.Buttons.LEFT || button != Input.Buttons.RIGHT) || pointer > 0)
            return false
        val screenX = actualScreenX(screenX)
        val screenY = actualScreenY(screenY)
        touchDownCoords = Pair(screenX, screenY)
        if (calledFromLongpress) calledFromLongpress = false
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val screenX = actualScreenX(screenX)
        val screenY = actualScreenY(screenY)
        if (max(abs(touchDownCoords.first - screenX), abs(touchDownCoords.second - screenY)) < 32)
            return true
        dragging = true
        lookingAround = true
        val zoom = (camera as OrthographicCamera).zoom
        camera.position.add(-Gdx.input.deltaX.toFloat() * zoom,
            Gdx.input.deltaY.toFloat() * zoom, 0f)
        return true
    }


    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!(button != Input.Buttons.LEFT || button != Input.Buttons.RIGHT || button != Input.Buttons.FORWARD) || pointer > 0) return false
        if (dragging) {
            dragging = false
            return true
        }
        // related to android long press handling
        if (calledFromLongpress)
            return true
        if (button == Input.Buttons.FORWARD)
            calledFromLongpress = true
        val button = if (button == Input.Buttons.FORWARD) Input.Buttons.RIGHT else button

        // If there is a popup, remove it
//        var currPopup: Table? = this.actors.find { it.name == "entityPopup" } as EntityLookupPopup?
//        if (currPopup == null)
//            currPopup = this.actors.find { it.name == "itemDetails" } as ItemDetailsPopup?
//        if (currPopup != null) {
//            currPopup.remove()
//
//            if (button == Input.Buttons.RIGHT || button == Input.Buttons.FORWARD)
//                return true
//        }

        val screenX = actualScreenX(screenX)
        val screenY = actualScreenY(screenY)
        val touch: Vector3 = Vector3(screenX.toFloat(), screenY.toFloat(),0f)
        camera.unproject(touch)
        val tile = getTileUnprojected(touch)

        if (button == Input.Buttons.RIGHT) {
            var entities = "${tile.x}, ${tile.y}: "
//            levelDrawer.map[tile.y][tile.x].forEach { entity ->
//                entities += "${entity.name}: " +
//                entity.get(TextureAttribute::class)?.textures?.map { "${it.texture.name}, " } }
            println(entities)
            return true
        }

        // create the entityLookupPopup
//        if (button == Input.Buttons.RIGHT) {
//
//            val popup: Table
//            if (level!!.getTopItem(tile.x, tile.y) != null) {
//                val item = level!!.getTopItem(tile.x, tile.y)!!
//                popup =
//                    if (item is EquipmentItem)
//                        EquipmentComparisonPopup(item)
//                    else
//                        ItemDetailsPopup(item)
//            }
//            else
//                popup = EntityLookupPopup(level!!.map[tile.y][tile.x], level!!.characterMap[tile.y][tile.x])
//            this.addActor(popup)
//            popup.setPosition(touch.x, touch.y)
//            return true
//        }

        dragging = false

        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        if (amountY.sign.toInt() == -1)
            (camera as OrthographicCamera).zoom /= 2
        else
            (camera as OrthographicCamera).zoom *= 2
        return true

//        var zoom = (camera as OrthographicCamera).zoom
//        zoom += amountY * zoom / 10
//        if (zoom <= 0.4)
//            (camera as OrthographicCamera).zoom = 0.4f
//        else if (zoom >= 12f)
//            (camera as OrthographicCamera).zoom = 12f
//        else
//            (camera as OrthographicCamera).zoom = zoom
//        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        val screenX = actualScreenX(screenX)
        val screenY = actualScreenY(screenY)
        val coord = getTile(screenX, screenY)
        return super.mouseMoved(screenX, screenY)
    }

    fun getTile(screenX: Int, screenY: Int): Coord {
        val touch: Vector3 = Vector3(screenX.toFloat(), screenY.toFloat(),0f)
        camera.unproject(touch)

        return getTileUnprojected(touch)
    }

    fun getTileUnprojected(position: Vector3): Coord {
        // Change the outOfBounds click behavior
        val tileX: Int = if(position.x.toInt() / 48 <= 0) 0 else
            if (position.x.toInt() / 48 >= map[0].size) map[0].size - 1 else
                position.x.toInt() / 48

        val tileY: Int = if((startYPosition - position.y) / 48 <= 0) 0 else
            if ((startYPosition - position.y) / 48 >= map.size - 1) map.size - 1 else
                (startYPosition - position.y).toInt() / 48 + 1

        return Coord.get(tileX, tileY)
    }

    fun setPosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    private fun actualScreenX(screenX: Int): Int {
        return screenX - x
    }

    private fun actualScreenY(screenY: Int): Int {
        return screenY - y
    }
}
