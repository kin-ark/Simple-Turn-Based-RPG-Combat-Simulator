import pandas as pd
import matplotlib.pyplot as plt

file_path = "results.csv"
data = pd.read_csv(file_path)

data["AvgDecisionTime_ms"] = data["AvgDecisionTime_ns"] / 1e6

pivot_data = data.pivot(index="TestCase", columns="AIType", values="AvgDecisionTime_ms")

fig, ax = plt.subplots(figsize=(12, 6))
pivot_data.plot(kind="bar", logy=True, ax=ax)

ax.set_title("Comparison of Decision Times (ms) on a Logarithmic Scale")
ax.set_ylabel("Decision Time (ms)")
ax.set_xlabel("Test Case")
ax.legend(title="AI Type")
plt.xticks(rotation=45, ha="right")
plt.tight_layout()

plt.show()
