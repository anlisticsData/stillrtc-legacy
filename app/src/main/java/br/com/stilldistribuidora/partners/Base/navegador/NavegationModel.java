package br.com.stilldistribuidora.partners.Base.navegador;


public class NavegationModel {
    private long id;
    private String props;
    private  String content;

    public NavegationModel(long id, String props, String content) {
        this.id = id;
        this.props = props;
        this.content = content;
    }

    public NavegationModel(String props, String content) {
        this.props = props;
        this.content = content;
    }

    public NavegationModel() {

    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
