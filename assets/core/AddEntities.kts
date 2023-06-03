
import attributes.DescriptionAttributeFalse
import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.RandomAttributeFalse
import com.neutrino.entities.attributes.TextureAttribute
import com.neutrino.util.Textures

Entities.add("Wall") {
    Entity()
        .addAttribute(DescriptionAttributeFalse("Chujkurwa"))
        .addAttribute(RandomAttributeFalse())
}
Entities.add("Wall2") {
    Entity()
        .addAttribute(DescriptionAttributeFalse("Shit sucks"))
        .addAttribute(RandomAttributeFalse())
}
Entities.add("TextureTest") {
    Entity()
        .addAttribute(TextureAttribute({
            position, random, textures -> {
                val texture = Textures.get("jeff")
                texture.mirror()
                textures.add(texture)
            }}
        ))
        .addAttribute(RandomAttributeFalse())
}
