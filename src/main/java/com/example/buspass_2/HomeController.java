package com.example.buspass_2;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserRepository repo;

    @GetMapping("/")
    public String home() {

        return "home";
    }
    @PostMapping("/register")
    public String register(User user,
            @RequestParam(value = "photofile", required = false)
            MultipartFile photo) {

        try {

            if(photo != null && !photo.isEmpty()) {

            	String uploadDir =
            	        System.getProperty("user.dir")
            	        + File.separator
            	        + "uploads";

            	File dir = new File(uploadDir);

            	if (!dir.exists()) {
            	    dir.mkdirs();
            	}

            	String fileName =
            	        System.currentTimeMillis()
            	        + "_" +
            	        photo.getOriginalFilename();

            	File destination =
            	        new File(dir, fileName);

            	photo.transferTo(destination);

            	user.setPhoto(fileName);
                }
       

            if(user.getUserType().equals("Student")) {
                user.setApprovalStatus("Pending Faculty Approval");
            } else {
                user.setApprovalStatus("Approved");
            }
            List<User> existingUser = repo.findByEmail(user.getEmail());

            if(!existingUser.isEmpty()) {

                return "login";
            }
            List<User> existingUsers =
                    repo.findByEmail(user.getEmail());

            if(!existingUsers.isEmpty()) {

                return "emailExists";
            }

            repo.save(user);

            return "success";

        } catch(Exception e) {

            e.printStackTrace();

            return "login";
        }
    }
    @PostMapping("/loginUser")
    public String loginUser(String email,
                            String password,
                            Model model,
                            HttpSession session) {

        User user =
                repo.findByEmailAndPassword(email, password);

        if(user != null) {

            session.setAttribute("userEmail",
                                 user.getEmail());

            model.addAttribute("name",
                               user.getName());

            return "dashboard";
        }

        model.addAttribute("error",
                "Invalid Email or Password");

        return "login";
    }@PostMapping("/submitRenewal")
    public String submitRenewal(
            @RequestParam String passNumber,
            @RequestParam String passType,
            Model model) {

        User user = repo.findByPassNumber(passNumber);

        if(user == null) {

            model.addAttribute("error",
                    "Invalid Pass Number");

            return "renewPass";
        }

        user.setPassType(passType);

        java.time.LocalDate today =
                java.time.LocalDate.now();

        user.setIssueDate(today.toString());

        // Expiry Date Calculation
        if(passType.equals("Monthly")) {

            user.setExpiryDate(
                    today.plusDays(30).toString());

        } else if(passType.equals("Quarterly")) {

            user.setExpiryDate(
                    today.plusDays(90).toString());

        } else {

            user.setExpiryDate(
                    today.plusDays(180).toString());
        }

        // Fee Calculation
        double fee = calculateFee(user, passType);

        user.setFee(fee);

        repo.save(user);

        // Free pass for students up to 6th class
        if(fee == 0) {

            if("Student".equals(user.getUserType())) {

                user.setApprovalStatus(
                        "Pending Faculty Approval");

            } else {

                user.setApprovalStatus("Approved");
            }

            repo.save(user);

            model.addAttribute("user", user);

            return "passCard";
        }

        model.addAttribute("user", user);

        return "payment";
    }
    @GetMapping("/admin")
    public String adminPage(Model model,
                            HttpSession session) {

        Boolean adminLoggedIn =
                (Boolean) session.getAttribute(
                        "adminLoggedIn");

        if(adminLoggedIn == null
                || !adminLoggedIn) {

            return "redirect:/adminLogin";
        }

        List<User> users = repo.findAll();

        model.addAttribute("users", users);

        return "admin";
    }private double calculateFee(User user, String passType) {

        // STUDENT FEES
        if ("Student".equals(user.getUserType())) {

            Integer cls = user.getClassLevel();

            // Class 1 to 6 → Free Pass
            if (cls != null && cls <= 6) {
                return 0;
            }

            // Class 7 to 10 → Reduced Fee
            if (cls != null && cls >= 7 && cls <= 10) {

                if ("Monthly".equals(passType))
                    return 100;

                if ("Quarterly".equals(passType))
                    return 250;

                if ("Half-Yearly".equals(passType))
                    return 450;
            }

            // Intermediate / Degree / PG Students
            if ("Monthly".equals(passType))
                return 200;

            if ("Quarterly".equals(passType))
                return 500;

            if ("Half-Yearly".equals(passType))
                return 900;
        }

        // PASSENGER FEES
        if ("Monthly".equals(passType))
            return 500;

        if ("Quarterly".equals(passType))
            return 1400;

        if ("Half-Yearly".equals(passType))
            return 2700;

        return 0;
    }
    
    @GetMapping("/adminLogin")
    public String adminLoginPage() {

        return "adminLogin";
    }
    @GetMapping("/adminLogout")
    public String adminLogout(HttpSession session) {

        session.invalidate();

        return "redirect:/adminLogin";
    }
    @GetMapping("/approve/{id}")
    public String approveUser(@PathVariable Long id) {

        User user = repo.findById(id).orElse(null);

        if(user != null) {

            user.setApprovalStatus("Approved");

            String passNo =
                    "BP" + System.currentTimeMillis();

            user.setPassNumber(passNo);

            user.setIssueDate(
                    java.time.LocalDate.now().toString());

            repo.save(user);
        }

        return "redirect:/admin";
    }

            
        @GetMapping("/login")
        public String loginPage() {
            return "login";
        }
        @GetMapping("/renewPass")
        public String renewPassPage() {
            return "renewPass";
        }
        @GetMapping("/applyPass")
        public String applyPass(HttpSession session,
                                Model model) {

            String email =
                    (String) session.getAttribute("userEmail");

            List<User> users = repo.findByEmail(email);

            if(users.isEmpty()) {
                return "login";
            }

            User user = users.get(0);

            model.addAttribute("user", user);

            return "applyPass";
      
        }@GetMapping("/viewPass")
        public String viewPass(Model model,
                HttpSession session) {

String email =
     (String) session.getAttribute("userEmail");

if(email == null) {
 return "login";
}

List<User> users = repo.findByEmail(email);

if(users.isEmpty()) {

 model.addAttribute("message",
         "No pass application found.");

 return "noPass";
}

User user = users.get(users.size() - 1);

model.addAttribute("user", user);

return "viewPass";
}
        @GetMapping("/logout")
        public String logout() {

            return "redirect:/login";
        }
        @GetMapping("/approveRenewal/{id}")
        public String approveRenewal(@PathVariable Long id) {

            User user = repo.findById(id).orElse(null);

            if(user != null) {

                user.setApprovalStatus("Approved");

                repo.save(user);
            }

            return "redirect:/admin";
        }
        @GetMapping("/rejectRenewal/{id}")
        public String rejectRenewal(@PathVariable Long id) {

            User user = repo.findById(id).orElse(null);

            if(user != null) {

                user.setApprovalStatus("Rejected");

                repo.save(user);
            }

            return "redirect:/admin";
        }
        @GetMapping("/status")
        public String statusPage(Model model,
                                 HttpSession session) {

            String email =
                    (String) session.getAttribute("userEmail");

            if(email == null) {

                return "redirect:/login";
            }

            List<User> users = repo.findByEmail(email);

            if(users.isEmpty()) {

                model.addAttribute("message",
                        "No pass application found.");

                return "noPass";
            }

            User user = users.get(0);

            model.addAttribute("user", user);

            return "status";
        }
        @PostMapping("/adminLogin")
        public String adminLogin(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model,
                                 HttpSession session) {

            if(username.equals("admin")
                    && password.equals("admin123")) {

                session.setAttribute("adminLoggedIn", true);

                return "redirect:/admin";
            }

            model.addAttribute("error",
                    "Invalid Admin Credentials");

            return "adminLogin";
        }@PostMapping("/savePass")
        public String savePass(HttpSession session,
                @RequestParam String passType,
                @RequestParam(required = false) String passCategory,
                @RequestParam(required = false) String source,
                @RequestParam(required = false) String destination,
                @RequestParam(required = false) String route,
                Model model) {

String email =
     (String) session.getAttribute("userEmail");

if(email == null) {
 model.addAttribute("error",
         "Please login first.");
 return "login";
}

List<User> users = repo.findByEmail(email);

if(users.isEmpty()) {

    model.addAttribute("error",
            "User not found.");

    return "applyPass";
}

User user = users.get(0);

if(user == null) {
 model.addAttribute("error",
         "User not found.");
 return "applyPass";
}
                if(user != null) {

                    if(user.getUserType().equals("Passenger")) {

                        user.setPassCategory(passCategory);

                        if("Fixed Route".equals(passCategory)) {

                            user.setSource(source);
                            user.setDestination(destination);
                            user.setRoute(route);
                        }
                    }
                 // Set Pass Type
                    user.setPassType(passType);

                    // Calculate Fee
                    double fee = 0;

                    if("Student".equals(user.getUserType())) {

                        if(user.getClassLevel() != null &&
                           user.getClassLevel() <= 6) {

                            fee = 0;

                        } else if(user.getClassLevel() != null &&
                                  user.getClassLevel() <= 10) {

                            if("Monthly".equals(passType))
                                fee = 100;
                            else if("Quarterly".equals(passType))
                                fee = 250;
                            else
                                fee = 450;

                        } else {

                            if("Monthly".equals(passType))
                                fee = 200;
                            else if("Quarterly".equals(passType))
                                fee = 500;
                            else
                                fee = 900;
                        }

                    } else {

                        if("Monthly".equals(passType))
                            fee = 500;
                        else if("Quarterly".equals(passType))
                            fee = 1400;
                        else
                            fee = 2700;
                    }

                    user.setFee(fee);

                    // Set Issue Date
                    java.time.LocalDate today =
                            java.time.LocalDate.now();

                    user.setIssueDate(today.toString());

                    // Set Expiry Date
                    if("Monthly".equals(passType)) {

                        user.setExpiryDate(
                                today.plusDays(30).toString());

                    } else if("Quarterly".equals(passType)) {

                        user.setExpiryDate(
                                today.plusDays(90).toString());

                    } else {

                        user.setExpiryDate(
                                today.plusDays(180).toString());
                    }

                    // Free Pass for Class 1-6 Students
                    if(fee == 0) {

                        user.setApprovalStatus(
                                "Pending Faculty Approval");

                        repo.save(user);

                        model.addAttribute("user", user);

                        return "paymentSuccess";
                    }

                    // Save User
                    repo.save(user);

                    model.addAttribute("user", user);

                    return "payment";
                }
                

               return "applyPass";
     
}@GetMapping("/passCard/{id}")
public String passCard(@PathVariable Long id,
        Model model) {

User user =
repo.findById(id).orElse(null);

if(user == null ||
!"Approved".equals(
user.getApprovalStatus())) {

return "redirect:/status";
}

model.addAttribute("user", user);

return "passCard";
}@PostMapping("/paymentSuccess")
public String paymentSuccess(HttpSession session,
        Model model) {

String email =
(String) session.getAttribute("userEmail");

if(email == null) {
return "login";
}

List<User> users = repo.findByEmail(email);

if(users.isEmpty()) {

model.addAttribute("error",
"User not found");

return "applyPass";
}

User user = users.get(users.size() - 1);

if("Passenger".equals(user.getUserType())) {

user.setApprovalStatus("Approved");

} else {

user.setApprovalStatus(
"Pending Faculty Approval");
}

if(user.getPassNumber() == null ||
user.getPassNumber().isEmpty()) {

user.setPassNumber(
"BP" + System.currentTimeMillis());
}

user.setIssueDate(
java.time.LocalDate.now().toString());

repo.save(user);

model.addAttribute("user", user);

return "paymentSuccess";
}@GetMapping("/reports")
public String reports(Model model) {

    model.addAttribute("totalUsers",
            repo.count());

    model.addAttribute("approved",
            repo.findByApprovalStatus("Approved")
                .size());

    model.addAttribute("pending",
            repo.findByApprovalStatus("Pending")
                .size());

    return "reports";
}@GetMapping("/registerPage")
public String registerPage() {

    return "index";
}
@GetMapping("/search")
public String search(@RequestParam String keyword,
                     Model model) {

    List<User> users =
            repo.findByNameContaining(keyword);

    model.addAttribute("users", users);

    return "admin";
}
@GetMapping("/downloadPass")
public String downloadPass(HttpSession session,
                           Model model) {

    String email =
            (String) session.getAttribute("userEmail");

    List<User> users =
            repo.findByEmail(email);

    if(users.isEmpty()) {

        return "login";
    }

    User user = users.get(0);

    if(user.getPassNumber() == null ||
       !"Approved".equals(user.getApprovalStatus())) {

        return "status";
    }

    model.addAttribute("user", user);

    return "downloadPass";
}
@GetMapping("/dashboard")
public String dashboard(HttpSession session,
                        Model model) {

    String email =
            (String) session.getAttribute("userEmail");

    if(email == null) {

        return "redirect:/login";
    }

    List<User> users = repo.findByEmail(email);

    if(users.isEmpty()) {

        return "redirect:/login";
    }

    User user = users.get(0);

    model.addAttribute("name", user.getName());

    return "dashboard";
}
        }
