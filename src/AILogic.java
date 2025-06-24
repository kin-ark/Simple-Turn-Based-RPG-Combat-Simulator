interface AILogic {
    Action chooseMove(GameState state);
    String getAIType();
    void reset();
}