
int FUN_ffff0000b806fd30(void)

{
  int iVar1;
  long lVar2;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  FUN_ffff0000b804c33c(s___s__Initializing_seccfg_major_ffff0000b809139b,s_SEC_CFG_ffff0000b80a18d5)
  ;
  FUN_ffff0000b804d070(&DAT_ffff0000b81b9980,0,0x3c);
  DAT_ffff0000b81b9980 = 0x44d4d4d4d;
  DAT_ffff0000b81b9988 = 0x10000003c;
  DAT_ffff0000b81b9998 = 0x45454545;
  FUN_ffff0000b804d070(&local_40,0,0x20);
  iVar1 = FUN_ffff0000b8061a14(&DAT_ffff0000b81b9980,0x1c,&local_40);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000b805b4a0(&local_40,0x20);
    if (iVar1 == 0) {
      FUN_ffff0000b804cd40(&DAT_ffff0000b81b999c,&local_40,0x20);
      iVar1 = FUN_ffff0000b807339c(s_seccfg_ffff0000b8086c55,&DAT_ffff0000b81b9980,0,0x3c);
      if (iVar1 == 0) {
        lVar2 = FUN_ffff0000b804bd60(0x3000,1);
        if (lVar2 == 0) {
          iVar1 = 0x100006;
        }
        else {
          iVar1 = FUN_ffff0000b80732d8(s_seccfg_ffff0000b8086c55,lVar2,0,0x3c);
          thunk_FUN_ffff0000b8082558(lVar2);
        }
      }
    }
  }
  return iVar1;
}


