// ffff000090798d10  size=116  FUN_ffff000090798d10

undefined8 FUN_ffff000090798d10(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009071c6e8();
  if (iVar1 == 2) {
    uVar2 = FUN_ffff000090798e50(param_1,param_2);
    return uVar2;
  }
  if (iVar1 == 1) {
    uVar2 = FUN_ffff0000907a5df8(param_1,param_2);
    return uVar2;
  }
  return 0x10c000;
}


