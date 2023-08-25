import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.EntityPositionRequirement
import com.neutrino.generation.EntityPositionRequirementType
import com.neutrino.generation.GenerationRequirements

GenerationRequirements.addOther("nearWall", listOf(
    EntityPositionRequirement(EntityPositionRequirementType.OR, Identity.Wall(), listOf(2, 4, 6, 8)),
    EntityPositionRequirement(EntityPositionRequirementType.NAND, Identity.Wall(), listOf(4, 6)),
    EntityPositionRequirement(EntityPositionRequirementType.NAND, Identity.Wall(), listOf(2, 8))
))
GenerationRequirements.addOther("nearWallBlockEntries", listOf(
    EntityPositionRequirement(EntityPositionRequirementType.OR, Identity.Wall(), listOf(2, 4, 6, 8))
))
GenerationRequirements.add(Identity.Door(), listOf(
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(1, 2, 3, 8)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(4, 7, 6, 9)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(7, 8, 9, 2)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(1, 4, 3, 6)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(4, 3, 6, 9)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(1, 2, 7, 8)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(1, 4, 7, 6)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(2, 3, 8, 9)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(2, 8)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(4, 9, 6, 3)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(2, 8)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(6, 1, 4, 7)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(4, 6)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(8, 1, 2, 3)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(4, 6)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(2, 7, 8, 9))
))
GenerationRequirements.add(Identity.Torch(), listOf(
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(7, 8, 9)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(1, 2, 3)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(1, 4, 7)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(3, 6, 9)),
    EntityPositionRequirement(EntityPositionRequirementType.AND),
    EntityPositionRequirement(EntityPositionRequirementType.AND, Identity.Wall(), listOf(3, 6, 9)),
    EntityPositionRequirement(EntityPositionRequirementType.NOR, Identity.Wall(), listOf(1, 4, 7))
))
GenerationRequirements.add(Identity.StairsDown(), listOf())
GenerationRequirements.add("DungeonWall", listOf())
