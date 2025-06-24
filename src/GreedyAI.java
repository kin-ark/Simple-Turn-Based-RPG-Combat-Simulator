class GreedyAI implements AILogic {

    @Override
    public String getAIType() {
        return "Greedy";
    }

    @Override
    public void reset() {
    }

    @Override
    public Action chooseMove(GameState state) {
        Action bestMove = null;
        int maxScore = -1;

        // Evaluate Fireball
        if (state.canUseFireball()) {
            if (35 > maxScore) {
                maxScore = 35;
                bestMove = Action.FIREBALL;
            }
        }

        // Evaluate Basic Attack
        if (10 > maxScore) {
            maxScore = 10;
            bestMove = Action.BASIC_ATTACK;
        }

        // Evaluate Meditate
        if (1 > maxScore) {
            maxScore = 1;
            bestMove = Action.MEDITATE;
        }

        return bestMove;
    }
}
