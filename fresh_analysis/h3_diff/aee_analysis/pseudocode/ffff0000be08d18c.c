
undefined4 FUN_ffff0000be08d18c(void)

{
  int iVar1;
  ulong uVar2;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  if (DAT_ffff0000be1b7c40 != 0x4d4d4d4d) {
    return 0x103005;
  }
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  iVar1 = FUN_ffff0000be07f3d4(&DAT_ffff0000be1b7c40,0x1c,&local_60);
  if (iVar1 == 0) {
    FUN_ffff0000be0607b0(&local_80,&local_60,0x20);
    iVar1 = FUN_ffff0000be075690(&local_60,0x20);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000be060778(&DAT_ffff0000be1b7c5c,&local_60,0x20);
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff0000be05fd98(s___s__seccfg_hw_hash__ffff0000be0c2719,s_SEC_CFG_ffff0000be0c6d63);
      uVar2 = 0;
      do {
        FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&local_80 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
        }
      } while (uVar2 != 0x20);
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
      FUN_ffff0000be05fd98(s___s__seccfg_sej_cipher_ffff0000be0ac154,s_SEC_CFG_ffff0000be0c6d63);
      uVar2 = 0;
      do {
        FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&local_60 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
        }
      } while (uVar2 != 0x20);
      FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
    }
    else {
      FUN_ffff0000be05fd98
                (s___s___s__seccfg_sej_encrpytion_f_ffff0000be0befc9,s_SEC_CFG_ffff0000be0c6d63,
                 s_hash_enc_seccfg_ffff0000be0bca79,iVar1);
    }
    FUN_ffff0000be08df48(&local_80);
    uVar2 = FUN_ffff0000be0852a4();
  }
  else {
    FUN_ffff0000be05fd98
              (s___s___s__seccfg_hash_failed__0x__ffff0000be0befa6,s_SEC_CFG_ffff0000be0c6d63,
               s_hash_enc_seccfg_ffff0000be0bca79,iVar1);
    uVar2 = FUN_ffff0000be0852a4();
  }
  if ((uVar2 & 1) != 0) {
    FUN_ffff0000be05fd98
              (s___s__seccfg_integrity_check_WA__ffff0000be0c158f,s_SEC_CFG_ffff0000be0c6d63);
    return 0;
  }
  return 0x103001;
}


