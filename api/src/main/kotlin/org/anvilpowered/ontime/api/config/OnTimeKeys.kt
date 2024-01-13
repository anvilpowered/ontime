package org.anvilpowered.ontime.api.config

import org.anvilpowered.anvil.core.config.Key
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.buildingMap
import org.anvilpowered.anvil.core.config.buildingSimple

@Suppress("PropertyName")
class OnTimeKeys : KeyNamespace by KeyNamespace.create("ONTIME") {

    val DB_URL by Key.buildingSimple {
        fallback("jdbc:postgresql://db:5432/ontime")
    }

    val DB_USER by Key.buildingSimple {
        fallback("ontime")
    }

    val DB_PASSWORD by Key.buildingSimple {
        fallback("ontime")
    }

    val RANK_TIMES by Key.buildingMap {
        description(
            """
            Player ranks and their time requirement in seconds.
            Make sure the rank names *exactly* match the groups in luckperms (case-sensitive).
            """.trimIndent(),
        )
        fallback(
            mapOf(
                "noob" to 0L,
                "player" to 600L,
                "trusted" to 1800L,
            ),
        )
    }

    val RANK_COMMANDS by Key.buildingMap {
        description(
            """
            Commands to run after a player has received a new rank. The nodes are compiled as a regex and
            matched against the the new rank name. The commands for every matching regex will run (not
            just the first one).
            
            Available placeholders:
            - %id%: The player's UUID
            - %username%: The player's username
            - %rank%: The player's new rank
            - %time%: The player's total play time, formatted
            """.trimIndent(),
        )
        fallback(
            mapOf(
                ".*" to listOf(
                    "say %username% has advanced to %rank% after playing for %time%",
                    "give %username% iron_ingot 1",
                ),
                "trusted" to listOf(
                    "say %username% is the best",
                ),
            ),
        )
    }

    val PERMISSION_USER_CHECK by Key.buildingSimple {
        fallback("ontime.user.check")
    }

    val PERMISSION_ADMIN_CHECK by Key.buildingSimple {
        fallback("ontime.admin.check")
    }

    val PERMISSION_ADMIN_EDIT by Key.buildingSimple {
        fallback("ontime.admin.edit")
    }

    val PERMISSION_ADMIN_IMPORT by Key.buildingSimple {
        fallback("ontime.admin.import")
    }
}
