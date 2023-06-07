package com.neutrino.textures

internal class LayeredTextureList: ArrayList<LayeredTexture>() {

    fun sort() {
        for (j in 1 until size){
            var i = j - 1
            val processedValue = this[j]
            while ( (i >= 0) && (this[i] > processedValue) ){
                this[i + 1] = this[i]
                i--
            }
            this[i + 1] = processedValue
        }
    }
}
