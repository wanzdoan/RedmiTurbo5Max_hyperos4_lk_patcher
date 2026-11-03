
undefined8 FUN_ffff000090736908(long *param_1,int param_2)

{
  code *UNRECOVERED_JUMPTABLE;
  long lVar1;
  undefined8 uVar2;
  int *piVar3;
  ulong uVar4;
  
  if (((param_1 != (long *)0x0) && (lVar1 = *param_1, lVar1 != 0)) &&
     (uVar4 = (ulong)*(uint *)(lVar1 + 0x10), 0 < (int)*(uint *)(lVar1 + 0x10))) {
    piVar3 = *(int **)(lVar1 + 8);
    do {
      if (*piVar3 == param_2) {
        UNRECOVERED_JUMPTABLE = (code *)&LAB_ffff00009073696c;
        if (*(code **)(lVar1 + 0x30) != (code *)0x0) {
          UNRECOVERED_JUMPTABLE = *(code **)(lVar1 + 0x30);
        }
                    /* WARNING: Could not recover jumptable at 0xffff000090736968. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        uVar2 = (*UNRECOVERED_JUMPTABLE)(lVar1,piVar3);
        return uVar2;
      }
      piVar3 = piVar3 + 0xf;
      uVar4 = uVar4 - 1;
    } while (uVar4 != 0);
  }
  return 0xfffffff8;
}


