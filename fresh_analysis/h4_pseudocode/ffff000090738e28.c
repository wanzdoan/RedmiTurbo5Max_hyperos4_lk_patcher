
void FUN_ffff000090738e28(uint param_1,int param_2)

{
  if (param_1 < 6) {
    if (param_1 - 1 == 0) {
      DAT_ffff00009086deac = param_2;
      return;
    }
    (&DAT_ffff00009086dea8)[param_1] = (&DAT_ffff00009086dea8)[param_1 - 1] + param_2;
  }
  return;
}


