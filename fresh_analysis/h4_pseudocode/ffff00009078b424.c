
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078b424(undefined8 param_1)

{
  undefined *puVar1;
  bool bVar2;
  long lVar3;
  
  bVar2 = (_DAT_ffff000016703e28 & 0xff) != 0;
  DAT_ffff00009088a21d = DAT_ffff00009088a21d & 0xfd | bVar2 << 1;
  puVar1 = &DAT_ffff00009080308f;
  if (bVar2) {
    puVar1 = &DAT_ffff0000907d3983;
  }
  FUN_ffff00009077e11c(s__USB__IP_version_0x_x__s_IP__ffff0000907f38b6,_DAT_ffff000016703ea0,puVar1)
  ;
  bVar2 = (DAT_ffff00009088a21d & 2) != 0;
  DAT_ffff00009088a21c = 0;
  DAT_ffff00009088a0cc = 2;
  if (bVar2) {
    DAT_ffff00009088a0cc = 3;
  }
  DAT_ffff00009088a0d8 = 0x600;
  if (bVar2) {
    DAT_ffff00009088a0d8 = 0xc00;
  }
  DAT_ffff00009088a0d0 = (DAT_ffff00009088a21d & 2) << 5 ^ 0x40;
  DAT_ffff00009088a0d4 = 0x1040;
  if (bVar2) {
    DAT_ffff00009088a0d4 = 0;
  }
  DAT_ffff00009088a0c8 = 0;
  DAT_ffff00009088a1b8 = &DAT_ffff00009088a0e0;
  lVar3 = FUN_ffff00009077d514(0x200);
  if (lVar3 == 0) {
    DAT_ffff00009088a1c0 = 0;
  }
  else {
    FUN_ffff00009077ee50(lVar3,0,0x200);
    DAT_ffff00009088a200 = 0;
    _DAT_ffff00009088a1f8 = 0x10001;
    DAT_ffff00009088a208 = 1;
    DAT_ffff00009088a218 = 1;
    DAT_ffff00009088a1c0 = lVar3;
    *DAT_ffff00009088a1b8 = &DAT_ffff00009088a1c0;
    FUN_ffff00009078d81c();
  }
  DAT_ffff00009088a0b8 = param_1;
  FUN_ffff0000907186c8(&DAT_ffff00009088a220);
  FUN_ffff000090718870(&DAT_ffff00009088a220,500,FUN_ffff00009078b570,0);
  return 0;
}


