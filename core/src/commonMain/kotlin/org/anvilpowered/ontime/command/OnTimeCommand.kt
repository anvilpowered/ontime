package org.anvilpowered.ontime.command

import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.tree.LiteralCommandNode

object OnTimeCommand {
    fun create(): LiteralCommandNode<CommandSource> {
        return ArgumentBuilder.literal<CommandSource>("ontime")
            .then(createTime())
            .build()
    }
}