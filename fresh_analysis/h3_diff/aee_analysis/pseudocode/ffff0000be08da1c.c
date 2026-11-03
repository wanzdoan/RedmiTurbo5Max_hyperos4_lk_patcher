
int FUN_ffff0000be08da1c(void)

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
  
  iVar1 = FUN_ffff0000be08d810();
  if (iVar1 != 0) {
    return iVar1;
  }
  if (DAT_ffff0000be1b7c40 == 0x4d4d4d4d) {
    iVar1 = FUN_ffff0000be08d18c();
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98
                (s___s___s__Check_major_integrity_f_ffff0000be0b9510,s_SEC_CFG_ffff0000be0c6d63,
                 s_sec_get_seccfg_ffff0000be0b3974);
      FUN_ffff0000be08db6c();
      goto LAB_ffff0000be08da7c;
    }
    if (DAT_ffff0000be1b7c80 != 0x4d4d4d4d) {
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
    iVar1 = FUN_ffff0000be091ff8(s_seccfg_ffff0000be0a4fd5,&local_60,0x3c,0x3c);
joined_r0xffff0000be08db34:
    if (iVar1 == 0) {
      return 0;
    }
    pcVar2 = s___s___s__clear_bak_fail__ffff0000be0b2934;
LAB_ffff0000be08db40:
    FUN_ffff0000be05fd98(pcVar2,s_SEC_CFG_ffff0000be0c6d63,s_sec_get_seccfg_ffff0000be0b3974);
  }
  else {
LAB_ffff0000be08da7c:
    if (DAT_ffff0000be1b7c80 == 0x4d4d4d4d) {
      iVar1 = FUN_ffff0000be08d394();
      if (iVar1 == 0) {
        iVar1 = FUN_ffff0000be08d754();
        if (iVar1 == 0) {
          iVar1 = FUN_ffff0000be08d52c();
          goto joined_r0xffff0000be08db34;
        }
        pcVar2 = s___s___s__seccfg_restore_fail__ffff0000be0c15b0;
        goto LAB_ffff0000be08db40;
      }
      FUN_ffff0000be05fd98
                (s___s___s__Check_backup_integrity_f_ffff0000be0a5309,s_SEC_CFG_ffff0000be0c6d63,
                 s_sec_get_seccfg_ffff0000be0b3974);
      FUN_ffff0000be08db6c();
    }
    iVar1 = FUN_ffff0000be08d8e8();
    FUN_ffff0000be08db6c();
  }
  return iVar1;
}


