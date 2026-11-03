
ulong FUN_ffff0000907b3560(void)

{
  ulong uVar1;
  long lVar2;
  
  FUN_ffff00009077e11c
            (s___s___s_ffff0000907dd693,s_SEC_CFG_ffff00009080355a,s_sec_restore_ffff0000907e7102);
  FUN_ffff00009077eb20(&DAT_ffff00009090d780,&DAT_ffff00009090d7c0,0x3c);
  uVar1 = FUN_ffff0000907b81d4(s_seccfg_ffff0000907d1c70,&DAT_ffff00009090d780,0,0x3c);
  if ((int)uVar1 == 0) {
    lVar2 = FUN_ffff00009077d5a8(0x3000,1);
    if (lVar2 == 0) {
      uVar1 = 0x100006;
    }
    else {
      uVar1 = FUN_ffff0000907b8110(s_seccfg_ffff0000907d1c70,lVar2,0,0x3c);
      uVar1 = uVar1 & 0xffffffff;
      thunk_FUN_ffff0000907c314c(lVar2);
    }
  }
  return uVar1;
}


