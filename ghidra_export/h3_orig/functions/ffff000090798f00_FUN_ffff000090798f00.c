// ffff000090798f00  size=48  FUN_ffff000090798f00

ulong FUN_ffff000090798f00(undefined8 param_1,ulong param_2,undefined8 param_3,ulong param_4)

{
  uint uVar1;
  ulong uVar2;
  
  if (param_2 == param_4) {
    uVar2 = FUN_ffff00009077e820(param_1,param_3,param_2);
    return uVar2;
  }
  uVar1 = 1;
  if (param_4 < param_2) {
    uVar1 = 0xffffffff;
  }
  return (ulong)uVar1;
}


