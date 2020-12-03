package com.example.books.controllers;

import java.util.List;

import javax.validation.Valid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.books.entities.Book;
import com.example.books.service.BookService;

@Controller
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@RequestMapping("/showCreate")
	public String showCreate(ModelMap modelMap) {
		modelMap.addAttribute("book", new Book());
		modelMap.addAttribute("mode", "new");
		return "formBook";
	}
	
	@RequestMapping("/saveBook")
	public String saveBook(@Valid Book book, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) return "createBook";
		bookService.saveBook(book);
		return "formBook";
	}
	
	@RequestMapping("/bookList")
	public String listBooks(ModelMap modelMap,
			@RequestParam (name="page",defaultValue = "0") int page,
			@RequestParam (name="size", defaultValue = "2") int size) {
		Page<Book> books = bookService.getAllBooksParPage(page, size);
		modelMap.addAttribute("books", books);
		 modelMap.addAttribute("pages", new int[books.getTotalPages()]);
		modelMap.addAttribute("currentPage", page);
		return "bookList";
	}
	
	@RequestMapping("/deleteBook")
	public String deleteBookt(@RequestParam("id") Long id,
			ModelMap modelMap,
			@RequestParam (name="page",defaultValue = "0") int page,
			@RequestParam (name="size", defaultValue = "2") int size) {
		bookService.deleteBookById(id);
		Page<Book> prods = bookService.getAllBooksParPage(page,
				size);
		modelMap.addAttribute("books", prods);
		modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
		modelMap.addAttribute("currentPage", page);
		modelMap.addAttribute("size", size);
		return "bookList";
	}
	
	@RequestMapping("/modifyBook")
	public String modifyBook(@RequestParam("id") Long id,ModelMap modelMap) {
		Book book = bookService.getBook(id);
		modelMap.addAttribute("book", book);
		modelMap.addAttribute("mode", "edit");
		return "formBook";
	}
	
	@RequestMapping("/updateBook")
	public String updateBook(@ModelAttribute("book") Book book,
	@RequestParam("date") String date,
		 ModelMap modelMap) throws ParseException {
		 //conversion de la date
		 SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		 Date dateCreation = dateformat.parse(String.valueOf(date));
		 book.setDateCreation(dateCreation);
		 bookService.updateBook(book);
		 List<Book> books = bookService.getAllBooks();
		 modelMap.addAttribute("books", books);
		 return "bookList";
	}
	
}
