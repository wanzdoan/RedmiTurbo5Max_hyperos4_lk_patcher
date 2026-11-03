
undefined8 FUN_ffff0000b802d0a4(long *param_1,int *param_2)

{
  long lVar1;
  undefined8 uVar2;
  
  *param_2 = 0;
  lVar1 = FUN_ffff0000b804bccc(0x1000);
  *param_1 = lVar1;
  uVar2 = 0;
  if (lVar1 != 0) {
    if (*param_2 < 0x1001) {
      uVar2 = 1;
    }
    else {
      uVar2 = 1;
      *param_2 = 0x1000;
    }
  }
  return uVar2;
}


