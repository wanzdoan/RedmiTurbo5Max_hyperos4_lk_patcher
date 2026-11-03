// ffff00009070f270  size=124  FUN_ffff00009070f270

void FUN_ffff00009070f270(long param_1,int param_2)

{
  if (param_2 == 0) {
    FUN_ffff0000907174e0(param_1 + 0x20);
    FUN_ffff0000907174e0(param_1 + 0x98,0);
    param_1 = param_1 + 0x130;
  }
  else {
    if (param_2 != 1) {
      return;
    }
    FUN_ffff0000907174e0(param_1 + 0x50,0);
    FUN_ffff0000907174e0(param_1 + 200,0);
    param_1 = param_1 + 0x100;
  }
  FUN_ffff0000907174e0(param_1,0);
  return;
}


