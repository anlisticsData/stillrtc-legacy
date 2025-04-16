package br.com.stilldistribuidora.pco.db;

import java.util.ArrayList;

import br.com.stilldistribuidora.pco.db.model.PictureImageGrafica;

public interface IActionBase {
    public long insert(Object obj);
    public long update(Object obj);
    public long delete(Object obj);
    public ArrayList<PictureImageGrafica> getAll();
    public  Object getBy(Object obj);

}
