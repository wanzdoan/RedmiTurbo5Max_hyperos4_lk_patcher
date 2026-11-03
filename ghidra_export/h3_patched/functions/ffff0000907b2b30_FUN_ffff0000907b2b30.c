// ffff0000907b2b30  size=408  FUN_ffff0000907b2b30

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b2b30(void)

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
  if (_DAT_ffff00009090d700 == 0x4d4d4d4d) {
    FUN_ffff00009077eb88(&local_50,0,0x20);
    uVar1 = FUN_ffff0000907a4058(&DAT_ffff00009090d6c0,0x1c,&local_50);
    if ((int)uVar1 == 0) {
      FUN_ffff00009077e858(&local_70,&local_50,0x20);
      FUN_ffff000090798d08(&local_50,0x20);
      uVar1 = FUN_ffff00009077e820(0xffff00009090d71c,&local_50,0x20);
      if ((int)uVar1 != 0) {
        FUN_ffff00009077de54
                  (s___s__seccfg_bk_hash_backup__ffff0000907fc7d7,s_SEC_CFG_ffff000090803332);
        uVar2 = 0;
        do {
          FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)((long)&local_70 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        FUN_ffff00009077de54(s___s__seccfg_bk_hash_ffff00009080333a,s_SEC_CFG_ffff000090803332);
        uVar2 = 0;
        do {
          FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)((long)&local_50 + uVar2));
          uVar2 = uVar2 + 1;
          if ((uVar2 & 0xf) == 0) {
            FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
          }
        } while (uVar2 != 0x20);
        FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        uVar1 = 0x103001;
      }
    }
  }
  else {
    uVar1 = 0x103005;
  }
  return uVar1;
}


