import java.util.Objects;

class GameState {
    private final int pcHp;
    private final int pcMana;
    private final int enemyHp;
    private final int enemyDamage;
    private final int fireballCooldown;

    public GameState(int pcHp, int pcMana, int enemyHp, int enemyDamage, int fireballCooldown) {
        this.pcHp = pcHp;
        this.pcMana = pcMana;
        this.enemyHp = enemyHp;
        this.enemyDamage = enemyDamage;
        this.fireballCooldown = fireballCooldown;
    }

    // Getters
    public int getPcHp() { return pcHp; }
    public int getPcMana() { return pcMana; }
    public int getEnemyHp() { return enemyHp; }
    public int getEnemyDamage() { return enemyDamage; }
    public int getFireballCooldown() { return fireballCooldown; }

    public boolean canUseFireball() {
        return pcMana >= 20 && fireballCooldown == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return pcHp == gameState.pcHp &&
                pcMana == gameState.pcMana &&
                enemyHp == gameState.enemyHp &&
                enemyDamage == gameState.enemyDamage &&
                fireballCooldown == gameState.fireballCooldown;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pcHp, pcMana, enemyHp, enemyDamage, fireballCooldown);
    }

    @Override
    public String toString() {
        return String.format("PC(HP:%d, Mana:%d) Enemy(HP:%d, Dmg:%d) Cooldown:%d", pcHp, pcMana, enemyHp, enemyDamage, fireballCooldown);
    }
}