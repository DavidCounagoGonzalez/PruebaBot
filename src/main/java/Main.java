import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Main {
    public static void main (final String[]args ){
        //Para conseguir el token debemos iniciar sesión en la página de desarrolladores de Discord
        // y crear una nueva aplicación y desde la opción bot podemos añadir el nuestro y obtener su token.

        //Se registra el token  para poder usarse en el login en Discord

        String token = "OTUzNjMyOTk0NzgwOTA1NDcz.YjHZ7w.AYIdb1tgBD2LiOllGUfchPohhxw";
        final DiscordClient cliente = DiscordClient.create(token);
        final GatewayDiscordClient puerto = cliente.login().block();
            //Se crea una clase y se implementa al bot.
        assert puerto != null;
        puerto.on(MessageCreateEvent.class).subscribe(event -> {
            //Y se crea el evento de recibir un mensaje
            final Message message = event.getMessage();
            // se crea una igualdad con el mensaje recibido y buscando el canal en el que se escribió se devuelve el mensaje en ese canal.
            if ("!Hambre".equals(message.getContent())) {
                final MessageChannel channel = message.getChannel().block();
                assert channel != null;
                channel.createMessage("Toma un melocotón <3").block();
            }
        });

        puerto.onDisconnect().block();
    }
}
