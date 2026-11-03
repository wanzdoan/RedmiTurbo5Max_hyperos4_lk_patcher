// ffff000090770e3c  size=220  FUN_ffff000090770e3c

long FUN_ffff000090770e3c(undefined8 *param_1,long param_2)

{
  char cVar1;
  int iVar2;
  long *plVar3;
  long lVar4;
  undefined8 *puVar5;
  
  iVar2 = thunk_FUN_ffff00009077f250(param_2);
  if ((param_1 != (undefined8 *)0x0) && (*(int *)*param_1 == 0x1000000)) {
    plVar3 = param_1 + 2;
    puVar5 = (undefined8 *)*plVar3;
    if (puVar5 != (undefined8 *)0x0) {
      lVar4 = (long)iVar2;
      do {
        if (((param_2 != 0) && (*(int *)*puVar5 == 0x3000000)) &&
           (iVar2 = thunk_FUN_ffff00009077f348(puVar5[2],param_2,lVar4), iVar2 == 0)) {
          if (*(int *)*puVar5 == 0x1000000) {
            cVar1 = *(char *)((long)*puVar5 + lVar4 + 4);
          }
          else {
            cVar1 = *(char *)(puVar5[2] + lVar4);
          }
          if (cVar1 == '\0') {
            return *plVar3;
          }
        }
        plVar3 = (long *)(*plVar3 + 8);
        puVar5 = (undefined8 *)*plVar3;
      } while (puVar5 != (undefined8 *)0x0);
    }
  }
  return 0;
}


