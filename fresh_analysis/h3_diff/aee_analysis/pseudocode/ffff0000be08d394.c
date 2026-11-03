
undefined8 FUN_ffff0000be08d394(void)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  if (DAT_ffff0000be1b7c80 == 0x4d4d4d4d) {
    FUN_ffff0000be060ae0(&local_50,0,0x20);
    uVar1 = FUN_ffff0000be07f3d4(&DAT_ffff0000be1b7c40,0x1c,&local_50);
    if ((int)uVar1 == 0) {
      FUN_ffff0000be0607b0(&local_70,&local_50,0x20);
      FUN_ffff0000be075690(&local_50,0x20);
      uVar1 = FUN_ffff0000be060778(&DAT_ffff0000be1b7c9c,&local_50,0x20);
      if ((int)uVar1 != 0) {
        FUN_ffff0000be05fd98
                  (s___s__seccfg_bk_hash_backup__ffff0000be0c26fc,s_SEC_CFG_ffff0000be0c6d63);
        uVar2 = 0;
        do {
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&local_70 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
        FUN_ffff0000be05fd98(s___s__seccfg_bk_hash_ffff0000be0c6d6b,s_SEC_CFG_ffff0000be0c6d63);
        uVar2 = 0;
        do {
          FUN_ffff0000be05fd98(&DAT_ffff0000be0a742a,*(undefined1 *)((long)&local_50 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000be05fd98(&DAT_ffff0000be0bf441);
        uVar1 = 0x103001;
      }
    }
  }
  else {
    uVar1 = 0x103005;
  }
  return uVar1;
}


