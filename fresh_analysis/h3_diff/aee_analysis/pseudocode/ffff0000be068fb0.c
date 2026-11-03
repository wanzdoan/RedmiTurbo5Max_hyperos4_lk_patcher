
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be068fb0(undefined8 param_1)

{
  undefined *puVar1;
  bool bVar2;
  long lVar3;
  
  bVar2 = (_DAT_ffff000016703e28 & 0xff) != 0;
  DAT_ffff0000be1348a5 = DAT_ffff0000be1348a5 & 0xfd | bVar2 << 1;
  puVar1 = &DAT_ffff0000be0c69cc;
  if (bVar2) {
    puVar1 = &DAT_ffff0000be0a6432;
  }
  FUN_ffff0000be05fd98(s__USB__IP_version_0x_x__s_IP__ffff0000be0bc728,_DAT_ffff000016703ea0,puVar1)
  ;
  bVar2 = (DAT_ffff0000be1348a5 & 2) != 0;
  DAT_ffff0000be1348a4 = 0;
  DAT_ffff0000be134754 = 2;
  if (bVar2) {
    DAT_ffff0000be134754 = 3;
  }
  DAT_ffff0000be134760 = 0x600;
  if (bVar2) {
    DAT_ffff0000be134760 = 0xc00;
  }
  DAT_ffff0000be134758 = (DAT_ffff0000be1348a5 & 2) << 5 ^ 0x40;
  DAT_ffff0000be13475c = 0x1040;
  if (bVar2) {
    DAT_ffff0000be13475c = 0;
  }
  DAT_ffff0000be134750 = 0;
  DAT_ffff0000be134840 = &DAT_ffff0000be134768;
  lVar3 = FUN_ffff0000be05f1bc(0x200);
  if (lVar3 == 0) {
    DAT_ffff0000be134848 = 0;
  }
  else {
    FUN_ffff0000be060ae0(lVar3,0,0x200);
    DAT_ffff0000be134888 = 0;
    _DAT_ffff0000be134880 = 0x10001;
    DAT_ffff0000be134890 = 1;
    DAT_ffff0000be1348a0 = 1;
    DAT_ffff0000be134848 = lVar3;
    *DAT_ffff0000be134840 = &DAT_ffff0000be134848;
    FUN_ffff0000be06b3a8();
  }
  DAT_ffff0000be134740 = param_1;
  FUN_ffff0000be01b688(&DAT_ffff0000be1348a8);
  FUN_ffff0000be01b830(&DAT_ffff0000be1348a8,500,FUN_ffff0000be0690fc,0);
  return 0;
}


