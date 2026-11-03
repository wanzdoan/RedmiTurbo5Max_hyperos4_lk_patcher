
undefined8 FUN_ffff0000907b31a0(void)

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
  if (DAT_ffff00009090d7c0 == 0x4d4d4d4d) {
    FUN_ffff00009077ee50(&local_50,0,0x20);
    uVar1 = FUN_ffff0000907a4318(&DAT_ffff00009090d780,0x1c,&local_50);
    if ((int)uVar1 == 0) {
      FUN_ffff00009077eb20(&local_70,&local_50,0x20);
      FUN_ffff000090798fc8(&local_50,0x20);
      uVar1 = FUN_ffff00009077eae8(&DAT_ffff00009090d7dc,&local_50,0x20);
      if ((int)uVar1 != 0) {
        FUN_ffff00009077e11c
                  (s___s__seccfg_bk_hash_backup__ffff0000907fc973,s_SEC_CFG_ffff00009080355a);
        uVar2 = 0;
        do {
          FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&local_70 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
        FUN_ffff00009077e11c(s___s__seccfg_bk_hash_ffff000090803562,s_SEC_CFG_ffff00009080355a);
        uVar2 = 0;
        do {
          FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&local_50 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
        uVar1 = 0x103001;
      }
    }
  }
  else {
    uVar1 = 0x103005;
  }
  return uVar1;
}


