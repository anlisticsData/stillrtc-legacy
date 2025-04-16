package br.com.stilldistribuidora.partners.Models;

import java.util.List;

public class AvailableOperationsComposition {
    public int id;
    public Router router;
    public List<RouterMap> routerMap;
    public List<Zonas> zonas;
    public Operation operation;
    public String datetime_start;
    public int group_id;
    public int store_id;
    public String creator_id;

}
