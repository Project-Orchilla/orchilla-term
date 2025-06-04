package software.siani.orchilla.units;

public enum Month {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;

    public int value() {
        return this.ordinal() + 1;
    }
}
