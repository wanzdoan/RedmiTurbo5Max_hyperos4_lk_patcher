
ulong FUN_ffff0000b806fb9c(void)

{
  ulong uVar1;
  long lVar2;
  
  FUN_ffff0000b804c33c
            (s___s___s_ffff0000b808cc8c,s_SEC_CFG_ffff0000b80a18d5,s_sec_restore_ffff0000b809211d);
  FUN_ffff0000b804cd40(&DAT_ffff0000b81b9980,&DAT_ffff0000b81b99c0,0x3c);
  uVar1 = FUN_ffff0000b807339c(s_seccfg_ffff0000b8086c55,&DAT_ffff0000b81b9980,0,0x3c);
  if ((int)uVar1 == 0) {
    lVar2 = FUN_ffff0000b804bd60(0x3000,1);
    if (lVar2 == 0) {
      uVar1 = 0x100006;
    }
    else {
      uVar1 = FUN_ffff0000b80732d8(s_seccfg_ffff0000b8086c55,lVar2,0,0x3c);
      uVar1 = uVar1 & 0xffffffff;
      thunk_FUN_ffff0000b8082558(lVar2);
    }
  }
  return uVar1;
}


