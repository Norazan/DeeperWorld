@file: UseSerializers(WorldSerializer::class, VectorSerializer::class)

package com.mineinabyss.deeperworld.world.section

import com.mineinabyss.deeperworld.world.Region
import com.mineinabyss.idofront.serialization.LocationSerializer
import com.mineinabyss.idofront.serialization.VectorSerializer
import com.mineinabyss.idofront.serialization.WorldSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.bukkit.Location
import org.bukkit.World

/**
 * @property region the region within which this section is active
 * @property world the world this section is a part of
 * @property key the key for this section
 * @property aboveKey the previous section that is above this one, or null if none exists TODO by null do you mean SectionKey.TERMINAL?
 * @property belowKey the next section that is below this one, or null if none exists
 * @property referenceTop the reference location between this section and the one above it.
 * This and the section above's [referenceBottom] represent the same location in physical space.
 * @property referenceBottom the reference location between this section and the one below it.
 * This and the section belows's [referenceTop] represent the same location in physical space.
 */
@Serializable
data class Section(
    val name: String? = null,
    val region: Region,
    val world: @Serializable(WorldSerializer::class) World,
    @SerialName("refTop") private val _refTop: ReferenceLocation,
    @SerialName("refBottom") private val _refBottom: ReferenceLocation
) {
    @Serializable(LocationSerializer::class)
    val referenceTop = _refTop.toLocation(world)

    @Serializable(LocationSerializer::class)
    val referenceBottom = _refBottom.toLocation(world)

    val key: SectionKey = name?.let { AbstractSectionKey.CustomSectionKey(name) }
        ?: AbstractSectionKey.InternalSectionKey()

    @Transient
    internal var aboveKey: SectionKey = SectionKey.TERMINAL

    @Transient
    internal var belowKey: SectionKey = SectionKey.TERMINAL

    override fun toString() = key.toString()
}

@Serializable
data class ReferenceLocation(val x: Int, val y: Int, val z: Int) {
    fun toLocation(world: World) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}
