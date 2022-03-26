package com.sadatmalik.fusionweb.controllers;

import com.sadatmalik.fusionweb.exceptions.NoSuchIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyExpenseException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchWeeklyExpenseException;
import com.sadatmalik.fusionweb.mappers.IncomeExpenseMapper;
import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;
import com.sadatmalik.fusionweb.model.websecurity.UserPrincipal;
import com.sadatmalik.fusionweb.services.IncomeExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * Spring MVC controller that is the primary interface for handling all endpoints
 * relating to CRUD operations on income and expenses - both types - weekly and
 * monthly are handled by this controller class.
 *
 * @author sadatmalik
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomeExpenseController {

    private final IncomeExpenseService incomeExpenseService;

    /**
     * This endpoint returns the main landing page template for income and expenses.
     * The {@code loadFormBindingObjects(model)} call populates the MVC model with a
     * set of empty DTOs (Data Transfer Objects) that are intended to be bound to the
     * corresponding HTML forms in the view.
     *
     * For example, an 'empty' MonthlyExpenseDto is passed with the MVC model, with
     * the intention of it being populated by the user, for instance, with the form
     * submission of a new monthly expense.
     *
     * Similarly, WeeklyExpenseDto, WeeklyIncomeDto and MonthlyIncomeDto objects are
     * created and attached to the MVC model, for binding to the corresponding view
     * forms. The form rendering itself is handled by the returned view template.
     *
     * The method call to {@code loadTableData(authentication, model)} will fetch the
     * weekly and monthly income and expenses for the user corresponding to the
     * current authentication.
     *
     * @param authentication provided by the Spring context
     * @param model provided by the Spring context
     * @return returns the income-and-expense view
     */
    @GetMapping({"/income-and-expenses"})
    public String incomeAndExpenses(Authentication authentication, Model model) {
        log.info("Returning income and expenses page");

        loadFormBindingObjects(model);
        loadTableData(authentication, model);

        return "income-and-expenses";
    }

    private void loadFormBindingObjects(Model model) {
        model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
        model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
        model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
        model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());
    }

    private void loadTableData(Authentication authentication, Model model) {
        model.addAttribute("monthlyExpenseList", getMonthlyExpenses(authentication));
        model.addAttribute("weeklyExpenseList", getWeeklyExpenses(authentication));
        model.addAttribute("monthlyIncomeList", getMonthlyIncome(authentication));
        model.addAttribute("weeklyIncomeList", getWeeklyIncome(authentication));
    }

    private List<MonthlyExpense> getMonthlyExpenses(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getMonthlyExpensesFor(user);
    }

    private List<WeeklyExpense> getWeeklyExpenses(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getWeeklyExpensesFor(user);
    }

    private List<MonthlyIncome> getMonthlyIncome(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getMonthlyIncomeFor(user);
    }

    private List<Income> getWeeklyIncome(Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        // @todo convert to Dto list?
        return incomeExpenseService.getWeeklyIncomeFor(user);
    }

    /**
     * Receives a populated monthly expense data transfer object from the corresponding HTML
     * form post to the handled endpoint.
     *
     * Returns a populated binding result if the @Valid annotation checks yield any validation
     * errors in the submitted DTO object.
     *
     * Otherwise, the DTO object is passed on to the {@code IncomeExpenseService} instance
     * where a DTO conversion to MonthlyExpense and save will be attempted.
     *
     * @param monthlyExpenseDto submitted by the HTML form
     * @param bindingResult populated and returned with the MVC model if there are
     *                      validation errors in the DTO submission
     * @param authentication provided by the Spring context
     * @param model injected by the Spring context
     *
     * @return redirects to success view on successful save - or returns validation errors
     * to the submitting form or passes to an error view if save fails
     */
    @PostMapping("/income-and-expenses/new-monthly-expense")
    public String saveMonthlyExpense(@ModelAttribute("monthlyExpenseDto") @Valid MonthlyExpenseDto monthlyExpenseDto,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - MonthlyExpenseDto has validation errors");
            model.addAttribute("showNewMonthlyExpenseForm", true);

            model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
            model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
            model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new monthly expense - " + monthlyExpenseDto);
            MonthlyExpense saved = incomeExpenseService.saveMonthlyExpense(monthlyExpenseDto, Utils.getUser(authentication));
            log.debug("Saved new monthly expense to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * Receives a populated weekly expense data transfer object from the corresponding HTML
     * form POST to this method's endpoint.
     *
     * Returns a populated binding result if the {@code @Valid} annotation checks yield any
     * validation errors in the received DTO object.
     *
     * If validation checks pass, then the DTO object is passed on to the {@code
     * IncomeExpenseService} instance where a DTO conversion to WeeklyExpense and save will
     * be attempted.
     *
     * @param weeklyExpenseDto submitted by the HTML form.
     * @param bindingResult populated and returned with the MVC model if there are
     *                      validation errors in the DTO submission.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to main view on a successful save attempt - or returns any
     * validation errors to the submitting form if DTO validation checks failed, or returns
     * an error view if save fails.
     */
    @PostMapping("/income-and-expenses/new-weekly-expense")
    public String saveWeeklyExpense(@ModelAttribute("weeklyExpenseDto") @Valid WeeklyExpenseDto weeklyExpenseDto,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - WeeklyExpenseDto has validation errors");
            model.addAttribute("showNewWeeklyExpenseForm", true);

            model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
            model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());
            model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new weekly expense - " + weeklyExpenseDto);
            WeeklyExpense saved =
                    incomeExpenseService.saveWeeklyExpense(weeklyExpenseDto,
                            Utils.getUser(authentication));
            log.debug("Saved new weekly expense to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * Receives a populated monthly income data transfer object from the corresponding HTML
     * form POST to this method's endpoint.
     *
     * Returns a populated binding result if the {@code @Valid} annotation checks yield any
     * validation errors in the received DTO object.
     *
     * If validation checks pass, then the DTO object is passed on to the {@code
     * IncomeExpenseService} instance where a DTO conversion to MonthlyIncome and save will
     * be attempted.
     *
     * @param monthlyIncomeDto submitted by the HTML form.
     * @param bindingResult populated and returned with the MVC model if there are
     *                      validation errors in the DTO submission.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to main view on a successful save attempt - or returns any
     * validation errors to the submitting form if DTO validation checks fail, or returns
     * an error view if save attempt fails.
     */
    @PostMapping("/income-and-expenses/new-monthly-income")
    public String saveMonthlyIncome(@ModelAttribute("monthlyIncomeDto") @Valid MonthlyIncomeDto monthlyIncomeDto,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - MonthlyIncomeDto has validation errors");
            model.addAttribute("showNewMonthlyIncomeForm", true);

            model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
            model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
            model.addAttribute("weeklyIncomeDto", new WeeklyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new monthly income - " + monthlyIncomeDto);
            MonthlyIncome saved = incomeExpenseService.saveMonthlyIncome(monthlyIncomeDto, Utils.getUser(authentication));
            log.debug("Saved new monthly income to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * Receives a populated weekly income data transfer object from the corresponding HTML
     * form POST to this method's endpoint.
     *
     * Returns a populated binding result if the {@code @Valid} annotation checks yield any
     * validation errors in the received DTO object.
     *
     * If validation checks pass, then the DTO object is passed on to the {@code
     * IncomeExpenseService} instance where a DTO conversion to MonthlyIncome and save will
     * be attempted.
     *
     * @param weeklyIncomeDto submitted by the HTML form.
     * @param bindingResult populated and returned with the MVC model if there are
     *                      validation errors in the DTO submission.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to main view on a successful save attempt - or returns with
     * validation errors to the submitting form if DTO validation checks fail, or returns
     * an error view if save attempt fails.
     */
    @PostMapping("/income-and-expenses/new-weekly-income")
    public String saveWeeklyIncome(@ModelAttribute("weeklyIncomeDto") @Valid WeeklyIncomeDto weeklyIncomeDto,
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - WeeklyIncomeDto has validation errors");
            model.addAttribute("showNewWeeklyIncomeForm", true);

            model.addAttribute("monthlyExpenseDto", new MonthlyExpenseDto());
            model.addAttribute("weeklyExpenseDto", new WeeklyExpenseDto());
            model.addAttribute("monthlyIncomeDto", new MonthlyIncomeDto());

            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Saving new weekly income - " + weeklyIncomeDto);
            Income saved = incomeExpenseService.saveWeeklyIncome(weeklyIncomeDto, Utils.getUser(authentication));
            log.debug("Saved new weekly income to DB - " + saved);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * This method handles an endpoint request for monthly expense details for a specific
     * id. The id is sent in the HTML GET request as a path variable "expenseId".
     *
     * The corresponding monthly expense is retrieved from the {@code IncomeExpenseService}
     * instance. The retrieved data is sent with the MVC model and will subsequently be
     * viewable in the corresponding HTML form template.
     *
     * @param id received as an HTML GET request path parameter.
     * @param model injected by the Spring context.
     * @param authentication provided by the Spring context.
     * @return returns the income-and-expense view.
     */
    @GetMapping("/income-and-expenses/{expenseId}/view")
    public String viewMonthlyExpenseDetails(@PathVariable("expenseId") String id, Model model,
                                            Authentication authentication) {
        log.debug("View MonthlyExpense details");

        // retrieve the MonthlyExpense
        MonthlyExpense monthlyExpense = incomeExpenseService.getMonthlyExpenseFor(Long.parseLong(id));

        // set up the forms Dto
        model.addAttribute("editableMonthlyExpenseDto",
                IncomeExpenseMapper.monthlyExpenseToMonthlyExpenseDto(monthlyExpense));
        model.addAttribute("showMonthlyExpenseEditDeleteForm", true);

        loadFormBindingObjects(model);
        loadTableData(authentication, model);
        return "income-and-expenses";
    }

    /**
     * This method handles the endpoint requesting weekly expense details for a specific
     * id. The id is sent in the HTML GET request as a path variable "expenseId".
     *
     * The corresponding weekly expense is retrieved from the {@code IncomeExpenseService}
     * instance. The retrieved data is returned with the MVC model and will subsequently be
     * viewable in the corresponding HTML form template.
     *
     * @param id received as an HTML GET request path parameter.
     * @param model injected by the Spring context.
     * @param authentication provided by the Spring context.
     * @return returns the income-and-expense view.
     */
    @GetMapping("/income-and-expenses/weekly-expense/{expenseId}/view")
    public String viewWeeklyExpenseDetails(@PathVariable("expenseId") String id, Model model,
                                            Authentication authentication) {
        log.debug("View WeeklyExpense details - for expense id " + id);

        // retrieve the WeeklyExpense
        WeeklyExpense weeklyExpense = incomeExpenseService.getWeeklyExpenseFor(Long.parseLong(id));

        // set up the forms Dto
        model.addAttribute("editableWeeklyExpenseDto",
                IncomeExpenseMapper.weeklyExpenseToWeeklyExpenseDto(weeklyExpense));
        model.addAttribute("showWeeklyExpenseEditDeleteForm", true);

        loadFormBindingObjects(model);
        loadTableData(authentication, model);
        return "income-and-expenses";
    }

    /**
     * This method handles the endpoint requesting monthly income details for a specific
     * id. The id is sent in the HTML GET request as a path variable "incomeId".
     *
     * The corresponding monthly income is retrieved via the {@code IncomeExpenseService}
     * instance. The retrieved data is returned with the MVC model and will subsequently be
     * viewable in the corresponding HTML form template.
     *
     * @param id received as an HTML GET request path parameter.
     * @param model injected by the Spring context.
     * @param authentication provided by the Spring context.
     * @return returns the income-and-expense view.
     */
    @GetMapping("/income-and-expenses/monthly-income/{incomeId}/view")
    public String viewMonthlyIncomeDetails(@PathVariable("incomeId") String id, Model model,
                                           Authentication authentication) {
        log.debug("View MonthlyIncome details - for income id " + id);

        // retrieve the MonthlyIncome
        MonthlyIncome monthlyIncome = incomeExpenseService.getMonthlyIncomeFor(Long.parseLong(id));

        // set up the forms Dto
        model.addAttribute("editableMonthlyIncomeDto",
                IncomeExpenseMapper.monthlyIncomeToMonthlyIncomeDto(monthlyIncome));
        model.addAttribute("showMonthlyIncomeEditDeleteForm", true);

        loadFormBindingObjects(model);
        loadTableData(authentication, model);
        return "income-and-expenses";
    }

    /**
     * This method handles the endpoint requesting weekly income details for a specific
     * id. The id is sent in the HTML GET request as a path variable "incomeId".
     *
     * The corresponding weekly income is retrieved via the {@code IncomeExpenseService}
     * instance. The retrieved data is returned with the MVC model and will subsequently be
     * viewable in the corresponding HTML form template.
     *
     * @param id received as an HTML GET request path parameter.
     * @param model injected by the Spring context.
     * @param authentication provided by the Spring context.
     * @return returns the income-and-expense view.
     */
    @GetMapping("/income-and-expenses/weekly-income/{incomeId}/view")
    public String viewWeeklyIncomeDetails(@PathVariable("incomeId") String id, Model model,
                                           Authentication authentication) {
        log.debug("View WeeklyIncome details - for income id " + id);

        // retrieve the WeeklyIncome
        Income weeklyIncome = incomeExpenseService.getWeeklyIncomeFor(Long.parseLong(id));

        // set up the forms Dto
        model.addAttribute("editableWeeklyIncomeDto",
                IncomeExpenseMapper.incomeToWeeklyIncomeDto(weeklyIncome));
        model.addAttribute("showWeeklyIncomeEditDeleteForm", true);

        loadFormBindingObjects(model);
        loadTableData(authentication, model);
        return "income-and-expenses";
    }

    /**
     * This endpoint handles the form POST submission of an edited monthly expense data.
     * The edited data is sent as a DTO object. The corresponding database id is sent as a
     * path variable "expenseId"
     *
     * The DTO object is checked for validation errors, returning a populated binding result
     * in the returned model if the DTO validation fails.
     *
     * Otherwise, the DTO is passed to the {@code IncomeExpenseService} which will attempt
     * to update the database with the revised data. An error view is returned should this
     * fail, otherwise, the flow is redirected to the income-and-expense endpoint.
     *
     * @param expenseId received as an HTML POST request path variable.
     * @param monthlyExpenseDto received via the HTML form submission.
     * @param bindingResult will contain any validation errors.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to the income-and-expense endpoint on a successful update attempt -
     * or returns any validation errors to the submitting form if the DTO validation checks
     * should fail, else returns an error view if the update attempt fails.
     */
    @PostMapping("/income-and-expenses/monthly-expense/{expenseId}/edit")
    public String updateMonthlyExpense(@PathVariable("expenseId") Long expenseId,
                                       @ModelAttribute("monthlyExpenseDto") @Valid MonthlyExpenseDto monthlyExpenseDto,
                                       BindingResult bindingResult,
                                       Authentication authentication,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - MonthlyExpenseDto has validation errors");
            model.addAttribute("showMonthlyExpenseEditDeleteForm", true);

            loadFormBindingObjects(model);
            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Updating monthly expense - " + monthlyExpenseDto);
            monthlyExpenseDto.setId(expenseId);
            MonthlyExpense updated = incomeExpenseService.updateMonthlyExpense(monthlyExpenseDto);
            log.debug("Updated new monthly expense in DB - " + updated);
        } catch (IllegalStateException | NoSuchMonthlyExpenseException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * This endpoint handles the form POST submission of a edited weekly expense data.
     * The edited data is received as a DTO object parameter. The corresponding database id
     * is received as a path variable "expenseId"
     *
     * The DTO object, annoted {@code @Valid} will be checked for validation errors,
     * returning a populated binding result in the returned model if the DTO validation
     * fails.
     *
     * Otherwise, the DTO is passed to the {@code IncomeExpenseService} which will attempt
     * to update the database with the revised data. An error view is returned should the
     * update attempt fail, otherwise, the flow is redirected to the income-and-expense
     * endpoint.
     *
     * @param expenseId received as an HTML POST request path variable.
     * @param weeklyExpenseDto received via the HTML form submission.
     * @param bindingResult will contain any validation errors.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to the income-and-expense endpoint on a successful update attempt -
     * or returns any validation errors to the submitting form if the DTO validation checks
     * fail, else returns an error view if the update attempt fails.
     */
    @PostMapping("/income-and-expenses/weekly-expense/{expenseId}/edit")
    public String updateWeeklyExpense(@PathVariable("expenseId") Long expenseId,
                                      @ModelAttribute("weeklyExpenseDto") @Valid WeeklyExpenseDto weeklyExpenseDto,
                                      BindingResult bindingResult,
                                      Authentication authentication,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - WeeklyExpenseDto has validation errors");
            model.addAttribute("showWeeklyExpenseEditDeleteForm", true);

            loadFormBindingObjects(model);
            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Updating weekly expense - " + weeklyExpenseDto);
            weeklyExpenseDto.setId(expenseId);
            WeeklyExpense updated = incomeExpenseService.updateWeeklyExpense(weeklyExpenseDto);
            log.debug("Updated new weekly expense in DB - " + updated);
        } catch (IllegalStateException | NoSuchWeeklyExpenseException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * This endpoint handles the form POST submission of edited monthly income data.
     * The edited data is received as a DTO object parameter. The corresponding database id
     * is received as a path variable "incomeId"
     *
     * The DTO object, annotated {@code @Valid} will be checked for validation errors,
     * returning a populated binding result in the returned model if the DTO validation
     * fails.
     *
     * Otherwise, the DTO is passed to the {@code IncomeExpenseService} which will attempt
     * to update the database with the revised data. An error view is returned should the
     * update attempt fail, otherwise, the flow is redirected to the income-and-expense
     * endpoint.
     *
     * @param incomeId received as an HTML POST request path variable.
     * @param monthlyIncomeDto received via the HTML form submission.
     * @param bindingResult will contain any validation errors.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to the income-and-expense endpoint on a successful update attempt -
     * or returns any validation errors to the submitting form if the DTO validation checks
     * fail, else returns an error view if the update attempt fails.
     */
    @PostMapping("/income-and-expenses/monthly-income/{incomeId}/edit")
    public String updateMonthlyIncome(@PathVariable("incomeId") Long incomeId,
                                      @ModelAttribute("monthlyIncomeDto") @Valid MonthlyIncomeDto monthlyIncomeDto,
                                      BindingResult bindingResult,
                                      Authentication authentication,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - MonthlyIncomeDto has validation errors");
            model.addAttribute("showMonthlyIncomeEditDeleteForm", true);

            loadFormBindingObjects(model);
            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Updating monthly income - " + monthlyIncomeDto);
            monthlyIncomeDto.setId(incomeId);
            MonthlyIncome updated = incomeExpenseService.updateMonthlyIncome(monthlyIncomeDto);
            log.debug("Updated new monthly income in DB - " + updated);
        } catch (IllegalStateException | NoSuchMonthlyIncomeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * This endpoint handles the form POST submission of edited weekly income data.
     * The edited data is received as a DTO object parameter. The corresponding database id
     * is received as a path variable "incomeId"
     *
     * The DTO object, annotated {@code @Valid} will be checked for validation errors,
     * returning a populated binding result in the returned model if the DTO validation
     * fails.
     *
     * Otherwise, the DTO is passed to the {@code IncomeExpenseService} which will attempt
     * to update the database with the revised data. An error view is returned should the
     * update attempt fail, otherwise, the application flow is redirected to the
     * income-and-expense endpoint.
     *
     * @param incomeId received as an HTML POST request path variable.
     * @param weeklyIncomeDto received via the HTML form submission.
     * @param bindingResult will contain any validation errors.
     * @param authentication provided by the Spring context.
     * @param model injected by the Spring context.
     *
     * @return redirects to the income-and-expense endpoint on a successful update attempt -
     * or returns any validation errors to the submitting form if the DTO validation checks
     * fail, else returns an error view if the update attempt fails.
     */
    @PostMapping("/income-and-expenses/weekly-income/{incomeId}/edit")
    public String updateWeeklyIncome(@PathVariable("incomeId") Long incomeId,
                                     @ModelAttribute("weeklyIncomeDto") @Valid WeeklyIncomeDto weeklyIncomeDto,
                                     BindingResult bindingResult,
                                     Authentication authentication,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Validation errors on form submission - WeeklyIncomeDto has validation errors");
            model.addAttribute("showWeeklyIncomeEditDeleteForm", true);

            loadFormBindingObjects(model);
            loadTableData(authentication, model);
            return "income-and-expenses";
        }

        try {
            log.debug("Updating weekly income - " + weeklyIncomeDto);
            weeklyIncomeDto.setId(incomeId);
            Income updated = incomeExpenseService.updateWeeklyIncome(weeklyIncomeDto);
            log.debug("Updated new weekly income in DB - " + updated);
        } catch (IllegalStateException | NoSuchIncomeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

        return "redirect:/income-and-expenses";
    }

    /**
     * Handler for delete monthly expense endpoint requests.
     *
     * The monthly expense "id" for the expense requested for deletion, is sent as a
     * path variable with the HTTP GET request.
     *
     * The {@code IncomeExpenseService} instance handles the actual deletion, with the
     * application flow redirected to the income-and-expense endpoint on successful
     * processing.
     *
     * Following the redirect, the income and expense tables will be reloaded,
     * reflecting the deleted item.
     *
     * @param id received as a path variable with the HTTP GET request.
     * @param model provided by the Spring context.
     * @param authentication injected by the Spring context.
     * @return redirects to the income-and-expense endpoint handler.
     */
    @GetMapping("/income-and-expenses/{expenseId}/delete")
    public String deleteMonthlyExpense(@PathVariable("expenseId") String id, Model model,
                                       Authentication authentication) {
        // delete the MonthlyExpense
        incomeExpenseService.deleteMonthlyExpenseFor(Long.parseLong(id));
        return "redirect:/income-and-expenses";
    }

    /**
     * Handler for delete weekly expense endpoint requests.
     *
     * The weekly expense "id" for the expense that is to be deleted, is sent as a
     * path variable with the HTTP GET request.
     *
     * The {@code IncomeExpenseService} instance handles the actual deletion, with the
     * application flow redirected to the income-and-expense endpoint on successful
     * processing.
     *
     * Following the redirect, the income and expense tables will be reloaded,
     * reflecting the deleted item.
     *
     * @param id received as a path variable with the HTTP GET request.
     * @param model provided by the Spring context.
     * @param authentication injected by the Spring context.
     * @return redirects to the income-and-expense endpoint handler.
     */
    @GetMapping("/income-and-expenses/weekly-expense/{expenseId}/delete")
    public String deleteWeeklyExpense(@PathVariable("expenseId") String id, Model model,
                                       Authentication authentication) {
        // delete the WeeklyExpense
        incomeExpenseService.deleteWeeklyExpenseFor(Long.parseLong(id));
        return "redirect:/income-and-expenses";
    }

    /**
     * Handler for delete monthly income requests.
     *
     * The monthly income "id" for the income that is to be deleted, is sent as a
     * path variable with the HTTP GET request.
     *
     * The {@code IncomeExpenseService} instance handles the actual deletion, with the
     * application flow redirected to the income-and-expense endpoint on successful
     * processing.
     *
     * Following the redirect, the income and expense tables will be reloaded,
     * reflecting the deleted item.
     *
     * @param id received as a path variable with the HTTP GET request.
     * @param model provided by the Spring context.
     * @param authentication injected by the Spring context.
     * @return redirects to the income-and-expense endpoint handler.
     */
    @GetMapping("/income-and-expenses/monthly-income/{incomeId}/delete")
    public String deleteMonthlyIncome(@PathVariable("incomeId") String id, Model model,
                                      Authentication authentication) {
        // delete the MonthlyIncome
        incomeExpenseService.deleteMonthlyIncomeFor(Long.parseLong(id));
        return "redirect:/income-and-expenses";
    }

    /**
     * Handler for delete weekly income requests.
     *
     * The weekly income "id" for the income that is to be deleted, is sent as a
     * path variable with the HTTP GET request.
     *
     * The {@code IncomeExpenseService} instance handles the actual deletion, with the
     * application flow redirected to the income-and-expense endpoint on successful
     * processing.
     *
     * Following the redirect, the income and expense tables will be reloaded,
     * reflecting the deleted item.
     *
     * @param id received as a path variable with the HTTP GET request.
     * @param model provided by the Spring context.
     * @param authentication injected by the Spring context.
     * @return redirects to the income-and-expense endpoint handler.
     */
    @GetMapping("/income-and-expenses/weekly-income/{incomeId}/delete")
    public String deleteWeeklyIncome(@PathVariable("incomeId") String id, Model model,
                                      Authentication authentication) {
        // delete the Income
        incomeExpenseService.deleteWeeklyIncomeFor(Long.parseLong(id));
        return "redirect:/income-and-expenses";
    }

}
