package org.teknux.tinyclockin.model;

import org.teknux.tinyclockin.config.app.LocalDateTimeAdapter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;


/**
 * @author Francois EYL
 */
@Entity(name = "audit")
public class Audit {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "runtime", nullable = false)
    private long runtime;

    @Column(name = "status", nullable = false)
    private int status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Audit audit = (Audit) o;

        if (runtime != audit.runtime)
            return false;
        if (status != audit.status)
            return false;
        if (id != null ? !id.equals(audit.id) : audit.id != null)
            return false;
        if (timestamp != null ? !timestamp.equals(audit.timestamp) : audit.timestamp != null)
            return false;
        if (email != null ? !email.equals(audit.email) : audit.email != null)
            return false;
        if (type != null ? !type.equals(audit.type) : audit.type != null)
            return false;
        if (url != null ? !url.equals(audit.url) : audit.url != null)
            return false;
        return ip != null ? ip.equals(audit.ip) : audit.ip == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (int) (runtime ^ (runtime >>> 32));
        result = 31 * result + status;
        return result;
    }

    public static Audit create(String email, String type, String url, String ip, long runtime, int status) {
        final Audit audit = new Audit();
        audit.setTimestamp(LocalDateTime.now());
        audit.setEmail(email);
        audit.setType(type);
        audit.setUrl(url);
        audit.setIp(ip);
        audit.setRuntime(runtime);
        audit.setStatus(status);
        return audit;
    }

    public interface Type {
        final String HTTP_GET = "GET";
        final String HTTP_POST = "POST";
        final String HTTP_DELETE = "DELETE";
    }
}
