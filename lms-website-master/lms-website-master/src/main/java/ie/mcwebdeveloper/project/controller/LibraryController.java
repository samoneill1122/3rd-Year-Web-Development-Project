package ie.mcwebdeveloper.project.controller;

import ie.mcwebdeveloper.project.UserSession;
import ie.mcwebdeveloper.project.models.Book;
import ie.mcwebdeveloper.project.models.LoanHistory;
import ie.mcwebdeveloper.project.models.User;
import ie.mcwebdeveloper.project.repositories.BookRepository;
import ie.mcwebdeveloper.project.repositories.LoanHistoryRepository;
import ie.mcwebdeveloper.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LibraryController {

    @Autowired
    private UserSession userSession;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    LoanHistoryRepository loanHistoryRepository;

    @GetMapping("/")
    public String getLanding(Model model) {
        model.addAttribute("title", "LMS - Home");
        model.addAttribute("user", userSession.getUser());
        return "index.html";
    }

    @GetMapping("/books")
    public String getSearch(@RequestParam (value = "search", required = false) String search, Model model) {
        model.addAttribute("title", "LMS - Search");
        List<Book> books = bookRepository.findAllByTitleOrAuthorLike(search);
        model.addAttribute("books", books);
        model.addAttribute("user", userSession.getUser());
        return "search.html";
    }

    @PostMapping("/admin/loan-book-to-member/{id}")
    public String loanBookToMember(@PathVariable String id, @RequestParam (value = "loanee", required = false) String loanee, Model model) {
        long i = Long.parseLong(id);
        long loaneeID = Long.parseLong(loanee);
        LocalDate currDate = LocalDate.now();
        LocalDate newDate = currDate.plusWeeks(1);
        Date dueDate = Date.valueOf(newDate);
        bookRepository.loanToMember(loaneeID, i, dueDate);
        model.addAttribute("user", userSession.getUser());
        return "redirect:/books";
    }

    @PostMapping("/admin/reserve-book-to-member/{id}")
    public String reserveBookToMember(@PathVariable String id, @RequestParam (value = "reservee", required = false) String reservee, Model model) {
        long i = Long.parseLong(id);
        long reserveeID = Long.parseLong(reservee);
        bookRepository.reserveToMember(reserveeID, i);
        model.addAttribute("user", userSession.getUser());
        return "redirect:/books";
    }

    @PostMapping("/admin/delete-book/{id}")
    public String deleteBook(@PathVariable String id, Model model) {
        long i = Long.parseLong(id);
        bookRepository.deleteById(i);
        model.addAttribute("user", userSession.getUser());
        return "redirect:/books";
    }

    @PostMapping("/admin/mark-book-as-returned/{id}")
    public String returnBook(@PathVariable String id, Model model) {
        long i = Long.parseLong(id);
        Long ID = null;
        Date date = null;
        Book b = bookRepository.findById(i).get();
        bookRepository.returnBook(i, date, ID);
        LoanHistory lh = new LoanHistory(i, b.getUserid());
        loanHistoryRepository.save(lh);
        model.addAttribute("user", userSession.getUser());
        return "redirect:/books";
    }

    //    @PostMapping("/")
//    public String loginMember() {
//        return "/";
//    }
    // Browse
//    @GetMapping("/browse")
//    public String getBrowse() {
//        return "browse.html";
//    }
    // View Library (Show)
//    @GetMapping("/browse/{id}")
//    public String showLibrary() {
//        return "browse.html";
//    }

    @GetMapping("/user/profile/{id}")
    public String getProfile(@PathVariable String id, Model model){
        model.addAttribute("title", "LMS - Profile");
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - Profile");
            model.addAttribute("user", userSession.getUser());
            return "profile.html";
        }
    }

    @PostMapping("/user/profile")
    public String changeProfile(User theUser, Model model) {
        User currUser = userSession.getUser();
        boolean usernameTaken = false, emailTaken = false;
        if(userRepository.existsByUsername(theUser.getUsername()))
            usernameTaken = true;
        if(userRepository.existsByEmail(theUser.getEmail()))
            emailTaken = true;

        if(!usernameTaken && !emailTaken) {
            userRepository.updateUser(theUser.getUsername(), theUser.getFirstname(), theUser.getLastname(), theUser.getEmail(), theUser.getPassword(), currUser.getId());
            model.addAttribute("success", true);
            model.addAttribute("user", userSession.getUser());
            return "redirect:/user/profile/" + currUser.getId();
        } else {
            model.addAttribute("success", false);
            model.addAttribute("user", userSession.getUser());
            return "redirect:/user/profile/" + currUser.getId();
        }
    }

    @GetMapping("/admin/manage/{id}")
    public String getManage(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - Manage");
            model.addAttribute("user", userSession.getUser());
            return "manage.html";
        }
    }

    @PostMapping("/admin/manage")
    public String addBook(Book theBook, Model model) {
        bookRepository.save(theBook);
        model.addAttribute("user", userSession.getUser());
        return "redirect:/admin/manage/" + userSession.getUser().getId();
    }

    @GetMapping("/admin/manage/view-members")
    public String viewMembers(Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - View Members");
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            model.addAttribute("user", userSession.getUser());
            return "viewmembers.html";
        }
    }

    @GetMapping("/admin/manage/view-members/search")
    public String searchMembers(@RequestParam (value = "search", required = false) String search, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - Search Results of Members");
            List<User> users = userRepository.findAllByUsernameOrFirstnameOrLastnameLike(search);
            model.addAttribute("users", users);
            model.addAttribute("user", userSession.getUser());
            return "viewmembers.html";
        }
    }

//    @GetMapping("admin/manage/view-books")
//    public String viewBooks(Model model) {
//        if(userSession.getUser() == null) {
//            return "redirect:/login";
//        } else {
//            model.addAttribute("title", "LMS - View Books");
//            List<Book> books = bookRepository.findAll();
//            model.addAttribute("books", books);
//            model.addAttribute("user", userSession.getUser());
//            return "viewbooks.html";
//        }
//    }

    @GetMapping("view-books")
    public String viewBooks(Model model) {
        model.addAttribute("title", "LMS - View Books");
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("user", userSession.getUser());
        return "viewbooks.html";
    }

    @GetMapping("admin/manage/view-members/{id}")
    public String viewMember(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - Edit Member");
            Long i = Long.parseLong(id);
            User member = userRepository.findById(i).get();
            model.addAttribute("member", member);
            model.addAttribute("user", userSession.getUser());
            return "editmember.html";
        }
    }


    @GetMapping("/admin/manage/view-members/{id}/edit")
    public String editMemberProfile(@PathVariable String id, Model model){
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - Member Profile");
            Long i = Long.parseLong(id);
            User member = userRepository.findById(i).get();
            model.addAttribute("member", member);
            model.addAttribute("user", userSession.getUser());
            return "profile.html";
        }
    }

@PostMapping("/admin/manage/view-members/{id}/edit")
public String editProfile(User theUser, @PathVariable String id, Model model) {
    Long i = Long.parseLong(id);
    User member = userRepository.findById(i).get();
    boolean usernameTaken = false;
    if(userRepository.existsByUsername(theUser.getUsername()))
        usernameTaken = true;
    if(!usernameTaken) {
        userRepository.updateUser(theUser.getUsername(), theUser.getFirstname(), theUser.getLastname(), theUser.getEmail(), theUser.getPassword(), member.getId());
        model.addAttribute("success", true);
        return "redirect:/admin/manage/view-members";
    } else {
        model.addAttribute("success", false);
        model.addAttribute("user", userSession.getUser());
        return "redirect:/admin/manage/view-members";
    }
}

    @GetMapping("/admin/manage/view-members/{id}/delete")
    public String deleteMembers(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            Long i = Long.parseLong(id);
            userRepository.deleteById(i);
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            model.addAttribute("user", userSession.getUser());
            return "redirect:/admin/manage/view-members";
        }
    }

    @GetMapping("/admin/manage/view-members/{id}/loans")
    public String memberLoans(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - Member Loans");
            Long i = Long.parseLong(id);
            User member = userRepository.findById(i).get();
            List<Book> currBooks = bookRepository.findAllByUserid(i);
            Long[] pastBookIds = loanHistoryRepository.findLoanHistoryOfUser(member.getId());
            List<Book> pastBooks = new ArrayList<Book>(pastBookIds.length);
            for(long ID : pastBookIds) {
                Book book = bookRepository.findById(ID).get();
                pastBooks.add(book);
            }
            model.addAttribute("currBooks", currBooks);
            model.addAttribute("pastBooks", pastBooks);
            model.addAttribute("member", member);
            model.addAttribute("user", userSession.getUser());
            return "loaninfo.html";
        }
    }

    @GetMapping("/admin/manage/view-members/{memberId}/{bookId}/renew")
    public String renewMemberLoan(@PathVariable String memberId, @PathVariable String bookId, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            long i = Long.parseLong(bookId);
            Optional<Book> book = bookRepository.findById(i);
            Book b = book.get();
            LocalDate oldDate = b.getDuedate().toLocalDate();
            LocalDate newDate = oldDate.plusWeeks(1);
            Date result = Date.valueOf(newDate);
            bookRepository.renewLoan(result, i);
//        model.addAttribute("message", "Loan renewed successfully.");
            long id = Long.parseLong(memberId);
            User member = userRepository.findById(id).get();
            model.addAttribute("user", member);
            return "redirect:/admin/manage/view-members/" + member.getId() + "/loans";
        }
    }

    @GetMapping("user/profile/{id}/loans")
    public String viewLoans(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("title", "LMS - View Loans");
            Long i = Long.parseLong(id);
            Optional<User> user = userRepository.findById(i);
            List<Book> currBooks = bookRepository.findAllByUserid(i);
            Long[] pastBookIds = loanHistoryRepository.findLoanHistoryOfUser(userSession.getUser().getId());
            List<Book> pastBooks = new ArrayList<Book>(pastBookIds.length);
            for(long ID : pastBookIds) {
                Book book = bookRepository.findById(ID).get();
                pastBooks.add(book);
            }
            model.addAttribute("currBooks", currBooks);
            model.addAttribute("pastBooks", pastBooks);
            model.addAttribute("user", userSession.getUser());
            return "loaninfo.html";
        }
    }

    @GetMapping("/user/profile/loans/{id}/renew")
    public String renewLoan(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            long i = Long.parseLong(id);
            Optional<Book> book = bookRepository.findById(i);
            Book b = book.get();
            LocalDate oldDate = b.getDuedate().toLocalDate();
            LocalDate newDate = oldDate.plusWeeks(1);
            Date result = Date.valueOf(newDate);
            bookRepository.renewLoan(result, i);
//        model.addAttribute("message", "Loan renewed successfully.");
            model.addAttribute("user", userSession.getUser());
            return "redirect:/user/profile/" + userSession.getUser().getId() + "/loans";
        }
    }

    @GetMapping("/admin/renew-loan-for-member/{id}")
    public String renewLoanForMember(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            long i = Long.parseLong(id);
            Optional<Book> book = bookRepository.findById(i);
            Book b = book.get();
            LocalDate oldDate = b.getDuedate().toLocalDate();
            LocalDate newDate = oldDate.plusWeeks(1);
            Date result = Date.valueOf(newDate);
            bookRepository.renewLoan(result, i);
//        model.addAttribute("message", "Loan renewed successfully.");
            model.addAttribute("user", userSession.getUser());
            return "redirect:/books";
        }
    }

    @GetMapping("/user/loan-book/{id}")
    public String loanBook(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            long i = Long.parseLong(id);
            Optional<Book> book = bookRepository.findById(i);
            Book b = book.get();
            bookRepository.newLoanee(userSession.getUser().getId(), i);
//          model.addAttribute("message", "Loaned book successfully.");
            model.addAttribute("user", userSession.getUser());
            return "redirect:/books";
        }
    }

    @GetMapping("/user/reserve-book/{id}")
    public String reserveBook(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            long i = Long.parseLong(id);
            Optional<Book> book = bookRepository.findById(i);
            Book b = book.get();
            bookRepository.newReservee(userSession.getUser().getId(), i);
            model.addAttribute("user", userSession.getUser());
//          model.addAttribute("message", "Reserved book successfully.");
            return "redirect:/books";
        }
    }

    @GetMapping("/admin/manage/edit-book/{id}")
    public String editBook(@PathVariable String id, Model model) {
        if(userSession.getUser() == null) {
            return "redirect:/login";
        } else {
            long i = Long.parseLong(id);
            Optional<Book> book = bookRepository.findById(i);
            Book b = book.get();
            model.addAttribute("user", userSession.getUser());
            model.addAttribute("book", b);
//          model.addAttribute("message", "Reserved book successfully.");
            return "editbook.html";
        }
    }

}