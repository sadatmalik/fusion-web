package com.sadatmalik.fusionweb.services;

import com.sadatmalik.fusionweb.exceptions.NoSuchIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyExpenseException;
import com.sadatmalik.fusionweb.exceptions.NoSuchMonthlyIncomeException;
import com.sadatmalik.fusionweb.exceptions.NoSuchWeeklyExpenseException;
import com.sadatmalik.fusionweb.model.*;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;
import com.sadatmalik.fusionweb.repositories.IncomeRepository;
import com.sadatmalik.fusionweb.repositories.MonthlyExpenseRepository;
import com.sadatmalik.fusionweb.repositories.MonthlyIncomeRepository;
import com.sadatmalik.fusionweb.repositories.WeeklyExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IncomeExpenseService {

    private final MonthlyExpenseRepository monthlyExpenseRepository;
    private final WeeklyExpenseRepository weeklyExpenseRepository;
    private final MonthlyIncomeRepository monthlyIncomeRepository;
    private final IncomeRepository incomeRepository;

    private final AccountService accountService;

    public MonthlyExpense getMonthlyExpenseFor(Long id) {
        return monthlyExpenseRepository.findById(id).orElse(null);
    }

    public List<MonthlyExpense> getMonthlyExpensesFor(User user) {
        return monthlyExpenseRepository.findByUser(user);
    }

    public WeeklyExpense getWeeklyExpenseFor(Long id) {
        return weeklyExpenseRepository.findById(id).orElse(null);
    }

    public List<WeeklyExpense> getWeeklyExpensesFor(User user) {
        return weeklyExpenseRepository.findByUser(user);
    }

    public MonthlyIncome getMonthlyIncomeFor(Long id) {
        return monthlyIncomeRepository.findById(id).orElse(null);
    }

    public List<MonthlyIncome> getMonthlyIncomeFor(User user) {
        return monthlyIncomeRepository.findByUser(user);
    }

    public Income getWeeklyIncomeFor(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public List<Income> getWeeklyIncomeFor(User user) {
        return incomeRepository.findByUser(user);
    }

    public MonthlyExpense saveMonthlyExpense(MonthlyExpenseDto monthlyExpenseDto, User user) {
        // @todo use MapStruct
        MonthlyExpense monthlyExpense = MonthlyExpense.builder()
                .name(monthlyExpenseDto.getName())
                .amount(new BigDecimal(monthlyExpenseDto.getAmount()))
                .dayOfMonthPaid(monthlyExpenseDto.getDayOfMonthPaid())
                .type(monthlyExpenseDto.getType())
                .account(accountService.getAccountById(
                        monthlyExpenseDto.getAccountId())
                )
                .user(user)
                .build();

        return monthlyExpenseRepository.save(monthlyExpense);
    }

    public WeeklyExpense saveWeeklyExpense(WeeklyExpenseDto weeklyExpenseDto, User user) {
        // @todo use MapStruct
        WeeklyExpense weeklyExpense = WeeklyExpense.builder()
                .name(weeklyExpenseDto.getName())
                .amount(new BigDecimal(weeklyExpenseDto.getAmount()))
                .timesPerWeek(weeklyExpenseDto.getTimesPerWeek())
                .type(weeklyExpenseDto.getType())
                .account(accountService.getAccountById(
                        weeklyExpenseDto.getAccountId())
                )
                .user(user)
                .build();

        return weeklyExpenseRepository.save(weeklyExpense);
    }

    public MonthlyIncome saveMonthlyIncome(MonthlyIncomeDto monthlyIncomeDto, User user) {
        // @todo use MapStruct
        MonthlyIncome monthlyIncome = MonthlyIncome.builder()
                .amount(new BigDecimal(monthlyIncomeDto.getAmount()))
                .source(monthlyIncomeDto.getSource())
                .dayOfMonthReceived(monthlyIncomeDto.getDayOfMonthReceived())
                .account(accountService.getAccountById(
                        monthlyIncomeDto.getAccountId())
                )
                .user(user)
                .build();

        return monthlyIncomeRepository.save(monthlyIncome);
    }

    public Income saveWeeklyIncome(WeeklyIncomeDto weeklyIncomeDto, User user) {
        // @todo use MapStruct
        Income weeklyIncome = Income.builder()
                .amount(new BigDecimal(weeklyIncomeDto.getAmount()))
                .source(weeklyIncomeDto.getSource())
                .weeklyInterval(weeklyIncomeDto.getWeeklyInterval())
                .account(accountService.getAccountById(
                        weeklyIncomeDto.getAccountId())
                )
                .user(user)
                .build();

        return incomeRepository.save(weeklyIncome);
    }

    public MonthlyExpense updateMonthlyExpense(MonthlyExpenseDto monthlyExpenseDto)
            throws NoSuchMonthlyExpenseException{
        // get current
        MonthlyExpense monthlyExpense =
                monthlyExpenseRepository.findById(monthlyExpenseDto.getId()).orElse(null);
        if (monthlyExpense != null) {
            monthlyExpense.setName(monthlyExpenseDto.getName());
            monthlyExpense.setAmount(new BigDecimal(monthlyExpenseDto.getAmount()));
            monthlyExpense.setDayOfMonthPaid(monthlyExpenseDto.getDayOfMonthPaid());
            monthlyExpense.setType(monthlyExpenseDto.getType());
            monthlyExpense.setAccount(accountService.getAccountById(
                    monthlyExpenseDto.getAccountId()
            ));
            return monthlyExpenseRepository.save(monthlyExpense);

        } else {
            log.error("Trying to update non-existing monthly expense - update attempted for monthly expense ID "
                    + monthlyExpenseDto.getId());
            throw new NoSuchMonthlyExpenseException("Trying to update non-existing monthly expense - " +
                    "update attempted for monthly expense ID " + monthlyExpenseDto.getId());
        }
    }

    public WeeklyExpense updateWeeklyExpense(WeeklyExpenseDto weeklyExpenseDto)
            throws NoSuchWeeklyExpenseException {
        // get current
        WeeklyExpense weeklyExpense =
                weeklyExpenseRepository.findById(weeklyExpenseDto.getId()).orElse(null);
        if (weeklyExpense != null) {
            weeklyExpense.setName(weeklyExpenseDto.getName());
            weeklyExpense.setAmount(new BigDecimal(weeklyExpenseDto.getAmount()));
            weeklyExpense.setTimesPerWeek(weeklyExpenseDto.getTimesPerWeek());
            weeklyExpense.setWeeklyInterval(weeklyExpenseDto.getWeeklyInterval());
            weeklyExpense.setType(weeklyExpenseDto.getType());
            weeklyExpense.setAccount(accountService.getAccountById(
                    weeklyExpenseDto.getAccountId()
            ));
            return weeklyExpenseRepository.save(weeklyExpense);

        } else {
            log.error("Trying to update non-existing weekly expense - update attempted for weekly expense ID "
                    + weeklyExpenseDto.getId());
            throw new NoSuchWeeklyExpenseException("Trying to update non-existing weekly expense - " +
                    "update attempted for weekly expense ID " + weeklyExpenseDto.getId());
        }
    }

    public MonthlyIncome updateMonthlyIncome(MonthlyIncomeDto monthlyIncomeDto)
            throws NoSuchMonthlyIncomeException {
        // get current
        MonthlyIncome monthlyIncome =
                monthlyIncomeRepository.findById(monthlyIncomeDto.getId()).orElse(null);
        if (monthlyIncome != null) {
            monthlyIncome.setAmount(new BigDecimal(monthlyIncomeDto.getAmount()));
            monthlyIncome.setSource(monthlyIncomeDto.getSource());
            monthlyIncome.setDayOfMonthReceived(monthlyIncomeDto.getDayOfMonthReceived());
            monthlyIncome.setAccount(accountService.getAccountById(
                    monthlyIncomeDto.getAccountId()
            ));
            return monthlyIncomeRepository.save(monthlyIncome);

        } else {
            log.error("Trying to update non-existing monthly income - update attempted for monthly income ID "
                    + monthlyIncomeDto.getId());
            throw new NoSuchMonthlyIncomeException("Trying to update non-existing monthly income" +
                    " - update attempted for monthly income ID " + monthlyIncomeDto.getId());
        }
    }

    public Income updateWeeklyIncome(WeeklyIncomeDto weeklyIncomeDto)
            throws NoSuchIncomeException {
        // get current
        Income weeklyIncome =
                incomeRepository.findById(weeklyIncomeDto.getId()).orElse(null);
        if (weeklyIncome != null) {
            weeklyIncome.setAmount(new BigDecimal(weeklyIncomeDto.getAmount()));
            weeklyIncome.setSource(weeklyIncomeDto.getSource());
            weeklyIncome.setWeeklyInterval(weeklyIncomeDto.getWeeklyInterval());
            weeklyIncome.setAccount(accountService.getAccountById(
                    weeklyIncomeDto.getAccountId()
            ));
            return incomeRepository.save(weeklyIncome);

        } else {
            log.error("Trying to update non-existing weekly income - update attempted for income ID "
                    + weeklyIncomeDto.getId());
            throw new NoSuchIncomeException("Trying to update non-existing weekly income" +
                    " - update attempted for weekly income ID " + weeklyIncomeDto.getId());
        }
    }

    public void deleteMonthlyExpenseFor(Long expenseId) {
        MonthlyExpense toDelete = monthlyExpenseRepository.findById(expenseId).orElse(null);
        if (toDelete != null) {
            monthlyExpenseRepository.deleteById(expenseId);
        } else {
            log.debug("Trying to delete non-existing monthly expense - no such monthly expense with ID - " + expenseId);
        }
        log.debug("Deleted - " + toDelete);
    }

    public void deleteWeeklyExpenseFor(Long expenseId) {
        WeeklyExpense toDelete = weeklyExpenseRepository.findById(expenseId).orElse(null);
        if (toDelete != null) {
            weeklyExpenseRepository.deleteById(expenseId);
        } else {
            log.debug("Trying to delete non-existing weekly expense - no such weekly expense with ID - " + expenseId);
        }
        log.debug("Deleted - " + toDelete);
    }

    public void deleteMonthlyIncomeFor(Long incomeId) {
        MonthlyIncome deletable = monthlyIncomeRepository.findById(incomeId).orElse(null);
        if (deletable != null) {
            monthlyIncomeRepository.deleteById(incomeId);
        } else {
            log.debug("Trying to delete non-existing monthly income - no such monthly income with ID - " + incomeId);
        }
        log.debug("Deleted - " + deletable);
    }

    public void deleteWeeklyIncomeFor(Long incomeId) {
        Income deletable = incomeRepository.findById(incomeId).orElse(null);
        if (deletable != null) {
            incomeRepository.deleteById(incomeId);
        } else {
            log.debug("Trying to delete non-existing weekly income - no such weekly income with ID - " + incomeId);
        }
        log.debug("Deleted - " + deletable);
    }

}
