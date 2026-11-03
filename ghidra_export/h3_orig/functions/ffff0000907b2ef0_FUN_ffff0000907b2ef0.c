// ffff0000907b2ef0  size=188  FUN_ffff0000907b2ef0

ulong FUN_ffff0000907b2ef0(void)

{
  ulong uVar1;
  long lVar2;
  
  FUN_ffff00009077de54
            (s___s___s_ffff0000907dd5f0,s_SEC_CFG_ffff000090803332,s_sec_restore_ffff0000907e6fbe);
  FUN_ffff00009077e858(&DAT_ffff00009090d6c0,&DAT_ffff00009090d700,0x3c);
  uVar1 = FUN_ffff0000907b7b60(s_seccfg_ffff0000907d1c33,&DAT_ffff00009090d6c0,0,0x3c);
  if ((int)uVar1 == 0) {
    lVar2 = FUN_ffff00009077d2e0(0x3000,1);
    if (lVar2 == 0) {
      uVar1 = 0x100006;
    }
    else {
      uVar1 = FUN_ffff0000907b7a9c(s_seccfg_ffff0000907d1c33,lVar2,0,0x3c);
      uVar1 = uVar1 & 0xffffffff;
      thunk_FUN_ffff0000907c2ad8(lVar2);
    }
  }
  return uVar1;
}


