// ffff0000907b2df0  size=256  FUN_ffff0000907b2df0

int FUN_ffff0000907b2df0(void)

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
  FUN_ffff00009077de54
            (s___s___s_ffff0000907dd5f0,s_SEC_CFG_ffff000090803332,s_sec_update_ffff0000907ea2a9);
  FUN_ffff00009077eb88(&local_40,0,0x20);
  iVar1 = FUN_ffff0000907a4058(&DAT_ffff00009090d6c0,0x1c,&local_40);
  if (iVar1 == 0) {
    FUN_ffff000090798d08(&local_40,0x20);
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
  return iVar1;
}


