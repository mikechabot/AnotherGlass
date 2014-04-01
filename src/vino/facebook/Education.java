package vino.facebook;

import java.util.ArrayList;
import java.util.List;

public class Education {

    private School school;
    private String type;
    private Year year;
    private List<Concentration> concentration = new ArrayList<Concentration>();

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public List<Concentration> getConcentration() {
        return concentration;
    }

    public void setConcentration(List<Concentration> concentration) {
        this.concentration = concentration;
    }

}
