// ffff0000907b3084  size=308  FUN_ffff0000907b3084

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907b3084(void)

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
  FUN_ffff00009077de54(s___s__Initializing_seccfg_major_ffff0000907e5748,s_SEC_CFG_ffff000090803332)
  ;
  FUN_ffff00009077eb88(&DAT_ffff00009090d6c0,0,0x3c);
  _DAT_ffff00009090d6c0 = 0x44d4d4d4d;
  _DAT_ffff00009090d6c8 = 0x10000003c;
  _DAT_ffff00009090d6d8 = 0x45454545;
  FUN_ffff00009077eb88(&local_40,0,0x20);
  iVar1 = FUN_ffff0000907a4058(&DAT_ffff00009090d6c0,0x1c,&local_40);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090798d08(&local_40,0x20);
    if (iVar1 == 0) {
      FUN_ffff00009077e858(0xffff00009090d6dc,&local_40,0x20);
      iVar1 = FUN_ffff0000907b7b60(s_seccfg_ffff0000907d1c33,&DAT_ffff00009090d6c0,0,0x3c);
      if (iVar1 == 0) {
        lVar2 = FUN_ffff00009077d2e0(0x3000,1);
        if (lVar2 == 0) {
          iVar1 = 0x100006;
        }
        else {
          iVar1 = FUN_ffff0000907b7a9c(s_seccfg_ffff0000907d1c33,lVar2,0,0x3c);
          thunk_FUN_ffff0000907c2ad8(lVar2);
        }
      }
    }
  }
  return iVar1;
}


