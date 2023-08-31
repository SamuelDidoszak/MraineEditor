
import com.neutrino.generation.Generators
import com.neutrino.generation.algorithms.SquidGenerationAlgorithm
import squidpony.squidgrid.mapping.styled.TilesetType

Generators.add("Test") {
    SquidGenerationAlgorithm(TilesetType.DEFAULT_DUNGEON, it).generateAll()
}
