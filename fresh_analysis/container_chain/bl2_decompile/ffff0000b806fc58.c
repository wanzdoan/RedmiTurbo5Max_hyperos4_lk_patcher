
int FUN_ffff0000b806fc58(void)

{
  int iVar1;
  long lVar2;
  
  FUN_ffff0000b804d070(&DAT_ffff0000b81b9980,0,0x3c);
  FUN_ffff0000b804d070(&DAT_ffff0000b81b99c0,0,0x3c);
  lVar2 = FUN_ffff0000b804bd14(0x40,0x3000);
  if (lVar2 == 0) {
    iVar1 = 0x100006;
  }
  else {
    iVar1 = FUN_ffff0000b80732d8(s_seccfg_ffff0000b8086c55,lVar2,0,0x3000);
    if (iVar1 == 0) {
      FUN_ffff0000b804cd40(&DAT_ffff0000b81b9980,lVar2,0x3c);
      FUN_ffff0000b804cd40(&DAT_ffff0000b81b99c0,lVar2 + 0x3c,0x3c);
    }
    else {
      FUN_ffff0000b804c33c
                (s___s__read_seccfg_fail__err_0x_x_ffff0000b80987ee,s_SEC_CFG_ffff0000b80a18d5,iVar1
                );
    }
    thunk_FUN_ffff0000b8082558(lVar2);
  }
  return iVar1;
}


