package br.com.stilldistribuidora.partners.Casts;

import java.util.List;

import br.com.stilldistribuidora.partners.Models.AvailableOperationsComposition;
import br.com.stilldistribuidora.partners.Models.Operation;
import br.com.stilldistribuidora.partners.Models.Router;
import br.com.stilldistribuidora.partners.Models.RouterMap;
import br.com.stilldistribuidora.partners.Models.Zonas;

public class SavesTheInformationAndBringsIfThereAreNewOperationsAvailable {

    private int status;
    private String search_data;
    private int count;
    private List<AvailableOperationsComposition>availableOperations;


    public int getStatus() {
        return status;
    }

    public String getSearch_data() {
        return search_data;
    }

    public int getCount() {
        return count;
    }

    public List<AvailableOperationsComposition> getAvailableOperations() {
        return availableOperations;
    }
}
