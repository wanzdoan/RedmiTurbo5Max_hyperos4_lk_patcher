// ffff00009078f634  size=168  FUN_ffff00009078f634

undefined8
FUN_ffff00009078f634(undefined8 param_1,undefined4 param_2,int param_3,uint param_4,long param_5)

{
  long lVar1;
  undefined8 uVar2;
  uint uVar3;
  
  lVar1 = FUN_ffff000090793148();
  if (*(long *)(lVar1 + 0x1f8) == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    for (; param_4 != 0; param_4 = param_4 - uVar3) {
      uVar3 = param_4;
      if (0x1fff < param_4) {
        uVar3 = 0x2000;
      }
      uVar2 = (**(code **)(lVar1 + 0x1f8))(lVar1,param_2,param_3,uVar3,param_5);
      if ((int)uVar2 != 0) {
        return uVar2;
      }
      param_3 = uVar3 + param_3;
      param_5 = param_5 + (ulong)(uVar3 << 9) * 8;
    }
    uVar2 = 0;
  }
  return uVar2;
}


