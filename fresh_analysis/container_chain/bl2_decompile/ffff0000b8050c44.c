
undefined8 FUN_ffff0000b8050c44(uint param_1,int param_2)

{
  ulong uVar1;
  char cVar2;
  ulong uVar3;
  long lVar4;
  undefined *puVar5;
  
  if (0x10e < param_1) {
    return 0xfffffffe;
  }
  uVar1 = (ulong)param_1;
  cVar2 = (&DAT_ffff0000b8103330)[uVar1];
  uVar3 = (ulong)cVar2;
  if (((uVar3 == 0xffffffffffffffff) && ((&DAT_ffff0000b8103cb8)[uVar1] == -1)) &&
     ((&DAT_ffff0000b8104640)[uVar1] == -1)) {
    return 0xffffffff;
  }
  if (param_2 == 0) {
    if (cVar2 == -1) {
      lVar4 = 4;
LAB_ffff0000b8050d88:
      cVar2 = (&DAT_ffff0000b8103cb8)[uVar1];
      if (cVar2 == -1) {
code_r0xffff0000b8050e34:
        puVar5 = &DAT_ffff0000b8103dc8;
        cVar2 = (&DAT_ffff0000b8104640)[uVar1];
        goto LAB_ffff0000b8050d9c;
      }
    }
    else {
      *(int *)(*(long *)(&DAT_ffff0000b8102ab8 + uVar1 * 8) + 8) = (int)(1L << (uVar3 & 0x3f));
      cVar2 = (&DAT_ffff0000b8103cb8)[uVar1];
      if (cVar2 == -1) {
        return 0;
      }
      lVar4 = 4;
    }
  }
  else {
    if (param_2 == 1) {
      if (cVar2 == -1) {
        lVar4 = 8;
        goto LAB_ffff0000b8050d88;
      }
      *(int *)(*(long *)(&DAT_ffff0000b8102ab8 + uVar1 * 8) + 4) = (int)(1L << (uVar3 & 0x3f));
code_r0xffff0000b8050d34:
      cVar2 = (&DAT_ffff0000b8103cb8)[uVar1];
      if (cVar2 == -1) {
        return 0;
      }
    }
    else {
      if (param_2 != 2) {
        return 0xfffffffe;
      }
      if (cVar2 != -1) {
        *(int *)(*(long *)(&DAT_ffff0000b8102ab8 + uVar1 * 8) + 8) = (int)(1L << (uVar3 & 0x3f));
        goto code_r0xffff0000b8050d34;
      }
      cVar2 = (&DAT_ffff0000b8103cb8)[uVar1];
      if (cVar2 == -1) {
        if ((&DAT_ffff0000b8104640)[uVar1] == -1) {
          lVar4 = 4;
        }
        else {
          *(int *)(*(long *)(&DAT_ffff0000b8104750 + uVar1 * 8) + 8) =
               (int)(1L << ((long)(char)(&DAT_ffff0000b8104fc8)[uVar1] & 0x3fU));
          lVar4 = 4;
          *(int *)(*(long *)(&DAT_ffff0000b81050d8 + uVar1 * 8) + 8) =
               (int)(1L << ((long)(char)(&DAT_ffff0000b8105950)[uVar1] & 0x3fU));
        }
        goto code_r0xffff0000b8050e34;
      }
    }
    lVar4 = 8;
  }
  puVar5 = &DAT_ffff0000b8103440;
LAB_ffff0000b8050d9c:
  *(int *)(*(long *)(puVar5 + uVar1 * 8) + lVar4) = (int)(1L << ((long)cVar2 & 0x3fU));
  return 0;
}


