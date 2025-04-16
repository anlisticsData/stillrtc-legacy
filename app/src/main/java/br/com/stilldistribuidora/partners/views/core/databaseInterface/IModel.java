package br.com.stilldistribuidora.partners.views.core.databaseInterface;

import java.util.List;

public interface IModel {
    public Object by(Object object);
    public long save(Object object);
    public int delete(Object object);
    public int update(Object object);

}
