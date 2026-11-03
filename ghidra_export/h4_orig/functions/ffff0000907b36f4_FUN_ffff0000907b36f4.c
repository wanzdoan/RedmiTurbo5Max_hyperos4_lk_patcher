// ffff0000907b36f4  size=308  FUN_ffff0000907b36f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907b36f4(void)

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
  FUN_ffff00009077e11c(s___s__Initializing_seccfg_major_ffff0000907e5863,s_SEC_CFG_ffff00009080355a)
  ;
  FUN_ffff00009077ee50(&DAT_ffff00009090d780,0,0x3c);
  _DAT_ffff00009090d780 = 0x44d4d4d4d;
  _DAT_ffff00009090d788 = 0x10000003c;
  _DAT_ffff00009090d798 = 0x45454545;
  FUN_ffff00009077ee50(&local_40,0,0x20);
  iVar1 = FUN_ffff0000907a4318(&DAT_ffff00009090d780,0x1c,&local_40);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff000090798fc8(&local_40,0x20);
    if (iVar1 == 0) {
      FUN_ffff00009077eb20(0xffff00009090d79c,&local_40,0x20);
      iVar1 = FUN_ffff0000907b81d4(s_seccfg_ffff0000907d1c70,&DAT_ffff00009090d780,0,0x3c);
      if (iVar1 == 0) {
        lVar2 = FUN_ffff00009077d5a8(0x3000,1);
        if (lVar2 == 0) {
          iVar1 = 0x100006;
        }
        else {
          iVar1 = FUN_ffff0000907b8110(s_seccfg_ffff0000907d1c70,lVar2,0,0x3c);
          thunk_FUN_ffff0000907c314c(lVar2);
        }
      }
    }
  }
  return iVar1;
}


