package com.example.swingworker;

import com.jeta.forms.components.panel.FormPanel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

@Slf4j
public class SimpleApp {

    public static void main(String[] args) {

        SimpleApp app = new SimpleApp();
        app.run();

    }

    public void run() {

        SwingUtilities.invokeLater(() -> {

            log.info("starting app");

            JFrame.setDefaultLookAndFeelDecorated(true);

            JFrame mainWindow = new JFrame("Some Simple App!");
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setResizable(true);

            mainWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    log.warn("app is shutting down");
                    System.exit(0);
                }
            });


            FormPanel panel = new FormPanel("simpleAppForm.jfrm");

            JTextField superSecretInfoTextField = panel.getTextField("superSecretInfoTextField");
            JButton analyzeButton = (JButton) panel.getButton("analyzeButton");
            JProgressBar progressBar = panel.getProgressBar("progressBar");

            progressBar.setValue(0);

            AnalysisService analysisService = new AnalysisService();

            analyzeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    // on click, scoop some info from the input and run a time-consuming task.
                    // usually takes 20 - 30 seconds to run, and I'd like to be updating the progress
                    // bar during that time.
                    //
                    // also need to handle cases where the task encounters a POSSIBLE error and needs to
                    // communicate back to the EDT to display a JOPtionPane to the user; and then get the
                    // user's response back and handle it.
                    //
                    // also need to handle the case where the long running task encounters both a checked
                    // and unchecked/unexpected exception
                    String superSecretInfo = superSecretInfoTextField.getText();

                    // here is where we start the long-running task. ideally this needs to go into a SwingWorker
                    // however there is a somewhat complex back-and-forth-communication required. see the analysis
                    // method comments for details
                    try {

                        InputAnalysis analysis = analysisService.analyze(superSecretInfo, mainWindow, progressBar);
                        superSecretInfoTextField.setText(analysis.getSuperSecretAnswer());

                    } catch (IOException ex) {
                        log.error(ExceptionUtils.getStackTrace(ex));
                        JOptionPane.showMessageDialog(
                            mainWindow,
                            "Something went wrong",
                            "Aborted!",
                            JOptionPane.WARNING_MESSAGE);
                    }


                    // comment the above try-catch out and uncomment all the worker code below to switch over
                    // to the async/non-blocking worker based method

//                    AnalysisWorker analysisWorker = new AnalysisWorker(mainWindow, progressBar, superSecretInfo);
//                    analysisWorker.addPropertyChangeListener(evt -> {
//
//                        if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
//                            try {
//                                // this is called on the EDT
//                                InputAnalysis asyncAnalysis = analysisWorker.get();
//                                superSecretInfoTextField.setText(asyncAnalysis.getSuperSecretAnswer());
//
//                            } catch (Exception ex) {
//                                log.error(ExceptionUtils.getStackTrace(ex));
//                            }
//                        }
//
//                    });
//
//                    analysisWorker.execute();


                }
            });

            mainWindow.add(panel);
            mainWindow.pack();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);

            log.info("application started");

        });

    }

}
