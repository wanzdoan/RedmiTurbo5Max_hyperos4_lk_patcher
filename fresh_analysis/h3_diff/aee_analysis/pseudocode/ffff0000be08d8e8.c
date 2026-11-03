
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be08d8e8(void)

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
  FUN_ffff0000be05fd98(s___s__Initializing_seccfg_major_ffff0000be0b2913,s_SEC_CFG_ffff0000be0c6d63)
  ;
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b7c40,0,0x3c);
  _DAT_ffff0000be1b7c40 = 0x44d4d4d4d;
  _DAT_ffff0000be1b7c48 = 0x10000003c;
  DAT_ffff0000be1b7c58 = 0x45454545;
  FUN_ffff0000be060ae0(&local_40,0,0x20);
  iVar1 = FUN_ffff0000be07f3d4(&DAT_ffff0000be1b7c40,0x1c,&local_40);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff0000be075690(&local_40,0x20);
    if (iVar1 == 0) {
      FUN_ffff0000be0607b0(&DAT_ffff0000be1b7c5c,&local_40,0x20);
      iVar1 = FUN_ffff0000be091ff8(s_seccfg_ffff0000be0a4fd5,&DAT_ffff0000be1b7c40,0,0x3c);
      if (iVar1 == 0) {
        lVar2 = FUN_ffff0000be05f250(0x3000,1);
        if (lVar2 == 0) {
          iVar1 = 0x100006;
        }
        else {
          iVar1 = FUN_ffff0000be091f34(s_seccfg_ffff0000be0a4fd5,lVar2,0,0x3c);
          thunk_FUN_ffff0000be09937c(lVar2);
        }
      }
    }
  }
  return iVar1;
}


