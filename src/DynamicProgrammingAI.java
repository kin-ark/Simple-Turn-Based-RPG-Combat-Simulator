import java.util.HashMap;
import java.util.Map;

class DynamicProgrammingAI implements AILogic {

    private static class Outcome {
        Action move;
        int turnsToWin;
        Outcome(Action move, int turns) {
            this.move = move;
            this.turnsToWin = turns;
        }
    }

    private Map<GameState, Outcome> memoTable;

    public DynamicProgrammingAI() {
        this.memoTable = new HashMap<>();
    }

    @Override
    public String getAIType() {
        return "DynamicProgramming";
    }

    @Override
    public void reset() {
        memoTable.clear();
    }

    @Override
    public Action chooseMove(GameState state) {
        Outcome optimalOutcome = findOptimalOutcome(state);
        return optimalOutcome.move;
    }

    private Outcome findOptimalOutcome(GameState state) {
        if (state.getEnemyHp() <= 0) return new Outcome(null, 0);
        if (state.getPcHp() <= 0) return new Outcome(null, 999);
        if (memoTable.containsKey(state)) return memoTable.get(state);

        Outcome bestOutcome = new Outcome(null, 999);

        // Recursive Exploration for each valid move
        if (state.canUseFireball()) {
            evaluateMove(state, Action.FIREBALL, bestOutcome);
        }
        evaluateMove(state, Action.BASIC_ATTACK, bestOutcome);
        evaluateMove(state, Action.MEDITATE, bestOutcome);

        memoTable.put(state, bestOutcome);
        return bestOutcome;
    }

    private void evaluateMove(GameState state, Action action, Outcome bestOutcome) {
        GameState nextState = applyMove(state, action);
        Outcome futureOutcome = findOptimalOutcome(nextState);

        int currentPathTurns = 1 + futureOutcome.turnsToWin;
        if (currentPathTurns < bestOutcome.turnsToWin) {
            bestOutcome.move = action;
            bestOutcome.turnsToWin = currentPathTurns;
        }
    }

    private GameState applyMove(GameState currentState, Action pcAction) {
        int nextPcHp = currentState.getPcHp();
        int nextPcMana = currentState.getPcMana();
        int nextEnemyHp = currentState.getEnemyHp();
        int nextFireballCooldown = Math.max(0, currentState.getFireballCooldown() - 1);

        switch (pcAction) {
            case BASIC_ATTACK: nextEnemyHp -= 10; break;
            case FIREBALL:
                nextEnemyHp -= 35;
                nextPcMana -= 20;
                nextFireballCooldown = 2;
                break;
            case MEDITATE: nextPcMana += 15; break;
        }

        if (nextEnemyHp <= 0) {
            return new GameState(nextPcHp, nextPcMana, nextEnemyHp, currentState.getEnemyDamage(), nextFireballCooldown);
        }

        nextPcHp -= currentState.getEnemyDamage();
        return new GameState(nextPcHp, nextPcMana, nextEnemyHp, currentState.getEnemyDamage(), nextFireballCooldown);
    }
}

