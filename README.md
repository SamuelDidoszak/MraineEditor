# MraineEditor
<img src="https://github.com/SamuelDidoszak/MraineEditor/assets/70522994/dea69297-b1f3-43e4-b439-f59df0a1e0c5" width="66%"></img>


MraineEditor is a desktop application that allows adding content to the game Mraine. 
The editor has several features:
- Adding new entities and their attributes
- Adding textures and animations
- Dynamic texturing based on rules
- Automatic generation of TextureAtlases
- Light sources
- Creating new maps using procedural generation algorithms
- Adding and saving entity generation rules

![mraineEditor](https://github.com/SamuelDidoszak/MraineEditor/assets/70522994/d913daa4-bad3-4c12-b61b-041d934387c4)


Content added with the editor interface is generated into executable scripts which can be found in the folder assets/core. Code generation is optimized for minimal code length.

Those scripts are containing kotlin code used to dynamically add content to the game and editor in real time. 

Output scripts handle:
- Entities
- Textures
- Generators
- Generation requirements
- Tilesets
- Characters

![mraineEditor2](https://github.com/SamuelDidoszak/MraineEditor/assets/70522994/b3f32d80-953c-4514-a221-8b87bb935c7b)

Editor uses its own implementation of an Entity Component System, where components and systems are joined together as Attributes. Entity generation is handled by calling an internal database which holds lambda functions responsible for generating entities and attaching parametered attributes. 
Those lambdas are added via the AddEntities.kts script generated in the editor.
Fragment of AddEntities script:

```Kotlin
Entities.add("StonePillar") {  
   Entity()  
      .addAttribute(TextureAttribute { position, random, textures -> run {  
            textures.add(position!!.check(listOf(2), Identity.Wall::class) {  
                Textures.get("stonePillarTop")})?.also {return@run}  
         textures add Textures.getRandomTexture(random, listOf(  
            80f to listOf("stonePillar"),  
            20f to listOf("stonePillarCracked"),  
      ))}})  
      .addAttribute(MapParamsAttribute(false, false))  
}  
Entities.add("WoodenDoor") {  
    Entity()  
        .addAttribute(Identity.Door())  
        .addAttribute(MapParamsAttribute(false, false))  
        .addAttribute(InteractionAttribute(arrayListOf(Interaction.DOOR())))  
        .addAttribute(ChangesImpassableAttribute())  
        .addAttribute(TextureAttribute { position, random, textures -> run {  
            textures.add(position!!.check(listOf(2, 8), NameOrIdentity(Identity.Wall::class)) {  
                Textures.get("woodenDoorVerticalClosed")})?.also {return@run}  
            textures add Textures.get("woodenDoorClosed")  
        }})  
}
```
