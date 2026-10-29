THIS REPOSITORY HIGHLIGHTS THE PROBLEM OF ADDRESS BOOK SYSTEM AND GIVES THE SOLUTION TO IT.



## 🌿 Branching Strategy & Repository Structure

| Branch | Content | Purpose |
| :--- | :--- | :--- |
| **`main`** | `README.md` only | High-level project documentation, architecture overview, and daily progress logs. No source code. |
| **`develop`** | Clean project skeleton (`src/main/java`, `src/test/java`, `pom.xml`) | The integration branch containing the baseline structure ready for use case implementations. |
| **`feature/*`** | Use case specific implementation & tests | Isolated branch per usecase (e.g., `feature/uc1-file-operations`). Merged into `develop` once completed and tested. |

---

## 🛠️ Development & Workflow Rules

1. **Feature Branch Creation**:
   Always branch off `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/<use-case-name>
   ```

2. **Testing & Verification**:
   Before merging, compile and run JUnit test cases:
   ```bash
   mvn test
   ```

3. **Merging into Develop**:
   Merge the tested feature branch into `develop`:
   ```bash
   git checkout develop
   git merge feature/<use-case-name>
   git push origin develop
   ```

---
