
/* save_install_status(int) */

void save_install_status(int param_1)

{
  undefined4 uVar1;
  long lVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  int iVar6;
  ulong uVar7;
  FILE *__s;
  size_t sVar8;
  int *piVar9;
  ostream *poVar10;
  undefined4 *puVar11;
  char *pcVar12;
  ulong local_60 [2];
  char *local_50;
  long local_48;

  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  if (((uint)param_1 < 10) && ((0x30fU >> (ulong)(param_1 & 0x1f) & 1) != 0)) {
    pcVar12 = (&PTR_s_INSTALL_SUCCESS_00428500)[param_1];
    uVar7 = android::base::ShouldLog(2,0);
    if ((uVar7 & 1) != 0) {
      puVar11 = (undefined4 *)__errno();
      uVar1 = *puVar11;
      android::base::LogMessage::LogMessage
                ((LogMessage *)local_60,"bootable/recovery/mi_adb_install.cpp",0x2aa,2,0,0xffffffff)
      ;
      poVar10 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_60);
      poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar10,"Saving new_status:",0x12);
      sVar8 = strlen(pcVar12);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar10,pcVar12,sVar8);
      android::base::LogMessage::~LogMessage((LogMessage *)local_60);
      *puVar11 = uVar1;
    }
    local_50 = operator_new(0x20);
    local_50[0x1b] = '\0';
    uVar5 = s__cache_recovery_last_status_00152103._19_8_;
    uVar4 = s__cache_recovery_last_status_00152103._0_8_;
    uVar3 = CONCAT35(s__cache_recovery_last_status_00152103._16_3_,
                     s__cache_recovery_last_status_00152103._11_5_);
    local_60[1] = 0x1b;
    local_60[0] = 0x21;
    *(ulong *)(local_50 + 8) =
         CONCAT53(s__cache_recovery_last_status_00152103._11_5_,
                  s__cache_recovery_last_status_00152103._8_3_);
    *(undefined8 *)local_50 = uVar4;
    *(undefined8 *)(local_50 + 0x13) = uVar5;
    *(undefined8 *)(local_50 + 0xb) = uVar3;
    __s = (FILE *)fopen_path((string *)local_60,"w",(selabel_handle *)0x0);
    if ((local_60[0] & 1) != 0) {
      operator_delete(local_50);
    }
    if (__s == (FILE *)0x0) {
      uVar7 = android::base::ShouldLog(4,0);
      if ((uVar7 & 1) == 0) goto LAB_0022f518;
      piVar9 = (int *)__errno();
      iVar6 = *piVar9;
      android::base::LogMessage::LogMessage
                ((LogMessage *)local_60,"bootable/recovery/mi_adb_install.cpp",0x2ad,4,0,0xffffffff)
      ;
      poVar10 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_60);
      poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar10,"Can\'t open",10);
      poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar10,"/cache/recovery/last_status",0x1b);
      poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar10,":",1);
      pcVar12 = strerror(*piVar9);
      sVar8 = strlen(pcVar12);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar10,pcVar12,sVar8);
    }
    else {
      sVar8 = strlen(pcVar12);
      fwrite(pcVar12,1,sVar8,__s);
      iVar6 = fileno(__s);
      fsync(iVar6);
      fflush(__s);
      iVar6 = ferror(__s);
      if ((iVar6 != 0) && (uVar7 = android::base::ShouldLog(4,0), (uVar7 & 1) != 0)) {
        piVar9 = (int *)__errno();
        iVar6 = *piVar9;
        android::base::LogMessage::LogMessage
                  ((LogMessage *)local_60,"bootable/recovery/mi_adb_install.cpp",99,4,0,0xffffffff);
        poVar10 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_60);
        poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar10,"Error in ",9);
        poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar10,"/cache/recovery/last_status",0x1b);
        poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar10,"(",1);
        pcVar12 = strerror(*piVar9);
        sVar8 = strlen(pcVar12);
        poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                            (poVar10,pcVar12,sVar8);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>(poVar10,")",1);
        android::base::LogMessage::~LogMessage((LogMessage *)local_60);
        *piVar9 = iVar6;
      }
      fclose(__s);
      iVar6 = chmod("/cache/recovery/last_status",0x1a4);
      if ((-1 < iVar6) || (uVar7 = android::base::ShouldLog(4,0), (uVar7 & 1) == 0))
      goto LAB_0022f518;
      piVar9 = (int *)__errno();
      iVar6 = *piVar9;
      android::base::LogMessage::LogMessage
                ((LogMessage *)local_60,"bootable/recovery/mi_adb_install.cpp",0x66,4,0,0xffffffff);
      poVar10 = (ostream *)android::base::LogMessage::stream((LogMessage *)local_60);
      poVar10 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar10,"chmod error: ",0xd);
      pcVar12 = strerror(*piVar9);
      sVar8 = strlen(pcVar12);
      std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                (poVar10,pcVar12,sVar8);
    }
    android::base::LogMessage::~LogMessage((LogMessage *)local_60);
    *piVar9 = iVar6;
  }
LAB_0022f518:
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
