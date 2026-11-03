
void FUN_ffff0000be088fc4(ulong param_1,undefined8 param_2)

{
  if (-1 < (long)param_1) {
    FUN_ffff0000be088ff4(0,param_1,param_2);
    return;
  }
  FUN_ffff0000be088ff4(1,~param_1,param_2);
  return;
}


