import java.util.ArrayList;
import java.util.List;

public class SeqExp extends Exp {
    List<Exp> exps;

    public SeqExp(List<Exp> exps) {
        this.exps = exps;
    }

    @Override
    public String show() {
        List<String> results = new ArrayList<>();
        for (var exp : exps) {
            results.add(exp.toString());
        }
        return String.join("; ", results);
    }
}
