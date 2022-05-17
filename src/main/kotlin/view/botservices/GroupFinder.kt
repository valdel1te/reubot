package view.botservices

import view.botservices.scheduleoperations.GROUPS

object GroupFinder {
    fun execute(input: String): String {
        val group = GROUPS.firstOrNull { group ->
            val length = input.length

            val index =
                if (input.replace("-", "").length == 4)
                    2
                else
                    3

            group.lowercase().contains(input.substring(0, index).lowercase()) && group.contains(
                input.substring(
                    length - 2,
                    length - 1
                )
            )
        } ?: "none"

        return group
    }
}