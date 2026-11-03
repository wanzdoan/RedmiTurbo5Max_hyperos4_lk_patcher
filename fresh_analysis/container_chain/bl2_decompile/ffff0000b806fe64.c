
int FUN_ffff0000b806fe64(void)

{
  int iVar1;
  char *pcVar2;
  undefined8 local_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined4 local_28;
  
  iVar1 = FUN_ffff0000b806fc58();
  if (iVar1 != 0) {
    return iVar1;
  }
  if ((int)DAT_ffff0000b81b9980 == 0x4d4d4d4d) {
    iVar1 = FUN_ffff0000b806f790();
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c
                (s___s___s__Check_major_integrity_f_ffff0000b8096c76,s_SEC_CFG_ffff0000b80a18d5,
                 s_sec_get_seccfg_ffff0000b8092129);
      FUN_ffff0000b806ffb4();
      goto LAB_ffff0000b806fec4;
    }
    if (DAT_ffff0000b81b99c0 != 0x4d4d4d4d) {
      return 0;
    }
    local_28 = 0;
    local_38 = 0;
    uStack_30 = 0;
    local_48 = 0;
    uStack_40 = 0;
    local_58 = 0;
    uStack_50 = 0;
    local_60 = 0;
    iVar1 = FUN_ffff0000b807339c(s_seccfg_ffff0000b8086c55,&local_60,0x3c,0x3c);
joined_r0xffff0000b806ff7c:
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___s__clear_bak_fail__ffff0000b80913bc;
LAB_ffff0000b806ff88:
    FUN_ffff0000b804c33c(pcVar2,s_SEC_CFG_ffff0000b80a18d5,s_sec_get_seccfg_ffff0000b8092129);
  }
  else {
LAB_ffff0000b806fec4:
    if (DAT_ffff0000b81b99c0 == 0x4d4d4d4d) {
      iVar1 = FUN_ffff0000b806f998();
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000b806fb9c();
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000b806fb30();
          goto joined_r0xffff0000b806ff7c;
        }
        pcVar2 = s___s___s__seccfg_restore_fail__ffff0000b809d1e0;
        goto LAB_ffff0000b806ff88;
      }
      FUN_ffff0000b804c33c
                (s___s___s__Check_backup_integrity_f_ffff0000b8086c7c,s_SEC_CFG_ffff0000b80a18d5,
                 s_sec_get_seccfg_ffff0000b8092129);
      FUN_ffff0000b806ffb4();
    }
    iVar1 = FUN_ffff0000b806fd30();
    FUN_ffff0000b806ffb4();
  }
  return iVar1;
}


