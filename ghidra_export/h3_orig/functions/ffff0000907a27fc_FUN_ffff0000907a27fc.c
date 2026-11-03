// ffff0000907a27fc  size=88  FUN_ffff0000907a27fc

undefined8
FUN_ffff0000907a27fc(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  undefined8 uVar1;
  
  if (param_4 == 1) {
    uVar1 = FUN_ffff00009079f5b8(param_1,0x30,param_2,0x180,0x30,0xc00,param_3);
    return uVar1;
  }
  if (param_4 == 0) {
    uVar1 = FUN_ffff0000907a0200(param_1,0x20,param_2,0x100,0x20,0x800);
    return uVar1;
  }
  return 0x101023;
}


