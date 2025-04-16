package br.com.stilldistribuidora.partners.Repository.Contracts;

import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;

public interface AppOpeationInterface   {

    public long create(ITEntity TEntity);
    public ITEntity getBy(ITEntity filter);




}
