package view.botservices

import view.botservices.schedule.GROUPS

object GroupFinder {
    val groups = GROUPS

    fun execute(input: String): String {
        val group = groups.firstOrNull { group ->
            val length = input.length

            val index =
                if (input.replace("-", "").length == 4)
                    2
                else
                    3

            group.lowercase().contains(input.substring(0, index).lowercase())
                    && group.contains(input.substring(length - 2, length))
        } ?: "none"

        return group
    }
}