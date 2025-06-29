public class Bit {
    public enum boolValues { FALSE, TRUE }

    private boolValues value;

    public Bit(boolean value) {
        if (value) {
            this.value = boolValues.TRUE;
        } else {
            this.value = boolValues.FALSE;
        }
    }

    public boolValues getValue() {
        return value;  //before it was boolValues.FALSE
    }

    public void assign(boolValues value) {
        this.value = value;
    }

    public void and(Bit b2, Bit result) {
        and(this, b2, result);
    }


    public static void and(Bit b1, Bit b2, Bit result) {
        if (b1.getValue() == boolValues.TRUE) {
            if (b2.getValue() == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
            } else {
                result.assign(boolValues.FALSE);
            }
        } else {
            result.assign(boolValues.FALSE);
        }
    }

    public void or(Bit b2, Bit result) {
        or(this, b2, result);
    }

    public static void or(Bit b1, Bit b2, Bit result) {
        if (b1.getValue() == boolValues.TRUE) {
            result.assign(b1.value);
        } else if (b2.getValue() == boolValues.TRUE) {
            result.assign(b2.value);
        } else {
            result.assign(boolValues.FALSE);
        }
    }

    public void xor(Bit b2, Bit result) {
        xor(this, b2, result);
    }

    public static void xor(Bit b1, Bit b2, Bit result) {
        if (b1.getValue() == boolValues.TRUE) {
            if (b2.getValue() == boolValues.FALSE) {
                result.assign(boolValues.TRUE);
            } else {
                result.assign(boolValues.FALSE);
            }
        } else if (b1.getValue() == boolValues.FALSE) {
            if (b2.getValue() == boolValues.TRUE) {
                result.assign(boolValues.TRUE);
            } else {
                result.assign(boolValues.FALSE);
            }
        } else {
            result.assign(boolValues.FALSE);
        }
    }


    public static void not(Bit b2, Bit result) {
        if (b2.getValue() == boolValues.TRUE) {
            result.assign(boolValues.FALSE);
        } else if (b2.getValue() == boolValues.FALSE) {
            result.assign(boolValues.TRUE);
        }
    }

    public void not(Bit result) {
        not(this, result);
    }

    public String toString() {
        if (value == boolValues.TRUE) {
            return "t";
        } else {
            return "f";
        }
    }
}
