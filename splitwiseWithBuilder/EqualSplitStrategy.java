package splitwiseWithBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EqualSplitStrategy implements ISplitStrategy {
    private static EqualSplitStrategy instance = null;
    private EqualSplitStrategy() {}

    public static synchronized EqualSplitStrategy getInstance()
    {
        if(instance == null)
        {
            instance = new EqualSplitStrategy();
        }
        return instance;
    }

    public List<Split> calculateSplits(Double amount, HashMap<String, Double> payerToAmountMap, List<String> participants)
    {
        List<Split> splits = new ArrayList<>();
        Double equalSplit = -1.0*amount/participants.size();
        for(var participant: participants)
        {
            Double userSplit = equalSplit;
            if(payerToAmountMap.containsKey(participant))
            {
                userSplit += payerToAmountMap.get(participant);
            }

            splits.add(new Split(participant, userSplit));
        }
        return splits;
    }
}
