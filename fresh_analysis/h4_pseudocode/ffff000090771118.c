
undefined8 * FUN_ffff000090771118(undefined8 *param_1,char *param_2,int param_3)

{
  char *pcVar1;
  char cVar2;
  int iVar3;
  char *pcVar4;
  long *plVar5;
  char *pcVar6;
  char *pcVar7;
  long lVar8;
  undefined8 *puVar9;
  
  pcVar1 = param_2 + param_3;
LAB_ffff000090771144:
  if (pcVar1 <= param_2) {
    return param_1;
  }
  pcVar6 = param_2 + -1;
  pcVar7 = pcVar6;
  do {
    pcVar6 = pcVar6 + 1;
    pcVar7 = pcVar7 + 1;
  } while (*pcVar6 == '/');
  if (pcVar6 == pcVar1) {
    return param_1;
  }
  pcVar4 = (char *)thunk_FUN_ffff00009077eab8(pcVar6,0x2f,(long)pcVar1 - (long)pcVar7);
  param_2 = pcVar1;
  if (pcVar4 != (char *)0x0) {
    param_2 = pcVar4;
  }
  if (param_1 == (undefined8 *)0x0) {
    return (undefined8 *)0x0;
  }
  if (*(int *)*param_1 == 0x1000000) {
    plVar5 = param_1 + 2;
    puVar9 = (undefined8 *)*plVar5;
    if (puVar9 != (undefined8 *)0x0) {
      lVar8 = (long)((int)param_2 - (int)pcVar7);
      do {
        if ((*(int *)*puVar9 == 0x1000000) &&
           (iVar3 = thunk_FUN_ffff00009077f348((int *)*puVar9 + 1,pcVar6,lVar8), iVar3 == 0)) {
          if (*(int *)*puVar9 == 0x1000000) {
            cVar2 = *(char *)((long)*puVar9 + lVar8 + 4);
          }
          else {
            cVar2 = *(char *)(puVar9[2] + lVar8);
          }
          if (cVar2 == '\0') goto LAB_ffff000090771204;
        }
        plVar5 = (long *)(*plVar5 + 8);
        puVar9 = (undefined8 *)*plVar5;
        if (puVar9 == (undefined8 *)0x0) {
          return (undefined8 *)0x0;
        }
      } while( true );
    }
  }
  return (undefined8 *)0x0;
LAB_ffff000090771204:
  param_1 = (undefined8 *)*plVar5;
  if (param_1 == (undefined8 *)0x0) {
    return (undefined8 *)0x0;
  }
  goto LAB_ffff000090771144;
}


