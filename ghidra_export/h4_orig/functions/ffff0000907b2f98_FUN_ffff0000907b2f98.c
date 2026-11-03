// ffff0000907b2f98  size=520  FUN_ffff0000907b2f98

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b2f98(void)

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
  
  if (_DAT_ffff00009090d780 != 0x4d4d4d4d) {
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
  iVar1 = FUN_ffff0000907a4318(&DAT_ffff00009090d780,0x1c,&local_60);
  if (iVar1 == 0) {
    FUN_ffff00009077eb20(&local_80,&local_60,0x20);
    iVar1 = FUN_ffff000090798fc8(&local_60,0x20);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077eae8(0xffff00009090d79c,&local_60,0x20);
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff00009077e11c(s___s__seccfg_hw_hash__ffff0000907fc990,s_SEC_CFG_ffff00009080355a);
      uVar2 = 0;
      do {
        FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&local_80 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
        }
      } while (uVar2 != 0x20);
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
      FUN_ffff00009077e11c(s___s__seccfg_sej_cipher_ffff0000907dbd02,s_SEC_CFG_ffff00009080355a);
      uVar2 = 0;
      do {
        FUN_ffff00009077e11c(&DAT_ffff0000907d4d8c,*(undefined1 *)((long)&local_60 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
        }
      } while (uVar2 != 0x20);
      FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
    }
    else {
      FUN_ffff00009077e11c
                (s___s___s__seccfg_sej_encrpytion_f_ffff0000907f7642,s_SEC_CFG_ffff00009080355a,
                 s_hash_enc_seccfg_ffff0000907f3c47,iVar1);
    }
    FUN_ffff0000907b3e48(&local_80);
    uVar2 = FUN_ffff0000907aa2b0();
  }
  else {
    FUN_ffff00009077e11c
              (s___s___s__seccfg_hash_failed__0x__ffff0000907f761f,s_SEC_CFG_ffff00009080355a,
               s_hash_enc_seccfg_ffff0000907f3c47,iVar1);
    uVar2 = FUN_ffff0000907aa2b0();
  }
  if ((uVar2 & 1) != 0) {
    FUN_ffff00009077e11c
              (s___s__seccfg_integrity_check_WA__ffff0000907fad3a,s_SEC_CFG_ffff00009080355a);
    return 0;
  }
  return 0x103001;
}


