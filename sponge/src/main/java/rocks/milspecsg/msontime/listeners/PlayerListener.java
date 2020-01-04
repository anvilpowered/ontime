package rocks.milspecsg.msontime.listeners;

import com.google.inject.Inject;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import rocks.milspecsg.msontime.api.member.MemberManager;

public class PlayerListener {

    @Inject
    MemberManager<Text> memberManager;

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        memberManager.getPrimaryComponent().getOneOrGenerateForUser(event.getTargetEntity().getUniqueId());
    }
}
