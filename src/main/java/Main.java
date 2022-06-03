import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.Instant;

public class Main {
    public static void main (final String[]args ){
        //Para conseguir el token debemos iniciar sesión en la página de desarrolladores de Discord
        // y crear una nueva aplicación y desde la opción bot podemos añadir el nuestro y obtener su token.

        //Se registra el token  para poder usarse en el login en Discord

        final String token = args[0];
        final DiscordClient cliente = DiscordClient.create(token);
        final GatewayDiscordClient puerto = cliente.login().block();
            //Se crea una clase y se implementa al bot.

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.PINK)
                .title("VLC")
                .image("attachment://kirbyCono.png")
                .build();

        puerto.on(MessageCreateEvent.class).subscribe(event -> {
            //Y se crea el evento de recibir un mensaje
            final Message message = event.getMessage();
            // se crea una igualdad con el mensaje recibido y buscando el canal en el que se escribió se devuelve el mensaje en ese canal.
            if ("!Hambre".equals(message.getContent())) {
                final MessageChannel channel = message.getChannel().block();

                channel.createMessage("Toma unos melocotones <3").block();
            }

            try {
                Drive.main();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            if("!Help".equals(message.getContent())){
                 String IMAGE_URL ="https://c.tenor.com/HG5nbp_7N_QAAAAd/triangle-shape.gif";
                 String ANY_URL = "https://www.youtube.com/watch?v=yT16_7Jizn8&ab_channel=Zenuxio";
                final MessageChannel channel = message.getChannel().block();
                EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();
                builder.author("Yo", ANY_URL, IMAGE_URL);
                builder.image(IMAGE_URL);
                builder.title("Info");
                builder.url(ANY_URL);
                builder.description("Descripcion\n" +
                        "No lo se\n" +
                        "Solo tengo melocotones\n"+
                        "!Hambre : para recibir melocotones \n" +
                        "!Kirby : pruebalo ");
                builder.thumbnail(IMAGE_URL);
                builder.footer("setFooter --> setTimestamp", IMAGE_URL);
                builder.timestamp(Instant.now());
                channel.createMessage(builder.build()).block();
            }

            if ("!Kirby".equals(message.getContent())) {
                final MessageChannel channel = message.getChannel().block();

                InputStream fileAsInputStream = null;
                try {
                    fileAsInputStream = new FileInputStream("C:\\Users\\david\\Downloads\\kirbyCono.png");
                } catch (FileNotFoundException e) {
                    System.out.println("Error lectura de fichero" + e.getMessage());
                }
                ;
                channel.createMessage(MessageCreateSpec.builder()
                        .content(" ")
                        .addFile("kirbyCono.png", fileAsInputStream)
                        .addEmbed(embed)
                        .build()).subscribe();
            }

            if("!Img".equals(message.getContent())){
                final MessageChannel channel = message.getChannel().block();

                InputStream fileAsInputStream = null;
                try{
                    fileAsInputStream = new FileInputStream("D:\\david\\Documents\\imagenesB\\your_aux.jpeg");
                } catch (FileNotFoundException e) {
                    System.out.println("Error de lectura "+e.getMessage());;
                }

                channel.createMessage(MessageCreateSpec.builder()
                        .content("  ")
                        .addFile("your_aux.jpeg",fileAsInputStream)
                        .addEmbed(embed)
                        .build()).subscribe();
            }
            if("!pdf".equals(message.getContent())){
                final MessageChannel channel = message.getChannel().block();

                InputStream fileAsInputStream = null;
                try{
                    fileAsInputStream = new FileInputStream("D:\\david\\Documents\\imagenesB\\examen.pdf");
                } catch (FileNotFoundException e) {
                    System.out.println("Error de lectura "+e.getMessage());;
                }

                channel.createMessage(MessageCreateSpec.builder()
                        .content("  ")
                        .addFile("examenPDF.pdf",fileAsInputStream)
                        .build()).subscribe();
            }

        });



        puerto.onDisconnect().block();
    }
}
