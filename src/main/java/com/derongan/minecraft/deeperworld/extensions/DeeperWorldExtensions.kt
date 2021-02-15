package com.derongan.minecraft.deeperworld.extensions

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

internal fun Entity.getRootVehicle(): Entity? {
    var currentVehicle = vehicle

    while (currentVehicle?.vehicle != null) {
        currentVehicle = currentVehicle.vehicle
    }

    return currentVehicle
}

internal fun Entity.getPassengersRecursive(): List<Entity> {
    return mutableListOf<Entity>().let { passengerList ->
        passengerList.addAll(passengers)

        for (passenger: Entity in passengers) {
            passenger.getPassengersRecursive().let { passengerList.addAll(it) }
        }

        return@let passengerList
    }
}

internal fun Player.getLeashedEntities(): List<LivingEntity> {
    // Max leashed entity range is 10 blocks, therefore these parameter values
    return getNearbyEntities(20.0, 20.0, 20.0)
        .filterIsInstance<LivingEntity>()
        .filter { it.isLeashed && it.leashHolder == this }
}

internal fun Player.teleportWithSpectator(loc : Location){
    val nearbyPlayers = getNearbyEntities(5.0, 5.0, 5.0).filterIsInstance<Player>()

    var spectator : Player? = null
    for (nearbyPlayer in nearbyPlayers) {
        if(nearbyPlayer.spectatorTarget == this){
            spectator = nearbyPlayer
            break
        }
    }

    spectator?.spectatorTarget = null
    teleport(loc)
    spectator?.teleport(loc)
    spectator?.spectatorTarget = this
}