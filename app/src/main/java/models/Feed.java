package models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "feed", strict = false)
public class Feed implements Serializable {

    @Element(name = "id")
    private String id;

    @Element(name = "title")
    private String title;

    @ElementList(inline = true, required = false)
    private List<Entry> entrys;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Entry> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<Entry> entrys) {
        this.entrys = entrys;
    }

    @Override
    public String toString() {
        return "Feed: \n [Entrys: \n" + entrys + "]";
    }
}