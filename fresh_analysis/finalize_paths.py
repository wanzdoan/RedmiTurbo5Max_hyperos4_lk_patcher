from pathlib import Path
p=Path('fresh_analysis/build_candidate.py');s=p.read_text(encoding='utf-8-sig').replace("EXPERIMENTAL_NOT_HARDWARE_VERIFIED","REJECTED_BY_H4_BL2_HASH_SELECTION").replace("fresh_analysis/output/lk_hyperos4_EXPERIMENTAL.img","fresh_analysis/rejected/lk_h4_main_only_REJECTED.img");p.write_text(s,encoding='utf-8')
p=Path('fresh_analysis/validate_certificates.py');s=p.read_text(encoding='utf-8-sig').replace("fresh_analysis/output/lk_hyperos4_EXPERIMENTAL.img","fresh_analysis/rejected/lk_h4_main_only_REJECTED.img");p.write_text(s,encoding='utf-8')
