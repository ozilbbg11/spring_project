package hkmu.comps380f.controller;

import hkmu.comps380f.dao.PollEntryRepository;
import hkmu.comps380f.model.CommentEntry;
import hkmu.comps380f.model.PollEntry;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import hkmu.comps380f.dao.CommentEntryRepository;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/guestbook")
public class PollpageController  {
    
    @Resource
    private PollEntryRepository pEntryRepo;
    @Resource
    private CommentEntryRepository gbEntryRepo;

    @GetMapping({"", "/view"})
    public String index(ModelMap model) {
        model.addAttribute("entries", pEntryRepo.listEntries());
        return "GuestBook";
    }
    
    @GetMapping("/addPoll")
    public ModelAndView addPollForm() {
        return new ModelAndView("AddPoll", "command", new PollEntry());
    }
    
    @PostMapping("/addPoll")
    public View addPollHandle(PollEntry entry) {
        pEntryRepo.addEntry(entry);
        return new RedirectView("/lecture", true);
    }
    
    @GetMapping("/editPoll")
    public String editPollForm(@RequestParam("id") Integer entryId, ModelMap model) {
        PollEntry entry = pEntryRepo.getEntryById(entryId);
        model.addAttribute("entry", entry);
        return "EditPoll";
    }
    
    @PostMapping("/editPoll")
    public View editPollHandle(PollEntry entry, HttpServletRequest req) throws ServletException, IOException {
        pEntryRepo.updateEntry(entry);
        return new RedirectView("/guestbook/comment?id=" + entry.getId() + "&" + req.getQueryString(), true);
    }

    @GetMapping("/delete")
    public String deletePollEntry(@RequestParam("id") Integer entryId) {
        pEntryRepo.removeEntryById(entryId);
        return "redirect:/";
    }

    @GetMapping("/comment")
    public String pollPage(@RequestParam("id") Integer entryId, ModelMap model) {
        PollEntry entry = pEntryRepo.getEntryById(entryId);
        model.addAttribute("entries", gbEntryRepo.listEntries());
        model.addAttribute("entry", entry);
        return "CommentPage";
    }

    @PostMapping("/comment")
    public View voteHandle(PollEntry entry) {
        return new RedirectView("/guestbook/comment?id=" + entry.getId(), true);
    }
    
    @GetMapping("/comment/add")
    public ModelAndView addCommentForm(@RequestParam("id") Integer entryId, ModelMap model) {
        PollEntry entry = pEntryRepo.getEntryById(entryId);
        model.addAttribute("entry", entry);
        return new ModelAndView("AddComment", "command", new CommentEntry());
    }
    
    @PostMapping("/comment/add")
    public View addCommentHandle(CommentEntry entry, PollEntry pEntry) {
        entry.setPollId(pEntry.getId());
        entry.setDate(new Date());
        gbEntryRepo.addEntry(entry);
        return new RedirectView("/guestbook/commentid=" + entry.getId(), true);
    }

    @GetMapping("/comment/delete")
    public String deleteCommentEntry(@RequestParam("id") Integer entryId) {
        gbEntryRepo.removeEntryById(entryId);
        return "redirect:/guestbook/commentid=" + entryId;
    }
}