// ffff0000907b2928  size=520  FUN_ffff0000907b2928

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b2928(void)

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
  
  if (_DAT_ffff00009090d6c0 != 0x4d4d4d4d) {
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
  iVar1 = FUN_ffff0000907a4058(&DAT_ffff00009090d6c0,0x1c,&local_60);
  if (iVar1 == 0) {
    FUN_ffff00009077e858(&local_80,&local_60,0x20);
    iVar1 = FUN_ffff000090798d08(&local_60,0x20);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077e820(0xffff00009090d6dc,&local_60,0x20);
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff00009077de54(s___s__seccfg_hw_hash__ffff0000907fc7f4,s_SEC_CFG_ffff000090803332);
      uVar2 = 0;
      do {
        FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)((long)&local_80 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        }
      } while (uVar2 != 0x20);
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
      FUN_ffff00009077de54(s___s__seccfg_sej_cipher_ffff0000907dbbe0,s_SEC_CFG_ffff000090803332);
      uVar2 = 0;
      do {
        FUN_ffff00009077de54(&DAT_ffff0000907d4d4f,*(undefined1 *)((long)&local_60 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
        }
      } while (uVar2 != 0x20);
      FUN_ffff00009077de54(&DAT_ffff0000907f81ce);
    }
    else {
      FUN_ffff00009077de54
                (s___s___s__seccfg_sej_encrpytion_f_ffff0000907f74b8,s_SEC_CFG_ffff000090803332,
                 s_hash_enc_seccfg_ffff0000907f3aa4,iVar1);
    }
    FUN_ffff0000907b37d8(&local_80);
    uVar2 = FUN_ffff0000907a9ff0();
  }
  else {
    FUN_ffff00009077de54
              (s___s___s__seccfg_hash_failed__0x__ffff0000907f7495,s_SEC_CFG_ffff000090803332,
               s_hash_enc_seccfg_ffff0000907f3aa4,iVar1);
    uVar2 = FUN_ffff0000907a9ff0();
  }
  if ((uVar2 & 1) != 0) {
    FUN_ffff00009077de54
              (s___s__seccfg_integrity_check_WA__ffff0000907fab9e,s_SEC_CFG_ffff000090803332);
    return 0;
  }
  return 0x103001;
}


