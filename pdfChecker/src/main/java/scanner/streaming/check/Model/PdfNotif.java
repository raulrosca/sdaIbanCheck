package scanner.streaming.check.Model;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PdfNotif {

/* 
  model based on recieved start information
*/
  public enum StateEnum {
    OK, // check has passed successfully.
    SUSPICIOUS, // check has found something suspicious.
    ERROR, // check has failed in a technical way.
    IGNORED; // check has not been executed, due to some pre-conditions.

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }
    
  public static final String HEADER_REQUESTER_SERVICE = "requester_service";

  private StateEnum state;

  private String url;

  private String fileType;

  public PdfNotif() {
  }

  public PdfNotif(String url, String fileType) {
    this.url = url;
    this.fileType = fileType;
  }

  public String getUrl() {
    return url;
  }

  public PdfNotif setUrl(String url) {
    this.url = url;
    return this;
  }

  public StateEnum getState() {
    return state;
  }

  public PdfNotif setState(StateEnum status) {
    this.state = status;
    return this;
  }

  public String getFileType() {
    return fileType;
  }

  public PdfNotif setFileType(String fileType) {
    this.fileType = fileType;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PdfNotif that = (PdfNotif) o;
    return url == that.url &&
            fileType.equals(that.fileType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, fileType);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
