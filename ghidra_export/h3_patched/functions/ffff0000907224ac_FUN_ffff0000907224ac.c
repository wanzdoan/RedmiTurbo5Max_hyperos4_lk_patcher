// ffff0000907224ac  size=64  FUN_ffff0000907224ac

undefined8 FUN_ffff0000907224ac(undefined1 *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  
  uVar1 = FUN_ffff000090703ca0();
  uVar1 = FUN_ffff0000907427fc(uVar1,param_2);
  if (-1 < (int)uVar1) {
    *param_1 = (char)uVar1;
    uVar1 = 0;
  }
  return uVar1;
}


