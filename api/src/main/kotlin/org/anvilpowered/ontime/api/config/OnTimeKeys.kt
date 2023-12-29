package org.anvilpowered.ontime.api.config

import org.anvilpowered.anvil.core.config.Key
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.buildingMap
import org.anvilpowered.anvil.core.config.buildingSimple

@Suppress("RemoveExplicitTypeArguments")
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

    val COMMANDS by Key.buildingMap {
        fallback(mapOf<String, List<String>>())
    }

    val RANKS by Key.buildingMap {
        fallback(mapOf<String, Int>())
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
