package splitwiseWithBuilder;

import java.util.HashMap;
import java.util.List;

public interface ISplitStrategy {
    List<Split> calculateSplits(Double amount, HashMap<String, Double> payerToAmountMap, List<String> participants);
}
