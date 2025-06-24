import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ExperimentRunner {
    // Change this value to 1 to run a single simulation per test case
    private static final int NUM_RUNS = 1000;

    // Helper record to define test cases cleanly
    private record TestCase(String name, GameState state) {}

    public static void main(String[] args) {
        List<String> resultsLog = new ArrayList<>();
        resultsLog.add("TestCase,AIType,Outcome/WinRate,AvgTurns,AvgDecisionTime_ns");

        // --- Define All Test Cases ---
        List<TestCase> testCases = List.of(
                new TestCase("1. Baseline", new GameState(100, 50, 150, 12, 0)),
                new TestCase("2. Resource Scarcity", new GameState(100, 15, 150, 12, 0)),
                new TestCase("3. Race Against Time", new GameState(70, 50, 150, 12, 0)),
                new TestCase("4. Durable Enemy", new GameState(100, 50, 120, 8, 0)),
                new TestCase("5. Glass Cannon Enemy", new GameState(100, 50, 70, 25, 0)),
                new TestCase("6. Strategic Cooldown", new GameState(100, 50, 150, 12, 1)),
                new TestCase("7. Mana Trap", new GameState(100, 19, 150, 12, 0)),
                new TestCase("8. Perfect Lethal", new GameState(100, 40, 70, 12, 0))
        );

        AILogic greedyAI = new GreedyAI();
        AILogic dpAI = new DynamicProgrammingAI();
        List<AILogic> aisToTest = List.of(greedyAI, dpAI);

        if (NUM_RUNS > 1) {
            System.out.println("Running " + NUM_RUNS + " simulations for each AI on " + testCases.size() + " test cases...");
        } else {
            System.out.println("Running a single simulation for each AI on " + testCases.size() + " test cases...");
        }
        System.out.println("----------------------------------------------------------");

        for (TestCase testCase : testCases) {
            System.out.println("RUNNING TEST CASE: " + testCase.name());
            for (AILogic ai : aisToTest) {
                runAndLogTestCase(testCase, ai, resultsLog);
            }
            System.out.println("----------------------------------------------------------");
        }

        writeResultsToFile(resultsLog);
        System.out.println("Experiments finished. Results saved to results.csv");
    }

    private static void runAndLogTestCase(TestCase testCase, AILogic ai, List<String> log) {
        BattleSimulator simulator = new BattleSimulator();
        int wins = 0;
        long totalTurns = 0;
        long totalAvgDecisionTime = 0;
        String finalOutcome = "Loss";

        // DP AI is stateful, so it needs a reset before each test case run.
        ai.reset();

        System.out.print("  Testing " + ai.getAIType() + " AI... ");

        for (int i = 0; i < NUM_RUNS; i++) {
            BattleSimulator.BattleResult result = simulator.runBattle(ai, testCase.state());
            if ("Win".equals(result.outcome)) {
                wins++;
            }
            totalTurns += result.totalTurns;
            totalAvgDecisionTime += result.averageDecisionTimeNs;
            if (i == 0) finalOutcome = result.outcome; // Store result of the first run
        }
        System.out.println("Done.");

        double avgTurns = (double) totalTurns / NUM_RUNS;
        long avgTime = totalAvgDecisionTime / NUM_RUNS;

        if (NUM_RUNS > 1) {
            double winRate = (double) wins / NUM_RUNS * 100.0;
            System.out.printf("    > Result: Win Rate=%.1f%%, Avg Turns=%.2f, Avg Decision Time=%d ns%n",
                    winRate, avgTurns, avgTime);
            log.add(String.format("%s,%s,%.1f%%,%.2f,%d",
                    testCase.name(), ai.getAIType(), winRate, avgTurns, avgTime));
        } else {
            System.out.printf("    > Result: Outcome=%s, Turns=%d, Decision Time=%d ns%n",
                    finalOutcome, totalTurns, avgTime);
            log.add(String.format("%s,%s,%s,%d,%d",
                    testCase.name(), ai.getAIType(), finalOutcome, totalTurns, avgTime));
        }
    }

    private static void writeResultsToFile(List<String> log) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv"))) {
            for (String line : log) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing results to file: " + e.getMessage());
        }
    }
}