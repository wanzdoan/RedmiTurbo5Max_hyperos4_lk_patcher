# Redmi Turbo 5 Max — LK HyperOS 4 hybrid

Người dùng đã xác nhận **boot thành công**, fastboot **unlocked: yes** trên ROM **OS4.0.3.0.XPLCNXM** với preloader ENG H3. Sau đó người dùng báo đã test việc thay preloader stock H4 thành công và yêu cầu dừng kiểm tra, cập nhật docs.

Bản đã chạy được tạo bằng **hai thay đổi phối hợp**: patch getter unlock trong main LK H4, đồng thời ghép bộ BL2_EXT/certificate H3 còn nguyên chữ ký vào container. AEE và DTB/DTBO giữ nguyên H4. Đây là bản hybrid, không phải toàn bộ bootloader thuần H4 được patch.

- [Chỉ mục tài liệu đầy đủ](docs/README.md).
- [Logic reverse và patch](docs/01-logic-and-patch.md).
- [Input, layout và nguồn thành phần](docs/02-inputs-and-layout.md).
- [Build và kiểm chứng](docs/03-build-and-validation.md).
- [Biên bản máy thật và preloader](docs/04-runtime-and-preloader.md).
- [GitHub, phiên bản và quay lại bản cũ](docs/05-versioning-and-rollback.md).
- [Chỉ mục evidence và giới hạn](docs/06-evidence-and-limits.md).
- [Changelog](docs/CHANGELOG.md).

File đã bàn giao: [lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img](fresh_analysis/output/lk_hyperos4_BL33_H3BL2_ENG_EXPERIMENTAL.img), **4.083.424 byte**.

SHA256: `f57d19c928b417ab082f06612038587fffadf693700e535eaa08542fa878c171`.

Giữ nguyên tên và bytes sau xác nhận boot. Tên `EXPERIMENTAL` là tên artifact ban đầu; biên bản runtime riêng ghi kết quả mới. `fresh_analysis/rejected` chỉ chứa đối chứng bị loại. Các thư mục source/reports/ghidra và bản patched cũ ngoài `fresh_analysis` được lưu để bảo tồn lịch sử, không phải quy trình tạo bản mới.

Nghiên cứu tiếp theo: [5G/NR và biên bản hoàn nguyên](docs/07-nr5g-investigation.md). Các thử nghiệm SA/NSA đã hoàn nguyên theo yêu cầu sau hai lần treo màn hình, nguyên nhân chưa xác định. Chưa có bản patch 5G hoạt động được xác nhận.

Bộ thử tách riêng: [radio region global v1](experiments/radio_region_global_v1/README_VI.md), gồm LK đã boot giữ nguyên byte và module đổi một thuộc tính Android `ro.vendor.radio.build_region=global`. Chưa cài hoặc kiểm thử NR trên máy thật; không phải LK có thêm patch 5G.

Theo yêu cầu tiếp theo chỉ sửa LK: [bản thử bỏ chốt ROM CN/Global](experiments/lk_global_region_v1/README_VI.md) đã qua mô phỏng logic vùng, giữ unlock cũ, chưa flash hoặc boot test. [Docs 08](docs/08-lk-rom-region.md) giải thích ba chốt đã sửa và bằng chứng mới: descriptor modem LK truyền xuống đã chứa bit hỗ trợ NR. Chưa chứng minh bản thử này mở được 5G.

Biến thể theo file H3 của người dùng: [H3 đã patch unlock + CN/Global](experiments/lk_h3_global_region_v1/README_VI.md), tạo trực tiếp từ `lk_hyperos3_patched_unlockBL/dash.img`, không ghép H4. Đã kiểm thử mô phỏng, chưa xác nhận boot bản mới.

Input mới ngày2026-11-04: [LK ENG H3 patch unlock riêng](experiments/lk_h3_eng_unlock_v1/README_VI.md), từ `Downloads/lk.img` SHA6f386241..., giữ toàn bộ thành phần ENG. [Docs09](docs/09-h3-eng-unlock.md) ghi logic và kiểm thử; chưa flash/boot test.

Nghiên cứu hiện tại: [khóa thật và OTA stock China](docs/10-stock-relock-and-ota.md). Đọc ADB/su xác nhận hai LK là token-key clone, kernel unlocked/orange khác props bị che; init_boot_a lệch hash ký, Secinfo fallback hợp lệ. Chưa có quy trình relock an toàn được xác nhận; chưa flash/lock hoặc ghi bảo mật. Raw riêng thiết bị giữ ngoài Git.

Cập nhật 2026-11-05: đã dừng nghiên cứu relock theo yêu cầu. Hai biến thể mới [ENG unlocked và fakelock v2 thêm HWC tự chọn vùng](experiments/lk_h3_eng_auto_hwc_v1/README_VI.md) giữ riêng tính năng từng bản; đã qua replay ARM64, chưa flash/boot test.

Bộ bàn giao theo xác nhận 2026-11-05: [6 file ENG all-region HWC](experiments/lk_h3_eng_mte_off_v1/README_VI.md), gồm unlocked/fakelock v2/stock-policy, mỗi bản giữ MTE gốc hoặc ép nhánh kernel MTE-off riêng. Đã qua replay, chưa flash/boot test. [Nghiên cứu MTE](fresh_analysis/mte_research/README_VI.md) phân biệt tắt phía kernel với hoạt động preloader đã chạy.

Theo yêu cầu mở lại nghiên cứu preloader ngày2026-11-05: [LK ENG và stock preloader H3/H4](docs/11-eng-preloader-compatibility.md). Đã kiểm đúng input, policy ARM64/DFD và chuỗi chứng chỉ; chưa chốt nguyên nhân brick H3 cũ + stock H4, chưa xác nhận sáu bản ENG boot với stock H3/H4. Không flash hoặc sửa firmware.

Phân tích file bên ngoài theo yêu cầu: [Fenrir so với fakelock v2](fresh_analysis/fenrir_review/README_VI.md). Nền H3 stock, fake locked/green ở nguồn chung và thêm gate AEE; native probes chỉ ra cả điểm mở rộng lẫn các kênh v2 bao phủ tốt hơn. Không flash, không đưa patch vào các bản hiện có.

Theo yêu cầu kết hợp Fenrir và v2: [hai hybrid mới trên H3 stock và H3 debug](experiments/lk_fenrir_v2_hybrid_v1/README_VI.md), mỗi nền một file, HWC tự chọn vùng và MTE gốc. Native init_boot hash/filter/caller và public status đã kiểm; chưa boot/root test. Hai ảnh có guard bỏ riêng call bảo trì phiên bản sau load để fake locked không kéo theo đường đó.

Yêu cầu mới nhất sau khi user báo các hybrid không dùng được: [Fenrir gốc chỉ thêm HWC auto và MTE-off](experiments/lk_fenrir_only_auto_hwc_mte_off_v1/README_VI.md), một file riêng SHA8d616abb..., không ghép v2/guard bảo trì. Chưa boot test; giữ nguyên các bản cũ làm lịch sử.

Cập nhật lỗi POCO H3: user báo bản8d616abb... chỉ về preloader. Bản đó chỉ đổi HWC, chưa mở các chốt CN/Global; không coi là all-region boot. [Audit và bản sửa bốn chốt CN/Global](experiments/lk_fenrir_cn_global_gates_v2/README_VI.md), SHAe6bd99c9..., chưa xác nhận boot hoặc nguyên nhân thực tế.

Theo yêu cầu quay lại đúng H4 đã boot: [biến thể H4 hybrid f57 chỉ thêm MTE-off](experiments/lk_h4_booted_hybrid_mte_off_v1/README_VI.md), SHA7873361e.... Chỉ một instruction H4 và main hash prefix thay đổi; nguồn đã boot giữ nguyên, bản mới chưa boot test.

Theo yêu cầu06-11: [ENG gốc stock-policy China, MTE gốc/off](experiments/lk_eng_stock_policy_cn_only_v1/README_VI.md). Giữ HWC/flash-region China gốc, chỉ thêm trust khóa China cạnh ENG; không thêm khóa Global, không ép unlock/fakelock. Hai file mới chưa boot test.

Theo đúng nhánh H3 được người dùng xác nhận trong ZIP sáu file: [H4 stock-policy + auto HWC, normal/MTE_OFF](experiments/lk_h4_eng_policy_auto_hwc_v1/README_VI.md). Main/AEE/DTB H4 và bộ nạp có chữ ký ENG H3; port khóa/vùng và một số gate main, không giả locked/green. Đã kiểm offline; chưa boot/lock hoặc chứng minh dùng preloader H4, không phải bản ENG H4 đầy đủ.

User đã thử H4 v1 MTE_OFF và báo vào recovery trên China/Global. [Ứng viên sửa v2](experiments/lk_h4_eng_policy_recovery_fix_v2/README_VI.md) bỏ riêng gate Secure ID sau AVB tại boot caller vốn không có trong ENG H3; giữ khóa thật/AVB/HWC/MTE. Đã tái hiện đường lỗi bằng native replay, chưa chứng minh nguyên nhân trên máy hoặc boot bản sửa.

V2 tiếp tục bị user báo recovery trên China H4 sạch, không root; ENG H3 vẫn vào Android. [Kiểm AVB native trên ROM thật](fresh_analysis/h4_recovery_v3_20261106/README_VI.md) qua ở cả hai main dưới fixtures, nên chưa xác định nguyên nhân. [V3 đối chứng BL2](experiments/lk_h4_policy_bl2_control_v3/README_VI.md) giữ main policy v2 nguyên byte, chỉ dùng đúng bộ nạp signed H3 thương mại của H4 f57 đã boot; normal/off riêng và một unlocked control tách trong diagnostic. Chưa có boot test v3.
