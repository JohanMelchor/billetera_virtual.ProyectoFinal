package co.edu.uniquindio.factory;

import co.edu.uniquindio.decorator.AlertaBasica;
import co.edu.uniquindio.decorator.LoggingAlertaDecorator;
import co.edu.uniquindio.decorator.SonidoAlertaDecorator;
import co.edu.uniquindio.decorator.TimestampAlertaDecorator;
import co.edu.uniquindio.service.IAlertaManager;

public class AlertaManagerFactory {
    public static IAlertaManager crearManagerBasico() {
        return new AlertaBasica();
    }
    
    public static IAlertaManager crearManagerCompleto() {
        IAlertaManager manager = new AlertaBasica();
        manager = new LoggingAlertaDecorator(manager);
        manager = new SonidoAlertaDecorator(manager);
        manager = new TimestampAlertaDecorator(manager);
        return manager;
    }
    
    public static IAlertaManager crearManagerConLogging() {
        IAlertaManager manager = new AlertaBasica();
        manager = new LoggingAlertaDecorator(manager);
        return manager;
    }
    
    public static IAlertaManager crearManagerConSonidos() {
        IAlertaManager manager = new AlertaBasica();
        manager = new SonidoAlertaDecorator(manager);
        return manager;
    }
    
    public static IAlertaManager crearManagerPersonalizado(boolean logging, boolean sonidos, boolean timestamps) {
        IAlertaManager manager = new AlertaBasica();
        
        if (logging) {
            manager = new LoggingAlertaDecorator(manager);
        }
        if (sonidos) {
            manager = new SonidoAlertaDecorator(manager);
        }
        if (timestamps) {
            manager = new TimestampAlertaDecorator(manager);
        }
        
        return manager;
    }
}
