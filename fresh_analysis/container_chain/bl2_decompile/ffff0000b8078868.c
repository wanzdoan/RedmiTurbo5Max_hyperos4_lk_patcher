
int * FUN_ffff0000b8078868(undefined8 *param_1,long param_2,uint *param_3)

{
  char cVar1;
  uint uVar2;
  int iVar3;
  int *piVar4;
  long *plVar5;
  long lVar6;
  undefined8 *puVar7;
  
  iVar3 = thunk_FUN_ffff0000b804d3cc(param_2);
  if ((param_1 != (undefined8 *)0x0) && (*(int *)*param_1 == 0x1000000)) {
    plVar5 = param_1 + 2;
    puVar7 = (undefined8 *)*plVar5;
    if (puVar7 != (undefined8 *)0x0) {
      lVar6 = (long)iVar3;
      do {
        if (((param_2 != 0) && (*(int *)*puVar7 == 0x3000000)) &&
           (iVar3 = thunk_FUN_ffff0000b804d4c4(puVar7[2],param_2,lVar6), iVar3 == 0)) {
          if (*(int *)*puVar7 == 0x1000000) {
            cVar1 = *(char *)((long)*puVar7 + lVar6 + 4);
          }
          else {
            cVar1 = *(char *)(puVar7[2] + lVar6);
          }
          if (cVar1 == '\0') {
            if ((undefined8 *)*plVar5 == (undefined8 *)0x0) {
              return (int *)0x0;
            }
            piVar4 = *(int **)*plVar5;
            if (*piVar4 == 0x3000000) {
              if (param_3 != (uint *)0x0) {
                uVar2 = (piVar4[1] & 0xff00ff00U) >> 8 | (piVar4[1] & 0xff00ffU) << 8;
                uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
                if ((int)uVar2 < 0) {
                  return (int *)0x0;
                }
                *param_3 = uVar2;
              }
              return piVar4 + 3;
            }
            return (int *)0x0;
          }
        }
        plVar5 = (long *)(*plVar5 + 8);
        puVar7 = (undefined8 *)*plVar5;
      } while (puVar7 != (undefined8 *)0x0);
    }
  }
  return (int *)0x0;
}


