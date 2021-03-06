package com.example.swingworker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class AnalysisWorker extends SwingWorker<InputAnalysis,Integer> {

    private JFrame mainWindow;
    private JProgressBar progressBar;
    private String superSecretInfo;

    @Override
    protected void process(List<Integer> chunks) {

        progressBar.setValue(chunks.size() - 1);

    }

    @Override
    protected InputAnalysis doInBackground() throws Exception {

        publish(25);

        // simulate a few seconds of processing
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("SOMETHIN BLEW UP");
        }

        // now we are ready to analyze the input which itself can take 10 - 15 seconds but
        // we'll mock it up here
        if (superSecretInfo == null || superSecretInfo.isEmpty()) {

            // if the input is null/empty, we'll consider that a "checked exception"; something the
            // REAL code I'm using explicitly has a try-catch for because the libraries I'm using throw
            // them

            throw new IOException("ERMERGERD");

        } else if (superSecretInfo.equals("WELL_WELL_WELL")) {

            // here we'll consider this an unchecked exception
            throw new RuntimeException("DID NOT SEE THIS ONE COMING");

        }

        publish(55);

        // check to see if the input equals "KEY MASTER"; if it does we need to go back to the EDT
        // and prompt the user with a JOptionPane
        if (superSecretInfo.equalsIgnoreCase("KEY MASTER")) {

            int answer = JOptionPane.showConfirmDialog(
                    mainWindow,
                    "We have identified a KEY MASTER scenario. Do you wish to proceed?",
                    "Do you wish to proceed",
                    JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.NO_OPTION) {

                // return a partial InputAnalysis and return
                Boolean isFizz = Boolean.TRUE;
                String superSecretAnswer = "HERE IS A PARTIAL ANSWER";
                Integer numDingers = 5;

                return new InputAnalysis(isFizz, superSecretAnswer, numDingers);

            }

        }

        // if we get here, either KEY MASTER was not in the input or they chose to proceed anyway
        Boolean isFizz = superSecretInfo.length() < 5 ? Boolean.TRUE : Boolean.FALSE;
        String superSecretAnswer = "HERE IS A FULL ANSWER";
        Integer numDingers = 15;

        publish(100);

        return new InputAnalysis(isFizz, superSecretAnswer, numDingers);

    }

}
