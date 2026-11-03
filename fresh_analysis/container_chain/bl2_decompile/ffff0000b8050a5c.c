
undefined8 FUN_ffff0000b8050a5c(uint param_1,undefined4 param_2)

{
  char cVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  undefined *puVar5;
  
  if (0x10e < param_1) {
switchD_ffff0000b8050ad4_default:
    return 0xfffffffe;
  }
  uVar2 = (ulong)param_1;
  cVar1 = (&DAT_ffff0000b8103330)[uVar2];
  if ((((long)cVar1 == 0xffffffffffffffff) && ((&DAT_ffff0000b8103cb8)[uVar2] == -1)) &&
     ((&DAT_ffff0000b8104640)[uVar2] == -1)) {
    return 0xffffffff;
  }
  switch(param_2) {
  case 0:
    if (cVar1 == -1) {
      cVar1 = (&DAT_ffff0000b8103cb8)[uVar2];
      lVar3 = 8;
      if (cVar1 == -1) {
        lVar4 = 8;
        break;
      }
    }
    else {
      *(int *)(*(long *)(&DAT_ffff0000b8102ab8 + uVar2 * 8) + 8) =
           (int)(1L << ((long)cVar1 & 0x3fU));
      cVar1 = (&DAT_ffff0000b8103cb8)[uVar2];
      if (cVar1 == -1) {
        return 0;
      }
    }
    lVar4 = 8;
    puVar5 = &DAT_ffff0000b8103440;
    goto LAB_ffff0000b8050c10;
  case 1:
    if (cVar1 != -1) {
      return 0xffffffff;
    }
    if ((&DAT_ffff0000b8103cb8)[uVar2] != -1) {
      return 0xffffffff;
    }
    goto LAB_ffff0000b8050b54;
  case 2:
    if ((&DAT_ffff0000b8104640)[uVar2] == -1) {
      return 0xffffffff;
    }
LAB_ffff0000b8050b54:
    lVar4 = 8;
    lVar3 = 4;
    break;
  case 3:
    if ((&DAT_ffff0000b8104640)[uVar2] == -1) {
      return 0xffffffff;
    }
    lVar4 = 4;
    lVar3 = 8;
    break;
  case 4:
    if ((&DAT_ffff0000b8104640)[uVar2] == -1) {
      return 0xffffffff;
    }
    lVar3 = 4;
    lVar4 = 4;
    break;
  default:
    goto switchD_ffff0000b8050ad4_default;
  }
  *(int *)(*(long *)(&DAT_ffff0000b8104750 + uVar2 * 8) + lVar3) =
       (int)(1L << ((long)(char)(&DAT_ffff0000b8104fc8)[uVar2] & 0x3fU));
  cVar1 = (&DAT_ffff0000b8105950)[uVar2];
  puVar5 = &DAT_ffff0000b81050d8;
LAB_ffff0000b8050c10:
  *(int *)(*(long *)(puVar5 + uVar2 * 8) + lVar4) = (int)(1L << ((long)cVar1 & 0x3fU));
  return 0;
}


