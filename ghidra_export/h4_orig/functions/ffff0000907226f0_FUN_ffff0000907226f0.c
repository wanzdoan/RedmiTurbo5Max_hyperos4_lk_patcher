// ffff0000907226f0  size=64  FUN_ffff0000907226f0

undefined8 FUN_ffff0000907226f0(undefined1 *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  
  uVar1 = FUN_ffff000090703bfc();
  uVar1 = FUN_ffff000090742a94(uVar1,param_2);
  if (-1 < (int)uVar1) {
    *param_1 = (char)uVar1;
    uVar1 = 0;
  }
  return uVar1;
}


