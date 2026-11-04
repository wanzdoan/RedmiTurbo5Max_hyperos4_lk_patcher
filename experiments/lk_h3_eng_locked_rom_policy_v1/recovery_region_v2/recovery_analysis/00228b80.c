
/* FinishRecovery(RecoveryUI*) */

void FinishRecovery(RecoveryUI *param_1)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  int iVar6;
  ulong uVar7;
  undefined4 *puVar8;
  ostream *poVar9;
  int *piVar10;
  undefined4 uVar11;
  ulong local_80 [2];
  char *local_70;
  ulong local_68 [2];
  char *local_58;
  ulong local_50;
  ulong local_48;
  void *local_40;
  long local_38;

  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_50 = 0;
  local_48 = 0;
  local_40 = (void *)0x0;
  (**(code **)(*(long *)param_1 + 0x108))(&local_50);
  uVar7 = local_50 >> 1 & 0x7f;
  if ((local_50 & 1) != 0) {
    uVar7 = local_48;
  }
  if ((uVar7 != 0) && (uVar7 = HasCache(), (uVar7 & 1) != 0)) {
    uVar7 = android::base::ShouldLog(2,0);
    if ((uVar7 & 1) != 0) {
      puVar8 = (undefined4 *)__errno();
      uVar11 = *puVar8;
      android::base::LogMessage::LogMessage
                ((LogMessage *)local_68,"bootable/recovery/recovery.cpp",0xab,2,0,0xffffffff);
      poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_68);
      poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         (poVar9,"Saving locale \"",0xf);
      poVar9 = std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::
               operator<(poVar9,(string *)&local_50);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar9,"\"",1);
      android::base::LogMessage::~LogMessage((LogMessage *)local_68);
      *puVar8 = uVar11;
    }
    local_58 = operator_new(0x20);
    local_58[0x1b] = '\0';
    uVar5 = s__cache_recovery_last_locale_0015535e._19_8_;
    uVar4 = s__cache_recovery_last_locale_0015535e._0_8_;
    uVar2 = CONCAT53(s__cache_recovery_last_locale_0015535e._11_5_,
                     s__cache_recovery_last_locale_0015535e._8_3_);
    uVar3 = CONCAT35(s__cache_recovery_last_locale_0015535e._16_3_,
                     s__cache_recovery_last_locale_0015535e._11_5_);
    local_68[1] = 0x1b;
    local_68[0] = 0x21;
    *(undefined8 *)(local_58 + 8) = uVar2;
    *(undefined8 *)local_58 = uVar4;
    *(undefined8 *)(local_58 + 0x13) = uVar5;
    *(undefined8 *)(local_58 + 0xb) = uVar3;
    iVar6 = ensure_path_mounted((string *)local_68);
    if ((local_68[0] & 1) != 0) {
      operator_delete(local_58);
    }
    if (iVar6 == 0) {
      local_58 = operator_new(0x20);
      local_58[0x1b] = '\0';
      *(undefined8 *)(local_58 + 8) = uVar2;
      *(undefined8 *)local_58 = uVar4;
      local_68[0] = 0x21;
      local_68[1] = 0x1b;
      *(undefined8 *)(local_58 + 0x13) = uVar5;
      *(undefined8 *)(local_58 + 0xb) = uVar3;
      uVar7 = android::base::WriteStringToFile((string *)&local_50,(string *)local_68,false);
      if ((local_68[0] & 1) != 0) {
        operator_delete(local_58);
      }
      if (((uVar7 & 1) == 0) && (uVar7 = android::base::ShouldLog(4,0), (uVar7 & 1) != 0)) {
        puVar8 = (undefined4 *)__errno();
        uVar11 = *puVar8;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)local_68,"bootable/recovery/recovery.cpp",0xaf,4,0,uVar11);
        poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_68);
        poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                           (poVar9,"Failed to save locale to ",0x19);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar9,"/cache/recovery/last_locale",0x1b);
        goto LAB_00228fe4;
      }
    }
    else {
      uVar7 = android::base::ShouldLog(4,0);
      if ((uVar7 & 1) != 0) {
        puVar8 = (undefined4 *)__errno();
        uVar11 = *puVar8;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)local_68,"bootable/recovery/recovery.cpp",0xad,4,0,0xffffffff);
        poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_68);
        poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                           (poVar9,"Failed to mount ",0x10);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar9,"/cache/recovery/last_locale",0x1b);
LAB_00228fe4:
        android::base::LogMessage::~LogMessage((LogMessage *)local_68);
        *puVar8 = uVar11;
      }
    }
  }
  copy_logs(true);
  local_68[0] = 0;
  local_68[1] = 0;
  local_58 = (void *)0x0;
  uVar7 = clear_bootloader_message((string *)local_68);
  if (((uVar7 & 1) == 0) && (uVar7 = android::base::ShouldLog(4,0), (uVar7 & 1) != 0)) {
    puVar8 = (undefined4 *)__errno();
    uVar11 = *puVar8;
    android::base::LogMessage::LogMessage
              ((LogMessage *)local_80,"bootable/recovery/recovery.cpp",0xb9,4,0,0xffffffff);
    poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_80);
    poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                       (poVar9,"Failed to clear BCB message: ",0x1d);
    std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
              (poVar9,(string *)local_68);
    android::base::LogMessage::~LogMessage((LogMessage *)local_80);
    *puVar8 = uVar11;
  }
  uVar7 = HasCache();
  if ((uVar7 & 1) == 0) goto LAB_00228e08;
  local_70 = operator_new(0x20);
  local_70[0x17] = '\0';
  uVar3 = s__cache_recovery_command_0014b87e._0_8_;
  uVar2 = CONCAT71(s__cache_recovery_command_0014b87e._16_7_,s__cache_recovery_command_0014b87e[0xf]
                  );
  local_80[1] = 0x17;
  local_80[0] = 0x21;
  *(ulong *)(local_70 + 8) =
       CONCAT17(s__cache_recovery_command_0014b87e[0xf],s__cache_recovery_command_0014b87e._8_7_);
  *(undefined8 *)local_70 = uVar3;
  *(undefined8 *)(local_70 + 0xf) = uVar2;
  iVar6 = ensure_path_mounted((string *)local_80);
  if (iVar6 == 0) {
    iVar6 = unlink("/cache/recovery/command");
    if (iVar6 == 0) {
      if ((local_80[0] & 1) != 0) {
        operator_delete(local_70);
      }
    }
    else {
      piVar10 = (int *)__errno();
      iVar6 = *piVar10;
      if ((local_80[0] & 1) != 0) {
        operator_delete(local_70);
      }
      if (iVar6 != 2) goto LAB_00228dc0;
    }
  }
  else {
    if ((local_80[0] & 1) != 0) {
      operator_delete(local_70);
    }
LAB_00228dc0:
    uVar7 = android::base::ShouldLog(3,0);
    if ((uVar7 & 1) != 0) {
      puVar8 = (undefined4 *)__errno();
      uVar11 = *puVar8;
      android::base::LogMessage::LogMessage
                ((LogMessage *)local_80,"bootable/recovery/recovery.cpp",0xbf,3,0,0xffffffff);
      poVar9 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_80);
      poVar9 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                         (poVar9,"Can\'t unlink ",0xd);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar9,"/cache/recovery/command",0x17);
      android::base::LogMessage::~LogMessage((LogMessage *)local_80);
      *puVar8 = uVar11;
    }
  }
  local_80[0] = 0x65686361632f0c;
  ensure_path_unmounted((string *)local_80);
  if ((local_80[0] & 1) != 0) {
    operator_delete(local_70);
  }
LAB_00228e08:
  sync();
  if ((local_68[0] & 1) != 0) {
    operator_delete(local_58);
  }
  if ((local_50 & 1) != 0) {
    operator_delete(local_40);
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
