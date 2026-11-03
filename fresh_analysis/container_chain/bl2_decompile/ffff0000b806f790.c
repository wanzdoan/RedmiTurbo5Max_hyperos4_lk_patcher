
undefined4 FUN_ffff0000b806f790(void)

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
  
  if ((int)DAT_ffff0000b81b9980 != 0x4d4d4d4d) {
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
  iVar1 = FUN_ffff0000b8061a14(&DAT_ffff0000b81b9980,0x1c,&local_60);
  if (iVar1 == 0) {
    FUN_ffff0000b804cd40(&local_80,&local_60,0x20);
    iVar1 = FUN_ffff0000b805b4a0(&local_60,0x20);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000b804cd08(&DAT_ffff0000b81b999c,&local_60,0x20);
      if (iVar1 == 0) {
        return 0;
      }
      FUN_ffff0000b804c33c(s___s__seccfg_hw_hash__ffff0000b809e0ec,s_SEC_CFG_ffff0000b80a18d5);
      uVar2 = 0;
      do {
        FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&local_80 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
        }
      } while (uVar2 != 0x20);
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
      FUN_ffff0000b804c33c(s___s__seccfg_sej_cipher_ffff0000b808c126,s_SEC_CFG_ffff0000b80a18d5);
      uVar2 = 0;
      do {
        FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)((long)&local_60 + uVar2));
        uVar2 = uVar2 + 1;
        if ((uVar2 & 0xf) == 0) {
          FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
        }
      } while (uVar2 != 0x20);
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    }
    else {
      FUN_ffff0000b804c33c
                (s___s___s__seccfg_sej_encrpytion_f_ffff0000b809b249,s_SEC_CFG_ffff0000b80a18d5,
                 s_hash_enc_seccfg_ffff0000b80996e9,iVar1);
    }
    FUN_ffff0000b8070264(&local_80);
    uVar2 = FUN_ffff0000b80678e4();
  }
  else {
    FUN_ffff0000b804c33c
              (s___s___s__seccfg_hash_failed__0x__ffff0000b809b226,s_SEC_CFG_ffff0000b80a18d5,
               s_hash_enc_seccfg_ffff0000b80996e9,iVar1);
    uVar2 = FUN_ffff0000b80678e4();
  }
  if ((uVar2 & 1) != 0) {
    FUN_ffff0000b804c33c
              (s___s__seccfg_integrity_check_WA__ffff0000b809d1bf,s_SEC_CFG_ffff0000b80a18d5);
    return 0;
  }
  return 0x103001;
}


