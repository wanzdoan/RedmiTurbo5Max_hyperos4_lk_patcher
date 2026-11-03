
undefined8 FUN_ffff0000be020364(undefined1 *param_1,undefined4 param_2)

{
  undefined8 uVar1;
  
  uVar1 = FUN_ffff0000be00375c();
  uVar1 = FUN_ffff0000be032420(uVar1,param_2);
  if (-1 < (int)uVar1) {
    *param_1 = (char)uVar1;
    uVar1 = 0;
  }
  return uVar1;
}


