import java.util.ArrayList;
import java.util.List;

class BattleSimulator {
    public static class BattleResult {
        public final String outcome;
        public final int totalTurns;
        public final long averageDecisionTimeNs;
        BattleResult(String outcome, int totalTurns, long averageDecisionTimeNs) {
            this.outcome = outcome;
            this.totalTurns = totalTurns;
            this.averageDecisionTimeNs = averageDecisionTimeNs;
        }
    }

    public BattleResult runBattle(AILogic ai, GameState initialState) {
        GameState currentState = initialState;
        int turns = 0;
        List<Long> decisionTimes = new ArrayList<>();

        while (currentState.getPcHp() > 0 && currentState.getEnemyHp() > 0) {
            turns++;
            long startTime = System.nanoTime();
            Action pcAction = ai.chooseMove(currentState);
            long endTime = System.nanoTime();
            decisionTimes.add(endTime - startTime);

            // *** FIX FOR NULL POINTER EXCEPTION ***
            // If AI returns null, it means it cannot find a winning path and concedes.
            if (pcAction == null) {
                break;
            }

            currentState = applyPcMove(currentState, pcAction);

            if (currentState.getEnemyHp() <= 0) break;

            currentState = applyEnemyMove(currentState);
        }

        String result = (currentState.getEnemyHp() <= 0) ? "Win" : "Loss";
        long avgTime = (long) decisionTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        return new BattleResult(result, turns, avgTime);
    }

    private GameState applyPcMove(GameState state, Action action) {
        int hp = state.getPcHp();
        int mana = state.getPcMana();
        int enemyHp = state.getEnemyHp();
        int cooldown = Math.max(0, state.getFireballCooldown() - 1);

        switch (action) {
            case BASIC_ATTACK: enemyHp -= 10; break;
            case FIREBALL:
                mana -= 20;
                enemyHp -= 35;
                cooldown = 2;
                break;
            case MEDITATE: mana += 15; break;
        }
        return new GameState(hp, mana, enemyHp, state.getEnemyDamage(), cooldown);
    }

    private GameState applyEnemyMove(GameState state) {
        int hp = state.getPcHp() - state.getEnemyDamage();
        return new GameState(hp, state.getPcMana(), state.getEnemyHp(), state.getEnemyDamage(), state.getFireballCooldown());
    }
}
