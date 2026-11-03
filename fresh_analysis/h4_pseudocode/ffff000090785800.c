
undefined8 FUN_ffff000090785800(uint param_1,undefined4 param_2)

{
  char cVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  undefined *puVar5;
  
  if (0x10e < param_1) {
switchD_ffff000090785878_default:
    return 0xfffffffe;
  }
  uVar2 = (ulong)param_1;
  cVar1 = (&DAT_ffff000090833b90)[uVar2];
  if ((((long)cVar1 == 0xffffffffffffffff) && ((&DAT_ffff000090834518)[uVar2] == -1)) &&
     ((&DAT_ffff000090834ea0)[uVar2] == -1)) {
    return 0xffffffff;
  }
  switch(param_2) {
  case 0:
    if (cVar1 == -1) {
      cVar1 = (&DAT_ffff000090834518)[uVar2];
      lVar3 = 8;
      if (cVar1 == -1) {
        lVar4 = 8;
        break;
      }
    }
    else {
      *(int *)(*(long *)(&DAT_ffff000090833318 + uVar2 * 8) + 8) =
           (int)(1L << ((long)cVar1 & 0x3fU));
      cVar1 = (&DAT_ffff000090834518)[uVar2];
      if (cVar1 == -1) {
        return 0;
      }
    }
    lVar4 = 8;
    puVar5 = &DAT_ffff000090833ca0;
    goto LAB_ffff0000907859b4;
  case 1:
    if (cVar1 != -1) {
      return 0xffffffff;
    }
    if ((&DAT_ffff000090834518)[uVar2] != -1) {
      return 0xffffffff;
    }
    goto LAB_ffff0000907858f8;
  case 2:
    if ((&DAT_ffff000090834ea0)[uVar2] == -1) {
      return 0xffffffff;
    }
LAB_ffff0000907858f8:
    lVar4 = 8;
    lVar3 = 4;
    break;
  case 3:
    if ((&DAT_ffff000090834ea0)[uVar2] == -1) {
      return 0xffffffff;
    }
    lVar4 = 4;
    lVar3 = 8;
    break;
  case 4:
    if ((&DAT_ffff000090834ea0)[uVar2] == -1) {
      return 0xffffffff;
    }
    lVar3 = 4;
    lVar4 = 4;
    break;
  default:
    goto switchD_ffff000090785878_default;
  }
  *(int *)(*(long *)(&DAT_ffff000090834fb0 + uVar2 * 8) + lVar3) =
       (int)(1L << ((long)(char)(&DAT_ffff000090835828)[uVar2] & 0x3fU));
  cVar1 = (&DAT_ffff0000908361b0)[uVar2];
  puVar5 = &DAT_ffff000090835938;
LAB_ffff0000907859b4:
  *(int *)(*(long *)(puVar5 + uVar2 * 8) + lVar4) = (int)(1L << ((long)cVar1 & 0x3fU));
  return 0;
}


