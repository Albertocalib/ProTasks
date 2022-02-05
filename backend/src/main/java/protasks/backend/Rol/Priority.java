package protasks.backend.Rol;

import java.awt.*;

public enum Priority {
    // Color int shows the equivalence in android Color library
    NO_PRIORITY("No Priority", -1, 0),
    LOW("Low", -7829368, 1),//Color Gray
    NORMAL("Normal", -16711936, 2),//Color Green
    HIGH("High", -256, 3), //Color Yellow
    URGENT("Urgent", -65536, 4); //Color Red

    public int getIndex() {
        return this.index;
    }

    // custom properties with default values
    private final String printableName;
    private final Integer color;
    private final Integer index;

    Priority(String printableName, Integer color, Integer index) {
        this.printableName = printableName;
        this.color = color;
        this.index = index;
    }

    public int[] getColors() {
        Priority[] values = values();
        int[] colors = new int[values.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = values[i].color;
        }
        return colors;
    }

    public String[] getNames() {
        Priority[] values = values();
        String[] names = new String[values.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = values[i].printableName;
        }
        return names;
    }


}
