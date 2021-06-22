package scanner.streaming.check.Processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/* 
  defining kafka topic channels for each enum status
*/
@Component
public interface PdfProcessor {

  String PDFS_IN = "output";
  String OK_OUT = "ok";
  String SUSPICIOUS_OUT = "suspicious";
  String ERROR_OUT = "error";
  String IGNORED_OUT = "ignored";

  @Input(PDFS_IN)
  SubscribableChannel pdfChecker();

  @Output(OK_OUT)
  MessageChannel ok();

  @Output(SUSPICIOUS_OUT)
  MessageChannel suspicious();

  @Output(ERROR_OUT)
  MessageChannel error();

  @Output(IGNORED_OUT)
  MessageChannel ignored();

}
