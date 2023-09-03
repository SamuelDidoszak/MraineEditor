
import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.Generator
import com.neutrino.generation.Generators
import com.neutrino.generation.algorithms.RoomFinderAlgorithm
import com.neutrino.generation.algorithms.SquidGenerationAlgorithm
import squidpony.squidgrid.mapping.styled.TilesetType

Generators.add("Test") {
    SquidGenerationAlgorithm(TilesetType.DEFAULT_DUNGEON, it).generateAll()
}
Generators.add("", Generator(true, 1) {
	SquidGenerationAlgorithm(TilesetType.ROOMS_AND_CORRIDORS_B, it)
		.generateAll()
	RoomFinderAlgorithm(it)
		.addInRooms("WoodenCrateBigger", true, "nearWall", 2f, false, false)
		.addInRooms("StandingMetalTorch", 1f, false, false)
		.addInCorridors("WoodenTorch", Identity.Torch(), 1f, false, false)
		.addInCorridors("Barrel", listOf(), 1f, false, false)
		.generateAll()
})
