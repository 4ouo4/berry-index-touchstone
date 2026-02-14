//Defines the berry class
public class Berry {
    private String name;
    private String effect;
    private String biomes;
    private Boolean found;

    public Berry(String name, String effect, String biomes, Boolean found) {
        this.name = name;
        this.effect = effect;
        this.biomes = biomes;
        this.found = found;
    }

    public String getName() {
        return name;
    }

    public String getEffect() {
        return effect;
    }

    public String getBiomes() {
        return biomes;
    }

    public Boolean getFound() {
        return found;
    }

    public void setName(String updateInput) {
        name = updateInput;
    }

    public void setEffect(String updateInput) {
        effect = updateInput;
    }

    public void setBiomes(String updateInput) {
        biomes = updateInput;
    }

    public void setFound(String updateInput) {
        if (updateInput.equalsIgnoreCase("Y")) {
            found = true;
        } else if (updateInput.equalsIgnoreCase("N")) {
            found = false;
        }
    }
}