package com.azuyamat.blixify.scheduler

object Scheduler {

    private val tasks: MutableMap<Long, Operation> = mutableMapOf()

    fun scheduleOperation(time: Long, operation: Operation) {
        tasks[time] = operation
    }

    fun initialize() {

        // Initialize a scheduler that runs every 20 ticks (1 second)

    }
}