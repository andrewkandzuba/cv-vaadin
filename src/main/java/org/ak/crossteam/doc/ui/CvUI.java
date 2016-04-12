package org.ak.crossteam.doc.ui;

import com.google.common.base.Throwables;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.ak.crossteam.doc.backend.CvService;
import org.ak.crossteam.doc.backend.MailService;
import org.ak.crossteam.doc.model.Cv;
import org.ak.crossteam.doc.model.Job;
import org.ak.crossteam.doc.utils.Text;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@Title("CV")
@Theme("valo")
public class CvUI extends UI {
    // Send response controls
    TextArea response = new TextArea("Send a response");
    Button send = new Button("Send");

    // Details components
    TextField fullName = new TextField("Name:");
    TextField email = new TextField("Email:");
    TextField phoneNumber = new TextField("Phone:");
    TextArea overview = new TextArea("Overview");

    Grid records = new Grid();

    CvService cvService = CvService.get();
    MailService mailService = MailService.get();

    @Override
    protected void init(VaadinRequest request) {

        configureComponents();
        buildLayout();

        try {
            bindCv();
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagateIfPossible(e);
        }
    }

    private void configureComponents() {
        records.setContainerDataSource(new BeanItemContainer<>(Job.class));
        records.setColumnOrder("title", "company", "location", "startDate", "endDate", "summary");
        records.removeColumn("additionalProperties");
        records.setCaption("Professional experience");
        records.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void buildLayout() {

        // CV details section
        VerticalLayout details = new VerticalLayout(
                fullName,
                email,
                phoneNumber,
                overview
        );
        fullName.setEnabled(false);
        email.setEnabled(false);
        email.setWidth("30%");
        phoneNumber.setEnabled(false);
        overview.setSizeFull();
        overview.setEnabled(false);
        response.setRows(10);
        details.setWidth("100%");
        details.setMargin(true);
        details.setSpacing(true);

        // Experience section
        VerticalLayout experience = new VerticalLayout(
                records
        );
        records.setSizeFull();
        experience.setWidth("100%");
        experience.setMargin(true);
        experience.setSpacing(true);

        // Feedback section
        VerticalLayout feedback = new VerticalLayout(
                response,
                send
        );
        response.setRows(3);
        response.setSizeFull();
        response.setWidth("1024px");
        experience.setWidth("100%");
        feedback.setMargin(true);
        feedback.setSpacing(true);

        // Set main page layout
        VerticalLayout layout = new VerticalLayout(
                details,
                experience,
                feedback
        );
        layout.setWidth("100%");

        setContent(layout);
    }

    public void bindCv() throws IOException {
        Cv cv = cvService.getCv();
        fullName.setValue(
                String.format("%s %s",
                        cv.getCandidate().getFirstName(),
                        cv.getCandidate().getLastName()));
        email.setValue(cv.getCandidate().getEMail());
        phoneNumber.setValue(cv.getCandidate().getPhone());
        overview.setValue(
                Text.splitAndJoinBack(
                        cv.getCandidate().getQualification(), '|', '\n'));

        records.setContainerDataSource(new BeanItemContainer<>(
                Job.class, cv.getJobs()));


        send.addClickListener( e -> mailService.send(cv.getCandidate().getEMail(), response.getValue()) );
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = CvUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
