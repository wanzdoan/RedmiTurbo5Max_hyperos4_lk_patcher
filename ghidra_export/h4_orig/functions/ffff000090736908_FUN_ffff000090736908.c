// ffff000090736908  size=100  FUN_ffff000090736908

undefined8 FUN_ffff000090736908(long *param_1,int param_2)

{
  code *pcVar1;
  long lVar2;
  undefined8 uVar3;
  int *piVar4;
  ulong uVar5;
  
  if (((param_1 != (long *)0x0) && (lVar2 = *param_1, lVar2 != 0)) &&
     (uVar5 = (ulong)*(uint *)(lVar2 + 0x10), 0 < (int)*(uint *)(lVar2 + 0x10))) {
    piVar4 = *(int **)(lVar2 + 8);
    do {
      if (*piVar4 == param_2) {
        pcVar1 = FUN_ffff00009073696c;
        if (*(code **)(lVar2 + 0x30) != (code *)0x0) {
          pcVar1 = *(code **)(lVar2 + 0x30);
        }
        uVar3 = (*pcVar1)(lVar2,piVar4);
        return uVar3;
      }
      piVar4 = piVar4 + 0xf;
      uVar5 = uVar5 - 1;
    } while (uVar5 != 0);
  }
  return 0xfffffff8;
}


