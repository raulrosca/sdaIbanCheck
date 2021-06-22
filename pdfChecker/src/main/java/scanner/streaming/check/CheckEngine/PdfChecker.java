package scanner.streaming.check.CheckEngine;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import scanner.streaming.check.Model.PdfNotif;
import scanner.streaming.check.Model.PdfNotif.StateEnum;
import scanner.streaming.check.Processor.PdfProcessor;
/*
  the check logic hapens here.
  ibanSet - contains each pdf's iban code. It is a set so that it does not allow duplicates.
*/
@Component
public class PdfChecker {

    public static final Logger log = LoggerFactory.getLogger(PdfChecker.class);
    private PdfProcessor processor;
    private HashSet<String> ibanSet = new HashSet<String>();
    String content = "";
    String ibans = "";
  
    @Autowired
    public PdfChecker(PdfProcessor processor) {
      this.processor = processor;
    }

    @StreamListener(PdfProcessor.PDFS_IN)
    public void checkFiles(PdfNotif pdfFile) {

      /*
        The check logic hapens here.
        We check both the stated pdf file type from the kafka tobic, and the metadata file type.
      */
      if (pdfFile.getFileType().toString().equals("pdf")){
        try {
          InputStream stream = this.getClass().getClassLoader().getResourceAsStream(pdfFile.getUrl());
  
        /*
          Using apache tika to process the pdf.
        */
          String fileType = PdfParser.detectDocTypeUsingFacade(stream);
          if (fileType.toString().equals("application/pdf")){
              try {
                String content = PdfParser.extractContentUsingParser(stream);
                /*
                  checking all the iban occurances through all the pdf content
                */
                ibans = StringUtils.substringBetween(content, "IBAN:", "SWIFT/BIC:");
                if (ibans != null && !ibans.equals("")) {
                  ibanSet.add(ibans.trim());
                  System.out.println("ibanList: " + ibanSet.toString());
                  /*
                    In order to check the ibans I am using a simple array with blacklisted ibans.
                  */
                  if (isIbansBlackisted(ibanSet)) {
                    /*
                      send to kafka topic as suspicios, and in need to be checked
                    */
                    pdfFile.setState(StateEnum.SUSPICIOUS);
                    processor.suspicious().send(message(pdfFile));
                  } else {
                    /*
                      all OK, write ok to the topic
                    */
                    pdfFile.setState(StateEnum.OK);
                    processor.ok().send(message(pdfFile));
                  }
                  ibans = "";
                  ibanSet.clear();
                  /*
                    clearing after check to prepare for the next pdf
                  */

                } else {

                  /*
                    ignoring files that are not pdf
                  */
                  pdfFile.setState(StateEnum.IGNORED);
                  processor.ignored().send(message(pdfFile));
                }
                content = "";
              } catch (TikaException  e) {
                /*
                  All errors get their custom status
                */
                pdfFile.setState(StateEnum.ERROR);
                processor.error().send(message(pdfFile));
                System.out.println("Error could not read content");
                e.printStackTrace();
              } catch (SAXException e) {
               pdfFile.setState(StateEnum.ERROR);
               processor.error().send(message(pdfFile));
               System.out.println("Error pdf size");
               e.printStackTrace();
             }
          } else {
            pdfFile.setState(StateEnum.IGNORED);
            processor.ignored().send(message(pdfFile));
          }
        } catch (IOException e) {
          pdfFile.setState(StateEnum.ERROR);
          processor.error().send(message(pdfFile));
          System.out.println("File error");
          e.printStackTrace();
        }
      } else {
        pdfFile.setState(StateEnum.IGNORED);
        processor.ignored().send(message(pdfFile));
      }
      log.info("STATUS: {} - fileType: {} - url: {}", pdfFile.getState(), pdfFile.getFileType(), pdfFile.getUrl() );
  
    }

    private static final <T> Message<T> message(T val) {
      return MessageBuilder.withPayload(val).build();
    }

    private boolean isIbansBlackisted(HashSet<String> ibanSet ) { 
      /*
        blackListedIbans contains blacklisted ibans.
        The pdf provided only contains one iban: DE15 3006 0601 0505 7807 80
        In order to check also the sunshine case, you need to remove it, or replace it inside the "blackListedIbans" set
      */
      
      HashSet<String> blackListedIbans = new HashSet<String>();
      blackListedIbans.add("DE15 3006 0601 0505 7807 80");
      blackListedIbans.add("DE15 3006 0601 0505 7807 81");

      if (!ibanSet.removeAll(blackListedIbans) ) {
        return false;
      }
      return true;
    }

}
