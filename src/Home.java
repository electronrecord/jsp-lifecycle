import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "Home")
public class Home extends HttpServlet {

    int count;

    public void init(ServletConfig config) throws ServletException {
        // Always call super.init(config) first (servlet mantra #1)
        super.init(config);
        // Try to load the initial count from our saved persistent state
        try {
            FileReader fileReader = new FileReader("InitDestroyCounter.initial");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String initial = bufferedReader.readLine();
            count = Integer.parseInt(initial);
            return;
        } catch (FileNotFoundException ignored) {
        }
        // no saved state
        catch (IOException ignored) {
        }
        // problem during read
        catch (NumberFormatException ignored) {
        }
        // corrupt saved state
        // No luck with the saved state, check for an init parameter
        String initial = getInitParameter("initial");
        try {
            count = Integer.parseInt(initial);
            return;
        }
        catch (NumberFormatException ignored) { }
        // null or non-integer value
        // Default to an initial count of "0"
        count = 0;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        count++;
        req.setAttribute("count", count);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("home.jsp");
        requestDispatcher.forward(req, resp);
    }

    public void destroy() {
        saveState();
    }

    public void saveState() {
        // Try to save the accumulated count
        try {
            FileWriter fileWriter = new FileWriter("InitDestroyCounter.initial");
            String initial = Integer.toString(count);
            fileWriter.write(initial, 0, initial.length());
            fileWriter.close();
            return;
        } catch (IOException e) {
            // problem during write
            // Log the exception. See Chapter 5, "Sending HTML Information".
        }
    }
}
