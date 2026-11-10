# Security Policy

## 🛡️ Commitment to Security

At **ExpenSync**, the confidentiality, integrity, and privacy of users' shared financial data is our highest priority. This document outlines our security practices, supported versions, and procedures for responsibly reporting vulnerabilities.

---

## 📌 Supported Versions

We actively maintain and provide security patches for the following versions:

| Version | Supported | Status |
| :--- | :---: | :--- |
| `1.0.x` | ✅ | Current production release / Actively maintained |
| `< 1.0` | ❌ | Development versions / Unsupported |

---

## 🚨 Reporting a Vulnerability

If you discover a security vulnerability in ExpenSync, please follow **Responsible Disclosure** guidelines:

1. **Private Disclosure**: Do not create public issues, pull requests, or discuss vulnerabilities publicly on social media or forums.
2. **Contact Email**: Send a detailed report directly to:
   - **Security Contact**: `jastinbolanos.tech@gmail.com`
   - **Subject Line**: `[SECURITY VULNERABILITY] ExpenSync - <Short Summary>`
3. **What to Include**:
   - Description and estimated severity of the issue (CVSS if applicable).
   - Detailed step-by-step reproduction instructions or a minimal Proof of Concept (PoC).
   - Affected components (`:data`, `:domain`, `:features`, dependencies, or configuration).
   - Test environment details (Android OS version, device/emulator model, app version).
4. **Response Times**:
   - Initial acknowledgment: **Within 48 business hours**.
   - Assessment and triage: **Within 3 to 5 business days**.
   - Remediation and patch release: Handled with critical priority based on severity.

We sincerely appreciate and publicly credit ethical security researchers who help protect our community.

---

## 🔐 Implemented Security Measures

### 1. Secrets & Credentials Management
- **Public vs. Private Keys**: The mobile client only uses public/anonymous client keys (`anon` / `publishable`) for Supabase, strictly restricted by backend database access policies (**Row Level Security - RLS**).
- **No Administrative Keys in Binaries**: Administrative credentials (`service_role` or master tokens) are never included in compiled APKs or AABs.
- **Environment Isolation**: Local secrets and configurations are managed through `.env`, which is strictly excluded from version control (`.gitignore`) to prevent accidental leaks.

### 2. Network Security
- **Enforced Encryption**: All HTTP requests and real-time database subscriptions with Supabase are transmitted strictly over **HTTPS / TLS 1.3**, protecting users against Man-in-the-Middle (MitM) attacks.
- **Ktor CIO Engine**: Validates platform SSL certificates and rejects unencrypted connections or untrusted certificate authorities.
- **No Plaintext Transmission**: Sensitive transaction records and financial balances are never transmitted across insecure protocols.

### 3. Local On-Device Persistence
- **Android Application Sandbox**: The local SQLite database managed by Room resides exclusively inside the application's private internal storage (`/data/data/com.jastin.expensync/databases/`).
- **File System Permissions**: On non-rooted devices, no other third-party application has read or write access to ExpenSync's internal cache.
- **Backup Exclusion Directives**: Explicit exclusion rules (`backup_rules.xml` and `data_extraction_rules.xml`) prevent sensitive cache or credential data from being exposed in unencrypted cloud backups.

### 4. Deterministic Financial Logic
- **Financial Calculation Integrity**: The `CalculateDebtsUseCase` enforces rounding to two decimal places and epsilon tolerance checking to eliminate floating-point precision discrepancies.
- **Input Sanitization**: Expense titles, non-negative amounts, and participant arrays are validated before persisting to avoid state corruption.

---

## 📋 Best Practices for Contributors

If you are contributing code to ExpenSync:
- Always run dependency vulnerability checks to prevent introducing known CVEs.
- Never log personal financial amounts, tokens, or identifiers to production `Logcat`.
- Adhere strictly to the contracts defined in the `:domain` module for all financial operations.
- Ensure all unit tests pass before submitting a Pull Request.

---

*Last updated: November 2026*