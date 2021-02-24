package uk.co.conoregan.showrenamer.model;

public class RenamedStuff {
    private String oldName;
    private String newName;

    public RenamedStuff(String oldName) {
        this.oldName = oldName;
    }

    public RenamedStuff(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return this.oldName;
    }

    public String getNewName() {
        return this.newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
