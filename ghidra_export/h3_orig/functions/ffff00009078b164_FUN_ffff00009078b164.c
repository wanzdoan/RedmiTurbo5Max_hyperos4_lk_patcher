// ffff00009078b164  size=332  FUN_ffff00009078b164

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078b164(undefined8 param_1)

{
  undefined *puVar1;
  bool bVar2;
  long lVar3;
  
  bVar2 = (_DAT_ffff000016703e28 & 0xff) != 0;
  DAT_ffff00009088a185 = DAT_ffff00009088a185 & 0xfd | bVar2 << 1;
  puVar1 = &DAT_ffff000090802e8b;
  if (bVar2) {
    puVar1 = &DAT_ffff0000907d3946;
  }
  FUN_ffff00009077de54(s__USB__IP_version_0x_x__s_IP__ffff0000907f372a,_DAT_ffff000016703ea0,puVar1)
  ;
  bVar2 = (DAT_ffff00009088a185 & 2) != 0;
  DAT_ffff00009088a184 = 0;
  _DAT_ffff00009088a034 = 2;
  if (bVar2) {
    _DAT_ffff00009088a034 = 3;
  }
  _DAT_ffff00009088a040 = 0x600;
  if (bVar2) {
    _DAT_ffff00009088a040 = 0xc00;
  }
  _DAT_ffff00009088a038 = (DAT_ffff00009088a185 & 2) << 5 ^ 0x40;
  _DAT_ffff00009088a03c = 0x1040;
  if (bVar2) {
    _DAT_ffff00009088a03c = 0;
  }
  _DAT_ffff00009088a030 = 0;
  _DAT_ffff00009088a120 = (undefined8 *)&DAT_ffff00009088a048;
  lVar3 = FUN_ffff00009077d24c(0x200);
  if (lVar3 == 0) {
    _DAT_ffff00009088a128 = 0;
  }
  else {
    FUN_ffff00009077eb88(lVar3,0,0x200);
    _DAT_ffff00009088a168 = 0;
    _DAT_ffff00009088a160 = 0x10001;
    _DAT_ffff00009088a170 = 1;
    _DAT_ffff00009088a180 = 1;
    _DAT_ffff00009088a128 = lVar3;
    *_DAT_ffff00009088a120 = &DAT_ffff00009088a128;
    FUN_ffff00009078d55c();
  }
  _DAT_ffff00009088a020 = param_1;
  FUN_ffff000090718584(0xffff00009088a188);
  FUN_ffff00009071872c(0xffff00009088a188,500,FUN_ffff00009078b2b0,0);
  return 0;
}


