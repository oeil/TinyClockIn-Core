package org.teknux.tinyclockin.model;

/**
 * @author Francois EYL
 */
public class Version {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Version that = (Version) o;

        return version != null ? version.equals(that.version) : that.version == null;
    }

    @Override
    public int hashCode() {
        return version != null ? version.hashCode() : 0;
    }
}
