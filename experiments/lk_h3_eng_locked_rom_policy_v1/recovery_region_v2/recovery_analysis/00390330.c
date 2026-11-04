
/* copy_logs(bool) */

void copy_logs(bool param_1)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  long lVar9;
  char *pcVar10;
  ulong uVar11;
  string local_68;
  undefined7 uStack_67;
  char cStack_60;
  undefined7 uStack_5f;
  undefined4 uStack_58;
  uint uStack_54;
  ulong local_50;
  undefined8 uStack_48;
  char *local_40;
  long local_38;

  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  if (!param_1) goto LAB_00390760;
  lVar9 = Paths::Get();
  if ((*(byte *)(lVar9 + 0x90) & 1) == 0) {
    uStack_48 = *(undefined8 *)(lVar9 + 0x98);
    local_50 = *(ulong *)(lVar9 + 0x90);
    local_40 = *(char **)(lVar9 + 0xa0);
  }
  else {
    std::string::__init_copy_ctor_external
              ((string *)&local_50,*(char **)(lVar9 + 0xa0),*(ulong *)(lVar9 + 0x98));
  }
  pcVar10 = operator_new(0x20);
  pcVar10[0x18] = '\0';
  uVar8 = s__cache_recovery_last_log_0014aa51._16_8_;
  uVar7 = s__cache_recovery_last_log_0014aa51._8_8_;
  uVar4 = s__cache_recovery_last_log_0014aa51._0_8_;
  cStack_60 = 0x18;
  uStack_5f = 0;
  uStack_58 = SUB84(pcVar10,0);
  uStack_54 = (uint)((ulong)pcVar10 >> 0x20);
  local_68 = (string)0x21;
  uStack_67 = 0;
  *(undefined8 *)(pcVar10 + 8) = s__cache_recovery_last_log_0014aa51._8_8_;
  *(undefined8 *)pcVar10 = uVar4;
  *(undefined8 *)(pcVar10 + 0x10) = uVar8;
  copy_log_file_to_pmsg((string *)&local_50,&local_68);
  if (((byte)local_68 & 1) != 0) {
    operator_delete((void *)CONCAT44(uStack_54,uStack_58));
  }
  if ((local_50 & 1) != 0) {
    operator_delete(local_40);
  }
  lVar9 = Paths::Get();
  if ((*(byte *)(lVar9 + 0x78) & 1) == 0) {
    uStack_48 = *(undefined8 *)(lVar9 + 0x80);
    local_50 = *(ulong *)(lVar9 + 0x78);
    local_40 = *(char **)(lVar9 + 0x88);
  }
  else {
    std::string::__init_copy_ctor_external
              ((string *)&local_50,*(char **)(lVar9 + 0x88),*(ulong *)(lVar9 + 0x80));
  }
  pcVar10 = operator_new(0x20);
  pcVar10[0x1c] = '\0';
  uVar6 = s__cache_recovery_last_install_00147e54._20_8_;
  uVar5 = s__cache_recovery_last_install_00147e54._0_8_;
  uVar2 = CONCAT44(s__cache_recovery_last_install_00147e54._12_4_,
                   s__cache_recovery_last_install_00147e54._8_4_);
  uVar3 = CONCAT44(s__cache_recovery_last_install_00147e54._16_4_,
                   s__cache_recovery_last_install_00147e54._12_4_);
  cStack_60 = 0x1c;
  uStack_5f = 0;
  uStack_58 = SUB84(pcVar10,0);
  uStack_54 = (uint)((ulong)pcVar10 >> 0x20);
  local_68 = (string)0x21;
  uStack_67 = 0;
  *(undefined8 *)(pcVar10 + 8) = uVar2;
  *(undefined8 *)pcVar10 = uVar5;
  *(undefined8 *)(pcVar10 + 0x14) = uVar6;
  *(undefined8 *)(pcVar10 + 0xc) = uVar3;
  copy_log_file_to_pmsg((string *)&local_50,&local_68);
  if (((byte)local_68 & 1) == 0) {
    if ((local_50 & 1) == 0) goto LAB_0039046c;
LAB_00390488:
    operator_delete(local_40);
    uVar11 = HasCache();
  }
  else {
    operator_delete((void *)CONCAT44(uStack_54,uStack_58));
    if ((local_50 & 1) != 0) goto LAB_00390488;
LAB_0039046c:
    uVar11 = HasCache();
  }
  if ((uVar11 & 1) != 0) {
    local_40 = operator_new(0x20);
    *(undefined8 *)(local_40 + 0x10) = uVar8;
    local_40[0x18] = '\0';
    uStack_48 = 0x18;
    local_50 = 0x21;
    *(undefined8 *)(local_40 + 8) = uVar7;
    *(undefined8 *)local_40 = uVar4;
    ensure_path_mounted((string *)&local_50);
    if ((local_50 & 1) != 0) {
      operator_delete(local_40);
    }
    local_40 = operator_new(0x20);
    local_40[0x19] = '\0';
    uVar8 = s__cache_recovery_last_kmsg_001545e5._17_8_;
    uVar7 = s__cache_recovery_last_kmsg_001545e5._0_8_;
    uVar4 = CONCAT17(s__cache_recovery_last_kmsg_001545e5[0x10],
                     s__cache_recovery_last_kmsg_001545e5._9_7_);
    uStack_48 = 0x19;
    local_50 = 0x21;
    *(ulong *)(local_40 + 8) =
         CONCAT71(s__cache_recovery_last_kmsg_001545e5._9_7_,s__cache_recovery_last_kmsg_001545e5[8]
                 );
    *(undefined8 *)local_40 = uVar7;
    *(undefined8 *)(local_40 + 0x11) = uVar8;
    *(undefined8 *)(local_40 + 9) = uVar4;
    ensure_path_mounted((string *)&local_50);
    if ((local_50 & 1) != 0) {
      operator_delete(local_40);
    }
    rotate_logs("/cache/recovery/last_log","/cache/recovery/last_kmsg");
    lVar9 = Paths::Get();
    if ((*(byte *)(lVar9 + 0x90) & 1) == 0) {
      uStack_48 = *(undefined8 *)(lVar9 + 0x98);
      local_50 = *(ulong *)(lVar9 + 0x90);
      local_40 = *(char **)(lVar9 + 0xa0);
    }
    else {
      std::string::__init_copy_ctor_external
                ((string *)&local_50,*(char **)(lVar9 + 0xa0),*(ulong *)(lVar9 + 0x98));
    }
    uStack_54 = uStack_54 & 0xffffff00;
    local_68 = (string)0x26;
    uStack_5f = (undefined7)s__cache_recovery_log_001597f6._8_8_;
    uStack_58 = CONCAT31(0x676f6c,SUB81(s__cache_recovery_log_001597f6._8_8_,7));
    uStack_67 = (undefined7)s__cache_recovery_log_001597f6._0_8_;
    cStack_60 = SUB81(s__cache_recovery_log_001597f6._0_8_,7);
    copy_log_file((string *)&local_50,&local_68,true);
    if (((byte)local_68 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_54,uStack_58));
    }
    if ((local_50 & 1) != 0) {
      operator_delete(local_40);
    }
    lVar9 = Paths::Get();
    if ((*(byte *)(lVar9 + 0x90) & 1) == 0) {
      uStack_48 = *(undefined8 *)(lVar9 + 0x98);
      local_50 = *(ulong *)(lVar9 + 0x90);
      local_40 = *(char **)(lVar9 + 0xa0);
    }
    else {
      std::string::__init_copy_ctor_external
                ((string *)&local_50,*(char **)(lVar9 + 0xa0),*(ulong *)(lVar9 + 0x98));
    }
    pcVar10 = operator_new(0x20);
    uVar7 = s__cache_recovery_last_log_0014aa51._8_8_;
    uVar4 = s__cache_recovery_last_log_0014aa51._0_8_;
    pcVar10[0x18] = '\0';
    uVar8 = s__cache_recovery_last_log_0014aa51._16_8_;
    cStack_60 = 0x18;
    uStack_5f = 0;
    uStack_58 = SUB84(pcVar10,0);
    uStack_54 = (uint)((ulong)pcVar10 >> 0x20);
    local_68 = (string)0x21;
    uStack_67 = 0;
    *(undefined8 *)(pcVar10 + 8) = uVar7;
    *(undefined8 *)pcVar10 = uVar4;
    *(undefined8 *)(pcVar10 + 0x10) = uVar8;
    copy_log_file((string *)&local_50,&local_68,false);
    if (((byte)local_68 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_54,uStack_58));
    }
    if ((local_50 & 1) != 0) {
      operator_delete(local_40);
    }
    lVar9 = Paths::Get();
    if ((*(byte *)(lVar9 + 0x78) & 1) == 0) {
      uStack_48 = *(undefined8 *)(lVar9 + 0x80);
      local_50 = *(ulong *)(lVar9 + 0x78);
      local_40 = *(char **)(lVar9 + 0x88);
    }
    else {
      std::string::__init_copy_ctor_external
                ((string *)&local_50,*(char **)(lVar9 + 0x88),*(ulong *)(lVar9 + 0x80));
    }
    pcVar10 = operator_new(0x20);
    pcVar10[0x1c] = '\0';
    *(undefined8 *)(pcVar10 + 8) = uVar2;
    *(undefined8 *)pcVar10 = uVar5;
    local_68 = (string)0x21;
    uStack_67 = 0;
    cStack_60 = 0x1c;
    uStack_5f = 0;
    uStack_58 = SUB84(pcVar10,0);
    uStack_54 = (uint)((ulong)pcVar10 >> 0x20);
    *(undefined8 *)(pcVar10 + 0x14) = uVar6;
    *(undefined8 *)(pcVar10 + 0xc) = uVar3;
    copy_log_file((string *)&local_50,&local_68,false);
    if (((byte)local_68 & 1) != 0) {
      operator_delete((void *)CONCAT44(uStack_54,uStack_58));
    }
    if ((local_50 & 1) != 0) {
      operator_delete(local_40);
    }
    save_kernel_log("/cache/recovery/last_kmsg");
    chmod("/cache/recovery/log",0x180);
    chown("/cache/recovery/log",1000,1000);
    chmod("/cache/recovery/last_kmsg",0x180);
    chown("/cache/recovery/last_kmsg",1000,1000);
    chmod("/cache/recovery/last_log",0x1a0);
    chmod("/cache/recovery/last_install",0x1a4);
    chown("/cache/recovery/last_install",1000,1000);
    sync();
  }
LAB_00390760:
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
