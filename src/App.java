import java.io.IOException;
import java.util.List;
import java.util.Optional;

import edu.ksu.canvas.*;
import edu.ksu.canvas.interfaces.*;
import edu.ksu.canvas.model.*;
import edu.ksu.canvas.oauth.*;
import edu.ksu.canvas.requestOptions.GetUsersInAccountOptions;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;


public class App {

    private String canvasUrl;
    private OauthToken oauthToken;

    public static void main(String[] args) {
        String url = "https://uk.instructure.com";
        String token = "Get your own token";

        App canvasQuery = new App(url, token);

        try {
            canvasQuery.getOwnCourses();
            canvasQuery.getUserInformation();
        }
        catch(Exception e) {
            System.out.println("Problem while executing example methods" + e);
        }
    }

    public App(String canvasUrl, String tokenString) {
        this.canvasUrl = canvasUrl;
        this.oauthToken = new NonRefreshableOauthToken(tokenString);
    }

    public void getRootAccess() throws IOException {
        CanvasApiFactory apiFactory = new CanvasApiFactory(canvasUrl);
        AccountReader acctReader = apiFactory.getReader(AccountReader.class, oauthToken);
        Account rootAccount = acctReader.getSingleAccount("1").get();
        System.out.println(rootAccount.getName());
    }

    public void getUserInformation() throws IOException {
        CanvasApiFactory apiFactory = new CanvasApiFactory(canvasUrl);
        UserReader userReader = apiFactory.getReader(UserReader.class, oauthToken);
        User user = userReader.showUserDetails("self").get();

        System.out.println(user.getName());
        System.out.println(user.getEnrollments());
    }

    public void getOwnCourses() throws IOException {
        CanvasApiFactory apiFactory = new CanvasApiFactory(canvasUrl);
        CourseReader courseReader = apiFactory.getReader(CourseReader.class, oauthToken);
        List<Course> myCourses = courseReader.listCurrentUserCourses(new ListCurrentUserCoursesOptions());

        for(Course course : myCourses) {
            System.out.println(course.getId() + ": " + course.getName()); 
        }
    }
}
