// ffff000090785538  size=488  FUN_ffff000090785538

undefined8 FUN_ffff000090785538(uint param_1,undefined4 param_2)

{
  char cVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  undefined *puVar5;
  
  if (0x10e < param_1) {
switchD_ffff0000907855b0_default:
    return 0xfffffffe;
  }
  uVar2 = (ulong)param_1;
  cVar1 = (&DAT_ffff000090833b70)[uVar2];
  if ((((long)cVar1 == 0xffffffffffffffff) && ((&DAT_ffff0000908344f8)[uVar2] == -1)) &&
     ((&DAT_ffff000090834e80)[uVar2] == -1)) {
    return 0xffffffff;
  }
  switch(param_2) {
  case 0:
    if (cVar1 == -1) {
      cVar1 = (&DAT_ffff0000908344f8)[uVar2];
      lVar3 = 8;
      if (cVar1 == -1) {
        lVar4 = 8;
        break;
      }
    }
    else {
      *(int *)(*(long *)(&DAT_ffff0000908332f8 + uVar2 * 8) + 8) =
           (int)(1L << ((long)cVar1 & 0x3fU));
      cVar1 = (&DAT_ffff0000908344f8)[uVar2];
      if (cVar1 == -1) {
        return 0;
      }
    }
    lVar4 = 8;
    puVar5 = &DAT_ffff000090833c80;
    goto LAB_ffff0000907856ec;
  case 1:
    if (cVar1 != -1) {
      return 0xffffffff;
    }
    if ((&DAT_ffff0000908344f8)[uVar2] != -1) {
      return 0xffffffff;
    }
    goto LAB_ffff000090785630;
  case 2:
    if ((&DAT_ffff000090834e80)[uVar2] == -1) {
      return 0xffffffff;
    }
LAB_ffff000090785630:
    lVar4 = 8;
    lVar3 = 4;
    break;
  case 3:
    if ((&DAT_ffff000090834e80)[uVar2] == -1) {
      return 0xffffffff;
    }
    lVar4 = 4;
    lVar3 = 8;
    break;
  case 4:
    if ((&DAT_ffff000090834e80)[uVar2] == -1) {
      return 0xffffffff;
    }
    lVar3 = 4;
    lVar4 = 4;
    break;
  default:
    goto switchD_ffff0000907855b0_default;
  }
  *(int *)(*(long *)(&DAT_ffff000090834f90 + uVar2 * 8) + lVar3) =
       (int)(1L << ((long)(char)(&DAT_ffff000090835808)[uVar2] & 0x3fU));
  cVar1 = (&DAT_ffff000090836190)[uVar2];
  puVar5 = &DAT_ffff000090835918;
LAB_ffff0000907856ec:
  *(int *)(*(long *)(puVar5 + uVar2 * 8) + lVar4) = (int)(1L << ((long)cVar1 & 0x3fU));
  return 0;
}


