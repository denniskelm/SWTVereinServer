package _programming;
/*
@author
Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
x Dennis Kelm
*/

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//Diese Klasse öffnet für uns die benötigten Projektdokumente für die Programmierung (bei Startup)
public class OpenAllProgrammingResources {
    public static final boolean RUNSTARTUPRESOURCES = true;

    public static void main(String[] args) throws URISyntaxException, IOException {
        URI[] urls = new URI[]{
                new URI("https://denniskelm.de/SWTentwurf.pdf"),
                new URI("https://miro.com/app/board/uXjVO8j4qS4=/"),
                new URI("https://miro.com/app/board/uXjVOdeTj8g=/"),
                new URI("https://trello.com/b/NJwc4JyC/swt2022")
        };

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
                && RUNSTARTUPRESOURCES) {
            for (URI url :
                    urls) {
                Desktop.getDesktop().browse(url);
            }

        }
    }
}
