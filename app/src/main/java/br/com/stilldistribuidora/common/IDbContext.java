package br.com.stilldistribuidora.common;

public interface IDbContext {
    public Object All();
    public Object findBy(Object object);
    public Object created(Object object);
    public Object update(Object object);
    public Object deleted(Object object);
}
