package com.sadatmalik.fusionweb.mappers;

import com.sadatmalik.fusionweb.model.Income;
import com.sadatmalik.fusionweb.model.MonthlyExpense;
import com.sadatmalik.fusionweb.model.MonthlyIncome;
import com.sadatmalik.fusionweb.model.WeeklyExpense;
import com.sadatmalik.fusionweb.model.dto.MonthlyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.MonthlyIncomeDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyExpenseDto;
import com.sadatmalik.fusionweb.model.dto.WeeklyIncomeDto;

/**
 * A mapper class for mapping data entities to data transfer objects. The idea
 * is that only DTO objects will be passed in the MVC model.
 *
 * A later revision may leverage the MapStruct libraries.
 *
 * @author sadatmalik
 */
public class IncomeExpenseMapper {

    /**
     * Maps the {@code MonthlyExpense} argument to a {@code MonthlyExpenseDto}
     * object and returns it.
     *
     * @param monthlyExpense monthly expense data entity
     * @return returns monthly expense data transfer object
     */
    public static MonthlyExpenseDto monthlyExpenseToMonthlyExpenseDto(MonthlyExpense monthlyExpense) {
        return MonthlyExpenseDto.builder()
                .id(monthlyExpense.getId())
                .name(monthlyExpense.getName())
                .amount(monthlyExpense.getAmount().doubleValue())
                .dayOfMonthPaid(monthlyExpense.getDayOfMonthPaid())
                .type(monthlyExpense.getType())
                .accountId(monthlyExpense.getAccount().getAccountId())
                .build();
    }

    /**
     * Maps the {@code WeeklyExpense} argument to a {@code WeeklyExpenseDto} object
     * and returns it.
     *
     * @param weeklyExpense weekly expense data entity
     * @return returns weekly expense data transfer object
     */
    public static WeeklyExpenseDto weeklyExpenseToWeeklyExpenseDto(WeeklyExpense weeklyExpense) {
        return WeeklyExpenseDto.builder()
                .id(weeklyExpense.getId())
                .name(weeklyExpense.getName())
                .amount(weeklyExpense.getAmount().doubleValue())
                .timesPerWeek(weeklyExpense.getTimesPerWeek())
                .weeklyInterval(weeklyExpense.getWeeklyInterval())
                .type(weeklyExpense.getType())
                .accountId(weeklyExpense.getAccount().getAccountId())
                .build();
    }

    /**
     * Maps the {@code MonthlyIncome} argument to a {@code MonthlyIncomeDto}
     * object and returns it.
     *
     * @param monthlyIncome monthly income data entity
     * @return returns monthly income data transfer object
     */

    public static MonthlyIncomeDto monthlyIncomeToMonthlyIncomeDto(MonthlyIncome monthlyIncome) {
        return MonthlyIncomeDto.builder()
                .id(monthlyIncome.getId())
                .source(monthlyIncome.getSource())
                .amount(monthlyIncome.getAmount().doubleValue())
                .dayOfMonthReceived(monthlyIncome.getDayOfMonthReceived())
                .accountId(monthlyIncome.getAccount().getAccountId())
                .build();
    }

    /**
     * Maps the {@code Income} argument to a {@code WeeklyIncomeDto} object and
     * returns it.
     *
     * @param weeklyIncome weekly income data entity
     * @return returns weekly income data transfer object
     */
    public static WeeklyIncomeDto incomeToWeeklyIncomeDto(Income weeklyIncome) {
        return WeeklyIncomeDto.builder()
                .id(weeklyIncome.getId())
                .source(weeklyIncome.getSource())
                .amount(weeklyIncome.getAmount().doubleValue())
                .weeklyInterval(weeklyIncome.getWeeklyInterval())
                .accountId(weeklyIncome.getAccount().getAccountId())
                .build();
    }
}
