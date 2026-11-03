
undefined8 FUN_ffff0000b806f998(void)

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
  if (DAT_ffff0000b81b99c0 == 0x4d4d4d4d) {
    FUN_ffff0000b804d070(&local_50,0,0x20);
    uVar1 = FUN_ffff0000b8061a14(&DAT_ffff0000b81b9980,0x1c,&local_50);
    if ((int)uVar1 == 0) {
      FUN_ffff0000b804cd40(&local_70,&local_50,0x20);
      FUN_ffff0000b805b4a0(&local_50,0x20);
      uVar1 = FUN_ffff0000b804cd08(&DAT_ffff0000b81b99dc,&local_50,0x20);
      if ((int)uVar1 != 0) {
        FUN_ffff0000b804c33c
                  (s___s__seccfg_bk_hash_backup__ffff0000b809e0cf,s_SEC_CFG_ffff0000b80a18d5);
        uVar2 = 0;
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&local_70 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
        FUN_ffff0000b804c33c(s___s__seccfg_bk_hash_ffff0000b80a18dd,s_SEC_CFG_ffff0000b80a18d5);
        uVar2 = 0;
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&local_50 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
          }
        } while (uVar2 != 0x20);
        FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
        uVar1 = 0x103001;
      }
    }
  }
  else {
    uVar1 = 0x103005;
  }
  return uVar1;
}


