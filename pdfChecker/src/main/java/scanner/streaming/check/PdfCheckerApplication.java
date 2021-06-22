package scanner.streaming.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import scanner.streaming.check.CheckEngine.PdfChecker;
import scanner.streaming.check.Processor.PdfProcessor;

@SpringBootApplication
@EnableBinding(PdfProcessor.class)
public class PdfCheckerApplication {

/* 
  Starting the pdf event checker
*/
	public static final Logger log = LoggerFactory.getLogger(PdfChecker.class);

	public static void main(String[] args) {
		SpringApplication.run(PdfCheckerApplication.class, args);
	}

}
