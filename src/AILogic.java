interface AILogic {
    Action chooseMove(GameState state);
    String getAIType();
    void reset(); // Added to reset stateful AIs like DP
}