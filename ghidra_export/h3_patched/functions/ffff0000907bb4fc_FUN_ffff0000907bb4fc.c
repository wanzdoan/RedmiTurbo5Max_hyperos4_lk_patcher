// ffff0000907bb4fc  size=56  FUN_ffff0000907bb4fc

int FUN_ffff0000907bb4fc(byte *param_1,byte *param_2,long param_3)

{
  while( true ) {
    if (param_3 == 0) {
      return 0;
    }
    if ((uint)*param_1 - (uint)*param_2 != 0) break;
    param_1 = param_1 + 1;
    param_2 = param_2 + 1;
    param_3 = param_3 + -1;
  }
  return (uint)*param_1 - (uint)*param_2;
}


