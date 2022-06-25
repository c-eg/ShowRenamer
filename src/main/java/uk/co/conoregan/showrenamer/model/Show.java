package uk.co.conoregan.showrenamer.model;

public abstract class Show {
    private final String title;

    protected Show(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
