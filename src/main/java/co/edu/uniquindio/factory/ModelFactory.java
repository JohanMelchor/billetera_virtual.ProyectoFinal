package co.edu.uniquindio.factory;

import co.edu.uniquindio.model.BilleteraVirtual;

public class ModelFactory {
    private static ModelFactory modelFactory;
    private BilleteraVirtual billeteraVirtual;

    private ModelFactory(){
        inicalizarDatos();
    }

    public static ModelFactory getInstance(){
        if(modelFactory==null){
            modelFactory=new ModelFactory();
        }
        return modelFactory;
    }

    private void inicalizarDatos() {
        billeteraVirtual = new BilleteraVirtual();
    }
}
