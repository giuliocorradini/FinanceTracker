package financetracker.gui.model;

/**
 * A simple immutable class to wrap summaries. Used by {@link BalanceModel#getBalanceSummary()}.
 * @param income sum of positive amounts.
 * @param outcome sum of negative amounts.
 * @param flow sum of all amounts.
 */
public record Summary(double income, double outcome, double flow) {
}
