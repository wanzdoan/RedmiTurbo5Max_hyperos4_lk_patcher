// File offset entry: 0x91394

int FUN_02091294(int param_1)

{
  int *piVar1;
  
  piVar1 = (int *)FUN_020036c8(1);
  if (piVar1 == (int *)0x0) {
    FUN_0208ada0(0x20b0cbf,0x20cec01,1);
  }
  else {
    if (*piVar1 == 2) {
      if (param_1 - 1U < 4) {
        return param_1;
      }
      return 3;
    }
    if (*piVar1 == 1) {
      if (param_1 - 1U < 4) {
        return *(int *)((long)(int)(param_1 - 1U) * 4 + 0x20d1d48);
      }
      return 0;
    }
  }
  return -1;
}


