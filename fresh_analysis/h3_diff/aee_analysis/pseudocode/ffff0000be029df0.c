
void FUN_ffff0000be029df0(uint param_1,int param_2)

{
  if (param_1 < 6) {
    if (param_1 - 1 == 0) {
      DAT_ffff0000be11816c = param_2;
      return;
    }
    (&DAT_ffff0000be118168)[param_1] = (&DAT_ffff0000be118168)[param_1 - 1] + param_2;
  }
  return;
}


