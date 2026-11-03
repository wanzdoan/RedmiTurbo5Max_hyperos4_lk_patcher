
undefined8 FUN_ffff0000b8079eb8(undefined8 *param_1,undefined8 param_2)

{
  long lVar1;
  undefined8 uVar2;
  
  lVar1 = thunk_FUN_ffff0000b804bccc(0x20);
  if (lVar1 == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    thunk_FUN_ffff0000b804d070(lVar1,0,0x20);
    uVar2 = 0;
    param_1[2] = lVar1;
    *param_1 = 4;
    param_1[1] = param_2;
  }
  return uVar2;
}


