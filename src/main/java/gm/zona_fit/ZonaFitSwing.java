package gm.zona_fit;

import com.formdev.flatlaf.FlatDarculaLaf;
import gm.zona_fit.gui.ZonaFItForma;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class ZonaFitSwing {
    public static void main(String[] args) {
        //Configuramos el modo oscuro.
        FlatDarculaLaf.setup();
        //Instanciar la fbarica de spring
        ConfigurableApplicationContext contextoSpring =
                new SpringApplicationBuilder(ZonaFitSwing.class).headless(false)
                        .web(WebApplicationType.NONE).run(args);
        //Crear objeto de swing
        SwingUtilities.invokeLater(() -> {
            ZonaFItForma zonaFitForma = contextoSpring.getBean(ZonaFItForma.class);
            zonaFitForma.setVisible(true);

        });

    }
}
