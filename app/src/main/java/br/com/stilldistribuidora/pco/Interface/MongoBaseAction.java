package br.com.stilldistribuidora.pco.Interface;

public interface MongoBaseAction {
    public  Object insert(Object object);
    public  Object updateOne(String uuid ,Object object);
    public  Object findBy(String uuid ,Object object);
    public  Object findAll(Object object);
    public  Object delete(String uuid );
}
