package br.com.stilldistribuidora.stillrtc.db.business;

import java.util.List;

/**
 * Created by Ack Lay (Cleidimar Viana) on 3/18/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

abstract class ProviderBusiness {

    public abstract void validateInsert(Object obj);

    public abstract void validateUpdate(Object obj);

    public abstract long insert(Object obj);

    public abstract long update(Object obj, String where);

    public abstract void delete(String where);

    public abstract Object getById(String where);

    public abstract List<?> getList(String where, String orderby);
}