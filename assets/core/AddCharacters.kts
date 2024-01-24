
import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Texture
import com.neutrino.entities.attributes.character.Stats
import com.neutrino.textures.Textures
Entities.add("Bat") {
	Entity()
		.addAttribute(Stats(
            hpMax = 10f,
            strength = 1f,
            dexterity = 3f,
            damageMin = 2f,
            damageMax = 5.5f,
            evasion = 0.2f,
            accuracy = 0.9f,
            criticalChance = 0.2f,
        ))
		.addAttribute(Texture { position, random, textures -> run {
			textures.add(Textures.get("bat"))
		}})
}
Entities.add("Mouse") {
	Entity()
		.addAttribute(Stats(
            hpMax = 15f,
            strength = 2f,
            dexterity = 2f,
            luck = 1f,
            damageMin = 2f,
            damageMax = 4f,
            defence = 2f,
        ))
		.addAttribute(Texture { position, random, textures -> run {
			textures.add(Textures.get("mouse"))
		}})
}
Entities.add("Slime") {
	Entity()
		.addAttribute(Stats(
            hpMax = 20f,
            strength = 1f,
            damageMin = 1f,
            damageMax = 3f,
            defence = 1f,
            fireDefence = 4f,
        ))
        .addAttribute(Texture { position, random, textures -> run {
            textures.add(Textures.get("slime"))
        }})
}
